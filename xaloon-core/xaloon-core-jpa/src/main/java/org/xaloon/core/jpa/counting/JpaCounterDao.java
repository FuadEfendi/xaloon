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
package org.xaloon.core.jpa.counting;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.counting.CounterDao;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.jpa.counting.model.JpaCounterEntity;
import org.xaloon.core.jpa.counting.model.JpaCounterId;

/**
 * @author vytautas r.
 */
@Named("counterDao")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaCounterDao implements CounterDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaCounterDao.class);

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Override
	public boolean increment(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterId id = createPrimaryKey(counterGroup, categoryId, entityId);
		if (incrementValue(id) == false) {
			return createNewSequence(id, 1L) != null;
		}
		return true;
	}

	private JpaCounterEntity createNewSequence(JpaCounterId id, long l) {
		JpaCounterEntity newEntity = new JpaCounterEntity();
		newEntity.setCounterId(id);
		newEntity.setCount(1L);
		return persistenceServices.create(newEntity);
	}

	private boolean incrementValue(JpaCounterId id) {
		QueryBuilder queryBuilder = new QueryBuilder("UPDATE JpaCounterEntity as s set s.count = s.count + 1");
		queryBuilder.addParameter("s.counterId", "_ID", id);
		int rows = persistenceServices.executeUpdate(queryBuilder);
		return (rows == 1);
	}

	private JpaCounterEntity find(JpaCounterId primaryKey) {
		return persistenceServices.find(JpaCounterEntity.class, primaryKey);
	}

	private JpaCounterId createPrimaryKey(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterId id = new JpaCounterId();
		id.setCounterGroup(counterGroup);
		id.setCategoryId(categoryId);
		id.setEntityId(entityId);
		return id;
	}

	@Override
	public Long count(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterEntity counterEntity = find(createPrimaryKey(counterGroup, categoryId, entityId));
		return (counterEntity != null) ? counterEntity.getCount() : 0L;
	}

	@Override
	public boolean decrement(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterId id = createPrimaryKey(counterGroup, categoryId, entityId);

		QueryBuilder queryBuilder = new QueryBuilder("UPDATE JpaCounterEntity as s set s.count = s.count - 1");
		queryBuilder.addParameter("s.counterId", "_ID", id);
		int rows = persistenceServices.executeUpdate(queryBuilder);
		return (rows == 1);
	}
}