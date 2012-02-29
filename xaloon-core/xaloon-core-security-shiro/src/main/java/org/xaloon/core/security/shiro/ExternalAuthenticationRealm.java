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

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.external.AuthenticationAttribute;
import org.xaloon.core.api.security.model.UserDetails;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public class ExternalAuthenticationRealm extends AbstractRealm {

	/**
	 * Construct.
	 */
	public ExternalAuthenticationRealm() {
		setCacheManager(new MemoryConstrainedCacheManager());
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		Collection p = principals.fromRealm(getName());
		if (p == null || p.isEmpty()) {
			return null;
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Object o = p.iterator().next();
		String username = null;
		org.xaloon.core.api.security.model.UserDetails userDetails = null;
		if (o instanceof org.xaloon.core.api.security.external.AuthenticationToken) {
			org.xaloon.core.api.security.external.AuthenticationToken token = (org.xaloon.core.api.security.external.AuthenticationToken)o;
			username = token.getName();
		} else if (o instanceof UserDetails) {
			userDetails = (UserDetails)o;
		}
		if (userDetails == null) {
			userDetails = getLoginService().loadUserDetails(username);
		}
		if (userDetails != null) {
			addInternalUserRoles(info, userDetails);
		} else {
			addExternalUserRoles(info);
		}
		return info;
	}

	protected void addExternalUserRoles(SimpleAuthorizationInfo info) {
		info.addRole(SecurityAuthorities.AUTHENTICATED_USER);
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		ShiroAuthenticationToken shiroAuthenticationToken = (ShiroAuthenticationToken)token;
		org.xaloon.core.api.security.external.AuthenticationToken externalToken = shiroAuthenticationToken.getToken();
		if (!externalToken.isAuthenticated()) {
			return null;
		}
		AuthenticationInfo result = doGetAuthenticationInfoInternal(externalToken.getName());

		if (result != null) {
			return result;
		} else {
			return createExternalUserAuthentication(externalToken);
		}
	}

	protected AuthenticationInfo createExternalUserAuthentication(org.xaloon.core.api.security.external.AuthenticationToken externalToken) {
		org.xaloon.core.api.security.external.AuthenticationToken authenticationResultToken = new org.xaloon.core.api.security.external.AuthenticationToken(
			externalToken.getName(), new ArrayList<AuthenticationAttribute>());
		authenticationResultToken.setLoginType(externalToken.getLoginType());

		User user = getUserDao().newUser();
		user.setUsername(externalToken.getName());
		user.setExternal(true);
		getExternalParameterResolver().resolve(externalToken, user);

		authenticationResultToken.setDetails(user);
		return new SimpleAuthenticationInfo(authenticationResultToken, null, getName());
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token != null && ShiroAuthenticationToken.class.isAssignableFrom(token.getClass());
	}
}
