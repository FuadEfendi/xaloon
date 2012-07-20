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

import java.util.Date;
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
import org.xaloon.core.api.security.RoleService;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityGroup;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.core.api.security.model.UserDetails;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.security.model.JpaGroup;
import org.xaloon.core.jpa.security.model.JpaRole;
import org.xaloon.core.jpa.security.model.JpaUserDetails;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaRoleService implements RoleService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PersistenceServices persistenceServices;

	@Override
	public List<SecurityRole> getAuthorities(long first, long count) {
		QueryBuilder queryBuilder = new QueryBuilder("select g from " + JpaRole.class.getSimpleName() + " g");
		queryBuilder.setCount(count);
		queryBuilder.setFirstRow(first);
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public Long getCount() {
		QueryBuilder queryBuilder = new QueryBuilder("select count(g) from " + JpaRole.class.getSimpleName() + " g");
		return (Long)persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public SecurityRole newAuthority() {
		return new JpaRole();
	}

	@Override
	public void delete(SecurityRole authority) {
		SecurityRole tmp = persistenceServices.find(authority.getClass(), authority.getId());


		// !!! Eclipselink related issue. it does not return the list of users.
		// Remove the role from users
		List<UserDetails> users = getUsersByAuthority(authority);
		for (UserDetails details : users) {
			details.getRoles().remove(tmp);
		}
		// Remove the role from groups
		List<SecurityGroup> groups = getGroupByAuthority(authority);
		for (SecurityGroup group : groups) {
			group.getRoles().remove(tmp);
		}
		persistenceServices.remove(tmp);
	}

	private List<SecurityGroup> getGroupByAuthority(SecurityRole authority) {
		QueryBuilder queryBuilder = new QueryBuilder("select g from " + JpaGroup.class.getSimpleName() + " g join g.roles r");
		queryBuilder.addParameter("r.id", "_ID", authority.getId());
		return persistenceServices.executeQuery(queryBuilder);
	}

	private List<UserDetails> getUsersByAuthority(SecurityRole authority) {
		QueryBuilder queryBuilder = new QueryBuilder("select u from " + JpaUserDetails.class.getSimpleName() + " u join u.roles r");
		queryBuilder.addParameter("r.id", "_ID", authority.getId());
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public SecurityRole findOrCreateAuthority(String authorityName) {
		SecurityRole securityRole = getAuthorityByPath(UrlUtil.encode(authorityName));
		if (securityRole == null) {
			securityRole = createNewRole(authorityName);
		}
		return securityRole;
	}

	@Override
	public List<SecurityRole> getAuthoritiesByUsername(String username) {
		QueryBuilder queryBuilder = new QueryBuilder("select r from " + JpaRole.class.getSimpleName() + " r join r.users u");
		queryBuilder.addParameter("u.username", "USERNAME", username);
		return persistenceServices.executeQuery(queryBuilder);
	}

	@Override
	public UserDetails revoke(UserDetails userDetails, SecurityRole authority) {
		UserDetails tmp = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		tmp.getRoles().remove(authority);
		return persistenceServices.edit(tmp);
	}

	@Override
	public SecurityRole getAuthorityByPath(String path) {
		QueryBuilder queryBuilder = new QueryBuilder("select r from " + JpaRole.class.getSimpleName() + " r");
		queryBuilder.addParameter("r.path", "PATH", path);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public UserDetails assignAuthoritiesByName(UserDetails userDetails, List<String> selections) {
		UserDetails tmp = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		for (String roleName : selections) {
			SecurityRole role = findOrCreateAuthority(roleName);
			tmp.getRoles().add(role);
		}
		return persistenceServices.edit(tmp);
	}

	@Override
	public UserDetails assignAuthorities(UserDetails userDetails, List<SecurityRole> selections) {
		UserDetails tmp = persistenceServices.find(userDetails.getClass(), userDetails.getId());
		tmp.getRoles().addAll(selections);
		return persistenceServices.edit(tmp);
	}

	@Override
	public SecurityRole assignChildren(SecurityRole parent, List<Authority> selections) {
		SecurityRole tmp = persistenceServices.find(parent.getClass(), parent.getId());
		tmp.getAuthorities().addAll(selections);
		return persistenceServices.edit(tmp);
	}

	@Override
	public SecurityRole revokeChild(SecurityRole parent, Authority authority) {
		SecurityRole tmp = persistenceServices.find(parent.getClass(), parent.getId());
		tmp.getAuthorities().remove(authority);
		return persistenceServices.edit(tmp);
	}

	@Override
	public SecurityRole save(SecurityRole authority) {
		if (authority == null) {
			return null;
		}
		if (StringUtils.isEmpty(authority.getPath())) {
			authority.setPath(UrlUtil.encode(authority.getName()));
		}
		return persistenceServices.createOrEdit(authority);
	}

	private SecurityRole createNewRole(String name) {
		SecurityRole role = newAuthority();
		role.setCreateDate(new Date());
		role.setUpdateDate(new Date());
		role.setName(name);
		return save(role);
	}

	@Override
	public SecurityRole getAuthorityByName(String name) {
		QueryBuilder queryBuilder = new QueryBuilder("select r from " + JpaRole.class.getSimpleName() + " r");
		queryBuilder.addParameter("r.name", "_NAME", name);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}
}
