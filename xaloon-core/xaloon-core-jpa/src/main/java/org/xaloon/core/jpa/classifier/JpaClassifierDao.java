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
package org.xaloon.core.jpa.classifier;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.classifier.Classifier;
import org.xaloon.core.api.classifier.dao.ClassifierDao;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.jpa.classifier.model.JpaClassifier;

/**
 * @author vytautas r.
 */
@Named("classifierDao")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaClassifierDao implements ClassifierDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Override
	public Long getCount() {
		return persistenceServices.getCount(JpaClassifier.class);
	}

	@Override
	public <T extends Classifier> List<T> findClassifiers(long first, long count) {
		QueryBuilder queryBuilder = new QueryBuilder("select cl from " + JpaClassifier.class.getSimpleName() + " cl ");
		queryBuilder.setFirstRow(first);
		queryBuilder.setCount(count);
		queryBuilder.addOrderBy("cl.name asc");
		return persistenceServices.executeQuery(queryBuilder);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Classifier> T loadClassifierById(Long id) {
		return (T)persistenceServices.find(JpaClassifier.class, id);
	}

	@Override
	public <T extends Classifier> void createClassifier(T item) {
		persistenceServices.createOrEdit(item);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Classifier> T newClassifier() {
		return (T)new JpaClassifier();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Classifier> T findClassifierByType(String type) {
		QueryBuilder queryBuilder = new QueryBuilder("select cl from " + JpaClassifier.class.getSimpleName() + " cl ");
		queryBuilder.addParameter("cl.type", "CL_TYPE", type.toUpperCase());
		return (T)persistenceServices.executeQuerySingle(queryBuilder);
	}
}
