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
package org.xaloon.core.jpa.security;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.persistence.Persistable;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.jpa.security.model.JpaGroup;
import org.xaloon.core.jpa.security.model.JpaRole;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaRoleGroupService implements RoleGroupService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PersistenceServices persistenceServices;

	@Override
	public int getGroupCount() {
		QueryBuilder queryBuilder = new QueryBuilder("select count(g) from " + JpaGroup.class.getSimpleName() + " g");
		return ((Long)persistenceServices.executeQuerySingle(queryBuilder)).intValue();
	}

	@Override
	public List<SecurityGroup> getGroupList(int first, int count) {
		QueryBuilder queryBuilder = new QueryBuilder("select g from " + JpaGroup.class.getSimpleName() + " g");
		queryBuilder.setCount(count);
		queryBuilder.setFirstRow(first);
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public List<SecurityRole> getRoleList(int first, int count) {
		QueryBuilder queryBuilder = new QueryBuilder("select g from " + JpaRole.class.getSimpleName() + " g");
		queryBuilder.setCount(count);
		queryBuilder.setFirstRow(first);
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public int getRoleCount() {
		QueryBuilder queryBuilder = new QueryBuilder("select count(g) from " + JpaRole.class.getSimpleName() + " g");
		return ((Long)persistenceServices.executeQuerySingle(queryBuilder)).intValue();
	}

	@Override
	public SecurityGroup newGroup() {
		return new JpaGroup();
	}

	@Override
	public <T extends Persistable> void save(T entity) {
		persistenceServices.create(entity);
	}

	@Override
	public SecurityRole newRole() {
		return new JpaRole();
	}

	@Override
	public List<SecurityRole> getRolesByUsername(String username) {
		QueryBuilder queryBuilder = new QueryBuilder("select r from " + JpaRole.class.getSimpleName() + " r join r.users u");
		queryBuilder.addParameter("u.username", "USERNAME", username);
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public List<SecurityGroup> getGroupsByUsername(String username) {
		QueryBuilder queryBuilder = new QueryBuilder("select g from " + JpaGroup.class.getSimpleName() + " g join g.users u");
		queryBuilder.addParameter("u.username", "USERNAME", username);
		return persistenceServices.executeQuery(queryBuilder);
	}

}
