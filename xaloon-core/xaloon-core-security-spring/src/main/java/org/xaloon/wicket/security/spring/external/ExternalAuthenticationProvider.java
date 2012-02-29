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
package org.xaloon.wicket.security.spring.external;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.core.api.security.external.ExternalParameterResolver;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
@Named("externalAuthenticationProvider")
public class ExternalAuthenticationProvider implements AuthenticationProvider, Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("springUserDetailsService")
	private transient UserDetailsService userDetailsService;

	@Inject
	@Named("userDao")
	private UserDao userDao;

	@Inject
	private ExternalParameterResolver externalParameterResolver;

	@Override
	public boolean supports(Class<? extends Object> authentication) {
		return (SpringAuthenticationToken.class.isAssignableFrom(authentication));
	}


	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (StringUtils.isEmpty(authentication.getName())) {
			throw new IllegalArgumentException("Authentication username is not provided!");
		}
		UserDetails loadedUser = null;
		AuthenticationToken initialToken = null;
		if (authentication instanceof SpringAuthenticationToken) {
			initialToken = ((SpringAuthenticationToken)authentication).getToken();
		}
		try {
			loadedUser = userDetailsService.loadUserByUsername(authentication.getName());
		} catch (DataAccessException repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
		} catch (UsernameNotFoundException e) {}

		if (loadedUser == null) {
			return createExternalAuthenticationToken(authentication, initialToken);
		} else {
			return createDefaultAuthenticationToken(authentication, loadedUser);
		}
	}


	private Authentication createDefaultAuthenticationToken(Authentication authentication, UserDetails loadedUser) {
		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, authentication.getCredentials(),
			loadedUser.getAuthorities());
		result.setDetails(userDao.getUserByUsername(loadedUser.getUsername()));
		return result;
	}


	private Authentication createExternalAuthenticationToken(Authentication authentication, AuthenticationToken initialToken) {
		User user = userDao.newUser();
		user.setUsername(authentication.getName());
		user.setExternal(true);
		externalParameterResolver.resolve(initialToken, user);

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(SecurityAuthorities.AUTHENTICATED_USER));

		UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
			authentication.getCredentials(), authorities);
		result.setDetails(user);
		return result;
	}
}
