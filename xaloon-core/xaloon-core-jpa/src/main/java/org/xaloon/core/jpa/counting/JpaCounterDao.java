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

import org.xaloon.core.api.counting.CounterDao;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.jpa.counting.model.JpaCounterEntity;

@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaCounterDao implements CounterDao {

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Override
	public void increment(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterEntity entity = find(counterGroup, categoryId, entityId);
		if (entity == null) {
			create(counterGroup, categoryId, entityId);
			return;
		}
		entity.setCount(entity.getCount() + 1);
		persistenceServices.edit(entity);
	}

	private void create(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterEntity newEntity = new JpaCounterEntity();
		newEntity.setCounterGroup(counterGroup);
		newEntity.setCategoryId(categoryId);
		newEntity.setEntityId(entityId);
		newEntity.setCount(1L);
		persistenceServices.create(newEntity);
	}

	private JpaCounterEntity find(String counterGroup, Long categoryId, Long entityId) {
		QueryBuilder queryBuilder = new QueryBuilder("select ce from " + JpaCounterEntity.class.getSimpleName() + " ce");
		queryBuilder.addParameter("ce.counterGroup", "GROUP_TO_UPDATE", counterGroup);
		queryBuilder.addParameter("ce.categoryId", "CATEGORY_ID", categoryId);
		queryBuilder.addParameter("ce.entityId", "ENTTY_ID", entityId);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public Long count(String counterGroup, Long categoryId, Long entityId) {
		JpaCounterEntity counterEntity = find(counterGroup, categoryId, entityId);
		return (counterEntity != null) ? counterEntity.getCount() : 0L;
	}
}
