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
package org.xaloon.core.impl.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.security.AuthorityFacade;
import org.xaloon.core.api.security.AuthorityService;
import org.xaloon.core.api.security.RoleService;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;

/**
 * Default implementation for authority facade
 * 
 * @author vytautas r.
 * @version 1.1, 02/06/12
 * @since 1.5
 */
@Named("authorityFacade")
public class DefaultAuthorityFacade implements AuthorityFacade {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAuthorityFacade.class);

	@Inject
	private RoleService roleService;

	@Inject
	private AuthorityService authorityService;

	@Override
	public void registerRoles(Plugin plugin) {
		for (SecurityRole role : plugin.getSupportedRoles()) {
			SecurityRole securityRole = roleService.findOrCreateAuthority(role.getName());
			registerAuthoritiesForRole(securityRole, role.getAuthorities());
		}
	}

	private void registerAuthoritiesForRole(SecurityRole securityRole, List<Authority> authorities) {
		List<Authority> authoritiesToAssign = new ArrayList<Authority>();
		for (Authority authority : authorities) {
			Authority persistedAuthority = authorityService.findOrCreateAuthority(authority.getName());
			if (!securityRole.getAuthorities().contains(persistedAuthority)) {
				authoritiesToAssign.add(persistedAuthority);
			}
		}
		if (!authoritiesToAssign.isEmpty()) {
			LOGGER.debug(String.format("Registering new security role '%s' and it's authorities: %s", securityRole.getName(),
				authoritiesToAssign.toString()));
			roleService.assignChildren(securityRole, authoritiesToAssign);
		}
	}
}
