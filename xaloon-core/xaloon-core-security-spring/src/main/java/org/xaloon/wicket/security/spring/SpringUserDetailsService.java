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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.LoginService;

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
		org.xaloon.core.api.security.UserDetails userDetails = loginService.loadUserDetails(arg0);
		if (userDetails != null) {
			return createAdaptor(userDetails);
		}
		throw new UsernameNotFoundException("User not found.");
	}

	private UserDetails createAdaptor(org.xaloon.core.api.security.UserDetails userDetails) {
		DefaultUserDetails details = new DefaultUserDetails();
		details.setAccountNonExpired(userDetails.isAccountNonExpired());
		details.setAccountNonLocked(userDetails.isAccountNonLocked());
		details.setCredentialsNonExpired(userDetails.isCredentialsNonExpired());
		details.setEnabled(userDetails.isEnabled());
		details.setPassword(userDetails.getPassword());
		details.setUsername(userDetails.getUsername());
		
		List<String> authorities = loginService.getAuthoritiesByUsername(userDetails.getUsername());
		if (!authorities.isEmpty()) {
			createAdaptorForAuthorities(details, authorities);
		}
		if (!userDetails.getAliases().isEmpty()) {
			details.getAliases().addAll(userDetails.getAliases());
		}
		return details;
	}

	private void createAdaptorForAuthorities(DefaultUserDetails details, List<String> authorities) {
		for (String authority : authorities) {
			details.getAuthorities().add(new SimpleGrantedAuthority(authority));
		}
	}
}
