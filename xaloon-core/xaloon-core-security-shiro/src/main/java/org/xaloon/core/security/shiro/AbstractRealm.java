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
package org.xaloon.core.security.shiro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.security.LoginService;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.ExternalParameterResolver;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public abstract class AbstractRealm extends AuthorizingRealm {

	private LoginService loginService;

	private UserDao userDao;

	private ExternalParameterResolver externalParameterResolver;

	/**
	 * Gets loginService.
	 * 
	 * @return loginService
	 */
	public LoginService getLoginService() {
		if (loginService == null) {
			loginService = ServiceLocator.get().getInstance(LoginService.class, "loginService");
		}
		return loginService;
	}

	/**
	 * Gets userDao.
	 * 
	 * @return userDao
	 */
	public UserDao getUserDao() {
		if (userDao == null) {
			userDao = ServiceLocator.get().getInstance(UserDao.class, "userDao");
		}
		return userDao;
	}

	/**
	 * Gets externalParameterResolver.
	 * 
	 * @return externalParameterResolver
	 */
	public ExternalParameterResolver getExternalParameterResolver() {
		if (externalParameterResolver == null) {
			externalParameterResolver = ServiceLocator.get().getInstance(ExternalParameterResolver.class);
		}
		return externalParameterResolver;
	}

	protected AuthenticationInfo doGetAuthenticationInfoInternal(String username) {
		org.xaloon.core.api.security.model.UserDetails userDetailPrincipal = getLoginService().loadUserDetails(username);
		if (userDetailPrincipal == null) {
			throw new CredentialsException(SecurityFacade.INVALID_USERNAME_PASSWORD);
		}
		if (!userDetailPrincipal.isEnabled()) {
			throw new DisabledAccountException(SecurityFacade.ACCOUNT_DISABLED);
		}
		if (!userDetailPrincipal.isAccountNonExpired()) {
			throw new ExpiredCredentialsException(SecurityFacade.ACCOUNT_EXPIRED);
		}
		if (!userDetailPrincipal.isAccountNonLocked()) {
			throw new LockedAccountException(SecurityFacade.ACCOUNT_LOCKED);
		}
		if (!userDetailPrincipal.isCredentialsNonExpired()) {
			throw new ExpiredCredentialsException(SecurityFacade.CREDENTIALS_EXPIRED);
		}
		
		//Everything should be fine now.
		User userPrincipal = getUserDao().getUserByUsername(username);
		Collection<Object> principalCollection = new ArrayList<Object>();
		principalCollection.add(userDetailPrincipal);
		principalCollection.add(userPrincipal);
		return new SimpleAuthenticationInfo(new SimplePrincipalCollection(principalCollection, getName()), userDetailPrincipal.getPassword(),
				getName());

	}

	protected void addInternalUserRoles(SimpleAuthorizationInfo info, org.xaloon.core.api.security.model.UserDetails userDetails) {
		List<Authority> authorities = loginService.getIndirectAuthoritiesForUsername(userDetails.getUsername());
		for (Authority authority : authorities) {
			info.addRole(authority.getName());// TODO check
		}
	}
}
