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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.xaloon.core.api.security.LoginService;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.model.Authority;

/**
 * @author vytautas r.
 */
@Named("springUserDetailsService")
public class SpringUserDetailsService implements UserDetailsService {

	@Inject
	@Named("loginService")
	private LoginService loginService;

	@Override
	public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException, DataAccessException {
		org.xaloon.core.api.security.model.UserDetails userDetails = loginService.loadUserDetails(arg0);
		if (userDetails != null) {
			return createAdaptor(userDetails);
		}
		throw new UsernameNotFoundException("User not found.");
	}

	private UserDetails createAdaptor(org.xaloon.core.api.security.model.UserDetails userDetails) {
		if (!userDetails.isEnabled()) {
			throw new DisabledException(SecurityFacade.ACCOUNT_DISABLED);
		}
		if (!userDetails.isAccountNonExpired()) {
			throw new AccountExpiredException(SecurityFacade.ACCOUNT_EXPIRED);
		}
		if (!userDetails.isAccountNonLocked()) {
			throw new LockedException(SecurityFacade.ACCOUNT_LOCKED);
		}
		if (!userDetails.isCredentialsNonExpired()) {
			throw new CredentialsExpiredException(SecurityFacade.CREDENTIALS_EXPIRED);
		}
		
		DefaultUserDetails details = new DefaultUserDetails();
		details.setAccountNonExpired(userDetails.isAccountNonExpired());
		details.setAccountNonLocked(userDetails.isAccountNonLocked());
		details.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
		details.setEnabled(userDetails.isEnabled());
		details.setPassword(userDetails.getPassword());
		details.setUsername(userDetails.getUsername());
		
		List<Authority> authorities = loginService.getIndirectAuthoritiesForUsername(userDetails.getUsername());
		if (!authorities.isEmpty()) {
			createAdaptorForAuthorities(details, authorities);
		}
		if (!userDetails.getAliases().isEmpty()) {
			details.getAliases().addAll(userDetails.getAliases());
		}
		return details;
	}

	private void createAdaptorForAuthorities(DefaultUserDetails details, List<Authority> authorities) {
		for (Authority authority : authorities) {
			details.getAuthorities().add(new SimpleGrantedAuthority(authority.getName()));
		}
	}
}
