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

import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.AuthorityFacade;
import org.xaloon.core.api.security.AuthorityService;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityRole;

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

	@Inject
	private RoleGroupService roleGroupService;

	@Inject
	private AuthorityService authorityService;

	@Override
	public void registerRoles(Plugin plugin) {
		for (SecurityRole role : plugin.getSupportedRoles()) {
			SecurityRole securityRole = roleGroupService.findOrCreateRole(role.getName());
			registerAuthoritiesForRole(securityRole, role.getAuthorities());
		}
	}

	private void registerAuthoritiesForRole(SecurityRole securityRole, List<Authority> authorities) {
		List<Authority> authoritiesToAssign = new ArrayList<Authority>();
		for (Authority authority : authorities) {
			Authority persistedAuthority = authorityService.findOrCreateAuthority(authority.getName());
			authoritiesToAssign.add(persistedAuthority);
		}
		if (!authoritiesToAssign.isEmpty()) {
			roleGroupService.assignAuthorities(securityRole, authoritiesToAssign);
		}
	}
}
