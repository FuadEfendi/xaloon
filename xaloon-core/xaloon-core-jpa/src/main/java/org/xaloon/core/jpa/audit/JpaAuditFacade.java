/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xaloon.core.jpa.audit;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xaloon.core.api.audit.AuditFacade;
import org.xaloon.core.api.audit.annotation.Auditable;
import org.xaloon.core.api.audit.model.AuditEntity;
import org.xaloon.core.api.audit.model.AuditEntityItem;
import org.xaloon.core.api.audit.model.AuditState;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.util.ClassUtil;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.audit.model.JpaAuditEntity;
import org.xaloon.core.jpa.audit.model.JpaAuditEntityItem;

/**
 * @author vytautas r.
 */
@Named("auditFacade")
@Stateless(mappedName = "auditFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaAuditFacade implements AuditFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Log logger = LogFactory.getLog(JpaAuditFacade.class);

	private transient EntityManager em;

	@Inject
	private PersistenceServices persistenceServices;

	/**
	 * @param em
	 *            instance used to interact with the persistence context.
	 */
	@PersistenceContext(unitName = "default-persistence-unit")
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public void audit(AuditState state, Object auditableObject) {
		if (auditableObject == null) {
			return;
		}
		// We create auditable object in order to send it to JMS or to synchronous save
		AuditEntity auditEntity = createAuditEntity(state, auditableObject);
		if (auditEntity == null) {
			return;
		}
		save(auditEntity);
	}

	private AuditEntity createAuditEntity(AuditState state, Object auditableObject) {
		Auditable annotation = ClassUtil.getAnnotation(auditableObject.getClass(), Auditable.class);
		if (annotation == null) {
			return null;
		}
		AuditEntity ae = newAuditEntity();
		ae.setAuditableName(auditableObject.getClass().getName());
		ae.setAuditState(state);
		ae.setCreateDate(new Date());
		ae.setUpdateDate(new Date());
		ae.setPath(UrlUtil.encode(ae.getAuditableName()));
		createAuditDetails(ae, auditableObject, auditableObject.getClass());
		return ae;
	}

	private void createAuditDetails(AuditEntity ae, Object auditObject, Class<?> parentClass) {
		if (parentClass == null) {
			return;
		}
		for (Field field : parentClass.getDeclaredFields()) {
			Auditable annotation = field.getAnnotation(Auditable.class);
			if (annotation != null) {
				String fieldName = field.getName();
				try {
					if (annotation.index() > -1) {
						Object value = PropertyUtils.getIndexedProperty(auditObject, fieldName, annotation.index());
						createAuditDetails(ae, value, value.getClass());
					} else {
						Object value = PropertyUtils.getSimpleProperty(auditObject, fieldName);
						AuditEntityItem details = newAuditEntityItem();
						details.setAuditEntity(ae);
						details.setCreateDate(new Date());
						details.setUpdateDate(new Date());
						details.setName(parentClass.getName() + "." + fieldName);
						if (value != null) {
							details.setValue(value.toString());
						}
						details.setKey(annotation.key());
						ae.getAuditEntityItems().add(details);
					}
				} catch (Exception e) {
					logger.error("Exception while getting audit field value", e);
				}
			}
		}
		createAuditDetails(ae, auditObject, parentClass.getSuperclass());
	}

	@Override
	public List<AuditEntity> search(List<String> auditableEntityNames, int first, int count) {
		QueryBuilder query = new QueryBuilder("select a from " + JpaAuditEntity.class.getSimpleName() + " a order by a.createDate desc");
		query.setFirstRow(first);
		query.setCount(count);
		return persistenceServices.executeQuery(query);
	}

	private QueryBuilder createQuery(String selectQuery, List<String> objectNames) {
		QueryBuilder query = new QueryBuilder(selectQuery);
		StringBuilder expression = new StringBuilder();
		for (String item : objectNames) {
			if (expression.length() > 0) {
				expression.append(",");
			}
			expression.append("'");
			expression.append(item);
			expression.append("'");
		}
		if (expression.length() > 0) {
			expression.insert(0, "( ");
			expression.append(" )");
		} else {
			return null;
		}
		// query.addExpression("a.auditableName IN " + expression.toString());
		return query;
	}

	private void save(AuditEntity auditEntity) {
		if (auditEntity == null) {
			return;
		}
		if (AuditState.CREATE.equals(auditEntity.getAuditState())) {
			em.persist(auditEntity);
			return;
		}
		AuditEntity existingEntity = null;// TODO fix
		if (existingEntity == null) {
			existingEntity = auditEntity;
			em.persist(auditEntity);
			return;
		}
		existingEntity.setUpdateDate(new Date());
		em.merge(existingEntity);
	}

	private AuditEntity newAuditEntity() {
		return new JpaAuditEntity();
	}

	private AuditEntityItem newAuditEntityItem() {
		return new JpaAuditEntityItem();
	}

}
