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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.persistence.QueryBuilder.Condition;
import org.xaloon.core.api.security.LoginService;
import org.xaloon.core.api.security.PasswordEncoder;
import org.xaloon.core.api.security.RoleService;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.core.api.security.model.UserDetails;
import org.xaloon.core.jpa.security.model.JpaAuthority;
import org.xaloon.core.jpa.security.model.JpaGroup;
import org.xaloon.core.jpa.security.model.JpaRole;
import org.xaloon.core.jpa.security.model.JpaUserAlias;
import org.xaloon.core.jpa.security.model.JpaUserDetails;

/**
 * @author vytautas r.
 */
@Named("loginService")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class LocalDatabaseLoginService implements LoginService {
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Inject
	private RoleService roleService;

	@Override
	public boolean performLogin(String username, String password) {
		QueryBuilder queryBuilder = new QueryBuilder("select ud from " + JpaUserDetails.class.getSimpleName() + " ud");
		queryBuilder.addParameter("ud.username", "_USERNAME", username);
		queryBuilder.addParameter("ud.password", "_PASSWORD", password);
		queryBuilder.addParameter("ud.accountNonExpired", "_accountNonExpired", Boolean.TRUE);
		queryBuilder.addParameter("ud.accountNonLocked", "_accountNonLocked", Boolean.TRUE);
		queryBuilder.addParameter("ud.credentialsNonExpired", "_credentialsNonExpired", Boolean.TRUE);
		JpaUserDetails userDetails = persistenceServices.executeQuerySingle(queryBuilder);
		if (userDetails != null) {
			userDetails.setUpdateDate(new Date());
			persistenceServices.edit(userDetails);
			return true;
		}
		return false;
	}

	@Override
	public String registerNewLogin(String username, String password) {
		return registerNewLogin(username, password, false, null);
	}

	@Override
	public String registerNewLogin(String username, String password, boolean active, KeyValue<String, String> alias) {
		JpaUserDetails jpaUserDetails = new JpaUserDetails();
		jpaUserDetails.setUsername(username);
		jpaUserDetails.setPassword(encode(username, password));

		String activationKey = org.xaloon.core.api.util.KeyFactory.generateKey();
		jpaUserDetails.setActivationKey(activationKey);
		jpaUserDetails.setAccountNonLocked(true);
		jpaUserDetails.setAccountNonExpired(true);
		jpaUserDetails.setCredentialsNonExpired(true);
		jpaUserDetails.setEnabled(active);
		if (alias != null) {
			createAlias(alias, jpaUserDetails);
		}
		persistenceServices.create(jpaUserDetails);
		List<String> selections = new ArrayList<String>();
		selections.add(SecurityAuthorities.ROLE_REGISTERED_USER);
		roleService.assignAuthoritiesByName(jpaUserDetails, selections);
		return activationKey;
	}

	@Override
	public boolean activate(String activationKey, String userPassword) {
		boolean result = false;

		QueryBuilder queryBuilder = new QueryBuilder("select ud from " + JpaUserDetails.class.getSimpleName() + " ud");
		queryBuilder.addParameter("ud.activationKey", "_activationKey", activationKey);
		queryBuilder.addParameter("ud.enabled", "_enabled", Boolean.FALSE);
		JpaUserDetails userDetails = persistenceServices.executeQuerySingle(queryBuilder);
		if (userDetails != null && userDetails.getPassword().equals(encode(userDetails.getUsername(), userPassword))) {
			userDetails.setEnabled(true);
			persistenceServices.edit(userDetails);
			result = true;
		}
		return result;

	}

	@Override
	public boolean isUsernameRegistered(String username) {
		return loadUserDetails(username) != null;
	}

	@Override
	public String generateNewPassword(String username) {
		String newPassword = RandomStringUtils.randomAlphanumeric(10);
		if (changePassword(username, newPassword)) {
			return newPassword;
		}
		return null;

	}

	@Override
	public boolean isValidPassword(String username, String password) {
		QueryBuilder queryBuilder = new QueryBuilder("select ud from " + JpaUserDetails.class.getSimpleName() + " ud");
		queryBuilder.addParameter("ud.username", "_USERNAME", username);
		queryBuilder.addParameter("ud.password", "_PASSWORD", encode(username, password));
		JpaUserDetails userDetails = persistenceServices.executeQuerySingle(queryBuilder);
		return userDetails != null;
	}

	@Override
	public boolean changePassword(String username, String new_password) {
		JpaUserDetails userDetails = (JpaUserDetails)loadUserDetails(username);
		if (userDetails != null) {
			userDetails.setPassword(encode(username, new_password));
			persistenceServices.edit(userDetails);
			return true;
		}
		return false;
	}

	@Override
	public void addAlias(String username, KeyValue<String, String> alias) {
		JpaUserDetails userDetails = (JpaUserDetails)loadUserDetails(username);
		if (userDetails != null) {
			if (createAlias(alias, userDetails)) {
				persistenceServices.edit(userDetails);
			}
		}
	}

	@Override
	public void removeAlias(String username, KeyValue<String, String> alias) {
		if (alias == null || StringUtils.isEmpty(alias.getKey())) {
			return;
		}

		JpaUserDetails userDetails = (JpaUserDetails)loadUserDetails(username);
		if (userDetails != null) {
			for (KeyValue<String, String> keyValue : userDetails.getAliases()) {
				if (alias.getKey().equals(keyValue.getKey())) {
					userDetails.getAliases().remove(keyValue);
					persistenceServices.remove(keyValue);
					persistenceServices.edit(userDetails);
					return;
				}
			}
		}
	}

	@Override
	public UserDetails loadUserDetails(String username) {
		QueryBuilder queryBuilder = new QueryBuilder("select distinct ud from " + JpaUserDetails.class.getSimpleName() + " ud ");
		queryBuilder.addJoin(QueryBuilder.OUTER_JOIN, "ud.aliases a");
		queryBuilder.addParameter("ud.username", "_USERNAME", username);
		queryBuilder.addParameter("a.value", "_VALUE", username, Condition.OR, false, false);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	private String encode(String username, String password) {
		return PasswordEncoder.get().encode(username, password);
	}

	private boolean createAlias(KeyValue<String, String> alias, JpaUserDetails jpaUserDetails) {

		JpaUserAlias jpaAlias = getAlias(alias.getKey(), alias.getValue());
		if (jpaAlias == null) {
			jpaAlias = new JpaUserAlias();
			jpaAlias.setKey(alias.getKey());
			jpaAlias.setValue(alias.getValue());
			jpaAlias.setPath(jpaAlias.getValue());
			jpaAlias.setUserDetails(jpaUserDetails);
			jpaUserDetails.getAliases().add(jpaAlias);
			return true;
		}

		return false;
	}

	private JpaUserAlias getAlias(String loginType, String aliasValue) {
		QueryBuilder queryBuilder = new QueryBuilder("select ua from " + JpaUserAlias.class.getSimpleName() + " ua ");
		queryBuilder.addParameter("ua.key", "KEY", loginType);
		queryBuilder.addParameter("ua.value", "VALUE", aliasValue);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public List<Authority> getIndirectAuthoritiesForUsername(String username) {
		List<Authority> result = new ArrayList<Authority>();
		if (StringUtils.isEmpty(username)) {
			return result;
		}
		JpaUserDetails userDetails = (JpaUserDetails)loadUserDetails(username);
		if (userDetails == null) {
			return result;
		}
		Set<Authority> items = new HashSet<Authority>();
		addByAuthorityMemberInternal(userDetails.getAuthorities(), items);
		addByRoleMemberInternal(userDetails.getRoles(), items);
		addByGroupMemberInternal(userDetails.getGroups(), items);
		return new ArrayList<Authority>(items);
	}

	private void addByGroupMemberInternal(List<JpaGroup> groups, Set<Authority> items) {
		for (JpaGroup group : groups) {
			addByRoleMemberInternal(group.getRoles(), items);
		}
	}

	private void addByRoleMemberInternal(List<JpaRole> roles, Set<Authority> items) {
		for (JpaRole role : roles) {
			addByAuthorityMemberInternal(role.getAuthorities(), items);
		}
	}

	private void addByAuthorityMemberInternal(List<JpaAuthority> authorities, Set<Authority> items) {
		for (JpaAuthority authority : authorities) {
			items.add(authority);
		}
	}

	@Override
	public List<SecurityRole> getIndirectRolesForUsername(String username) {
		List<SecurityRole> result = new ArrayList<SecurityRole>();
		if (StringUtils.isEmpty(username)) {
			return result;
		}
		JpaUserDetails userDetails = (JpaUserDetails)loadUserDetails(username);
		if (userDetails == null) {
			return result;
		}
		Set<SecurityRole> items = new HashSet<SecurityRole>();
		addRolesByRoleMember(userDetails.getRoles(), items);
		addRolesByGroupMember(userDetails.getGroups(), items);
		return new ArrayList<SecurityRole>(items);
	}

	private void addRolesByGroupMember(List<JpaGroup> groups, Set<SecurityRole> items) {
		for (JpaGroup group : groups) {
			addRolesByRoleMember(group.getRoles(), items);
		}
	}

	private void addRolesByRoleMember(List<JpaRole> roles, Set<SecurityRole> items) {
		if (!roles.isEmpty()) {
			items.addAll(roles);
		}
	}

	@Override
	public UserDetails modifyCredentialsNonExpired(String username, Boolean newPropertyValue) {
		UserDetails user = loadUserDetails(username);
		user.setCredentialsNonExpired(newPropertyValue);
		return persistenceServices.edit(user);
	}

	@Override
	public UserDetails modifyAccountNonLocked(String username, Boolean newPropertyValue) {
		UserDetails user = loadUserDetails(username);
		user.setAccountNonLocked(newPropertyValue);
		return persistenceServices.edit(user);
	}

	@Override
	public UserDetails modifyAccountNonExpired(String username, Boolean newPropertyValue) {
		UserDetails user = loadUserDetails(username);
		user.setAccountNonExpired(newPropertyValue);
		return persistenceServices.edit(user);
	}

	@Override
	public UserDetails modifyAccountEnabled(String username, Boolean newPropertyValue) {
		UserDetails user = loadUserDetails(username);
		user.setEnabled(newPropertyValue);
		return persistenceServices.edit(user);
	}

	@Override
	public boolean deleteUser(String username) {
		UserDetails ud = loadUserDetails(username);
		persistenceServices.remove(ud);
		return true;
	}
}
