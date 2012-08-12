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
package org.xaloon.wicket.security.spring;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationAttribute;
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.wicket.security.spring.external.SpringAuthenticationToken;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class SpringSecurityFacade implements SecurityFacade {
	private static final String ANONYMOUS_USER = "anonymousUser";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringSecurityFacade.class);

	@Inject
	@Named("userDao")
	private UserDao userDao;

	@Inject
	private transient AuthenticationManager authenticationManager;

	public AuthenticationToken authenticate(String username, String password) {
		UsernamePasswordAuthenticationToken authenticationRequestToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticateInternal(authenticationRequestToken);
	}

	private AuthenticationToken authenticateInternal(AbstractAuthenticationToken authenticationRequestToken) {
		boolean authenticated = false;
		String name = authenticationRequestToken.getName();
		String errorMessage = null;
		try {
			Authentication authentication = authenticationManager.authenticate(authenticationRequestToken);
			authenticated = authentication.isAuthenticated();
			if (authenticated && authentication.getDetails() == null) {
				// Try to load user details. Copy information into new token
				UsernamePasswordAuthenticationToken authenticationWithDetails = new UsernamePasswordAuthenticationToken(
					authentication.getPrincipal(), authentication.getCredentials(), authentication.getAuthorities());
				authenticationWithDetails.setDetails(userDao.getUserByUsername(authentication.getName()));
				authentication = authenticationWithDetails;
			}
			SecurityContextHolder.getContext().setAuthentication(authentication);
			name = authentication.getName();
		} catch (AuthenticationException e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("User " + name + " failed to login. Reason: ", e);
			}
			authenticated = false;
			errorMessage = e.getMessage();
		}
		if (authenticated) {
			return new AuthenticationToken(name, new ArrayList<AuthenticationAttribute>());
		}
		return new AuthenticationToken(name, errorMessage);
	}

	public boolean isAdministrator() {
		return hasAny(SecurityAuthorities.SYSTEM_ADMINISTRATOR);
	}

	public String getCurrentUsername() {
		DefaultUserDetails userDetails = getUserDetails();
		return (userDetails != null) ? userDetails.getUsername() : null;
	}

	public String getCurrentUserDisplayName() {
		if (getCurrentUser() != null) {
			return getCurrentUser().getDisplayName();
		}
		return null;
	}

	public String getCurrentUserEmail() {
		if (getCurrentUser() != null) {
			return getCurrentUser().getEmail();
		}
		return null;
	}

	@Override
	public boolean isOwnerOfObject(String username) {
		return username.equalsIgnoreCase(getCurrentUsername()) || isAlias(username);
	}

	private boolean isAlias(String username) {
		DefaultUserDetails userDetails = getUserDetails();
		if (userDetails != null) {
			return userDetails.getAliases().contains(username);
		}
		return false;
	}

	/**
	 * 
	 * @see org.xaloon.core.api.security.SecurityFacade#getAliases()
	 */
	public List<? extends KeyValue<String, String>> getAliases() {
		DefaultUserDetails userDetails = getUserDetails();
		if (userDetails != null) {
			return userDetails.getAliases();
		}
		return new ArrayList<KeyValue<String, String>>();
	}

	private DefaultUserDetails getUserDetails() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		Object o = authentication.getPrincipal();
		if (o != null && o instanceof DefaultUserDetails) {
			return (DefaultUserDetails)o;
		}
		return null;
	}

	@Override
	public boolean hasAny(String... roles) {
		boolean result = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return result;
		}
		for (String role : roles) {
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				if (authority.getAuthority().equalsIgnoreCase(role)) {
					result = true;
					break;
				}
			}
		}
		return result;
	}

	@Override
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (authentication != null && authentication.isAuthenticated() && !ANONYMOUS_USER.equalsIgnoreCase(authentication.getPrincipal()
			.toString()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication.getDetails() == null) {
			return null;
		}
		if (authentication.getDetails() instanceof User) {
			return (T)authentication.getDetails();
		}
		return null;
	}

	@Override
	public AuthenticationToken authenticate(AuthenticationToken token) {
		SpringAuthenticationToken authenticationRequestToken = new SpringAuthenticationToken(token);
		return authenticateInternal(authenticationRequestToken);
	}

	@Override
	public boolean isRegistered() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof AuthenticationToken) {
			return false;
		}
		return true;
	}

	@Override
	public KeyValue<String, String> getAlias() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Object principal = authentication.getPrincipal();
		if (principal instanceof AuthenticationToken) {
			AuthenticationToken token = (AuthenticationToken)principal;
			String loginType = token.getLoginType();
			return new DefaultKeyValue<String, String>(loginType, token.getName());
		}
		return null;
	}

	@Override
	public void logout() {
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@Override
	public void removeAlias(KeyValue<String, String> alias) {
		getAliases().remove(alias);
	}
}
