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

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityEntity;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.security.model.JpaAuthority;
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
	public <T extends SecurityEntity> void save(T entity) {
		if (StringUtils.isEmpty(entity.getPath())) {
			entity.setPath(UrlUtil.encode(entity.getName()));
		}
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

	@Override
	public <T extends SecurityGroup> UserDetails assignGroups(UserDetails userDetails, List<T> selections) {
		userDetails = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		userDetails.getGroups().addAll(selections);
		return persistenceServices.edit(userDetails);
	}

	@Override
	public <T extends SecurityRole> UserDetails assignRoles(UserDetails userDetails, List<T> selections) {
		userDetails = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		userDetails.getRoles().addAll(selections);
		return persistenceServices.edit(userDetails);
	}

	@Override
	public List<Authority> getAuthorityList(int first, int count) {
		QueryBuilder queryBuilder = new QueryBuilder("select a from " + JpaAuthority.class.getSimpleName() + " a");
		queryBuilder.setCount(count);
		queryBuilder.setFirstRow(first);
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public <T extends Authority> UserDetails assignAuthorities(UserDetails userDetails, List<T> selections) {
		userDetails = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		userDetails.getAuthorities().addAll(selections);
		return persistenceServices.edit(userDetails);
	}

	@Override
	public UserDetails revokeAuthority(UserDetails userDetails, Authority authority) {
		userDetails = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		userDetails.getAuthorities().remove(authority);
		return persistenceServices.edit(userDetails);
	}

	@Override
	public UserDetails revokeGroup(UserDetails userDetails, SecurityGroup group) {
		userDetails = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		userDetails.getGroups().remove(group);
		return persistenceServices.edit(userDetails);
	}

	@Override
	public UserDetails revokeRole(UserDetails userDetails, SecurityRole role) {
		userDetails = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		userDetails.getRoles().remove(role);
		return persistenceServices.edit(userDetails);
	}

	@Override
	public SecurityRole getRoleByPath(String path) {
		QueryBuilder queryBuilder = new QueryBuilder("select r from " + JpaRole.class.getSimpleName() + " r");
		queryBuilder.addParameter("r.path", "PATH", path);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public SecurityRole assignAuthorities(SecurityRole role, List<Authority> selections) {
		role = persistenceServices.find(role.getClass(), role.getId());
		role.getAuthorities().addAll(selections);
		return persistenceServices.edit(role);
	}

	@Override
	public SecurityRole revokeAuthority(SecurityRole role, Authority authority) {
		role = persistenceServices.find(role.getClass(), role.getId());
		role.getAuthorities().remove(authority);
		return persistenceServices.edit(role);
	}

	@Override
	public SecurityGroup getGroupByPath(String path) {
		QueryBuilder queryBuilder = new QueryBuilder("select g from " + JpaGroup.class.getSimpleName() + " g");
		queryBuilder.addParameter("g.path", "PATH", path);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public SecurityGroup revokeRoleFromGroup(SecurityGroup group, SecurityRole role) {
		group = persistenceServices.find(group.getClass(), group.getId());
		group.getRoles().remove(role);
		return persistenceServices.edit(group);
	}

	@Override
	public SecurityGroup assignRolesToGroup(SecurityGroup group, List<SecurityRole> selections) {
		group = persistenceServices.find(group.getClass(), group.getId());
		group.getRoles().addAll(selections);
		return persistenceServices.edit(group);
	}

	@Override
	public void delete(SecurityGroup group) {
		persistenceServices.remove(group);
	}

	@Override
	public void delete(SecurityRole role) {
		persistenceServices.remove(role);
	}
}
