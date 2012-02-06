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
package org.xaloon.wicket.plugin.system;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.xaloon.core.api.plugin.AbstractPlugin;
import org.xaloon.core.api.plugin.PluginType;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.impl.plugin.category.DefaultPluginCategories;
import org.xaloon.core.impl.security.DefaultSecurityEntity;
import org.xaloon.core.impl.security.DefaultSecurityRole;
import org.xaloon.wicket.plugin.system.panel.SystemAdministrationPanel;

/**
 * @author vytautas r.
 */
@Named
public class SystemPlugin extends AbstractPlugin<SystemPluginBean> {
	private static final long serialVersionUID = 1L;

	/**
	 * Default security authority for authenticated users
	 */
	public static final Authority AUTHENTICATED_USER = new DefaultSecurityEntity(SecurityAuthorities.AUTHENTICATED_USER);

	private static final Authority SYSTEM_ADMINISTRATOR = new DefaultSecurityEntity(SecurityAuthorities.SYSTEM_ADMINISTRATOR);

	private static final Authority[] CLASSIFIER_ADMINISTRATOR = new Authority[] { new DefaultSecurityEntity(SecurityAuthorities.CLASSIFIER_EDIT),
			new DefaultSecurityEntity(SecurityAuthorities.CLASSIFIER_DELETE) };

	/**
	 * Plugin type is set to visible by default.
	 */
	public SystemPlugin() {
		setType(PluginType.VISIBLE);
		setCategory(DefaultPluginCategories.ADMINISTRATION);
	}

	@Override
	public Class<?> getAdministratorFormClass() {
		return SystemAdministrationPanel.class;
	}

	@Override
	public List<SecurityRole> getSupportedRoles() {
		SecurityRole authenticatedUser = new DefaultSecurityRole(SecurityAuthorities.ROLE_REGISTERED_USER);
		authenticatedUser.getAuthorities().add(AUTHENTICATED_USER);

		SecurityRole systemAdministrator = new DefaultSecurityRole(SecurityAuthorities.ROLE_SYSTEM_ADMINISTRATOR);
		systemAdministrator.getAuthorities().add(SYSTEM_ADMINISTRATOR);

		SecurityRole classifierAdministrator = new DefaultSecurityRole(SecurityAuthorities.ROLE_CLASSIFIER_ADMINISTRATOR);
		classifierAdministrator.getAuthorities().addAll(Arrays.asList(CLASSIFIER_ADMINISTRATOR));

		return Arrays.asList(systemAdministrator, classifierAdministrator, authenticatedUser);
	}

	@Override
	public List<Authority> getSupportedAuthorities() {
		List<Authority> result = new ArrayList<Authority>();
		result.add(AUTHENTICATED_USER);
		result.add(SYSTEM_ADMINISTRATOR);
		result.addAll(Arrays.asList(CLASSIFIER_ADMINISTRATOR));
		return result;
	}
}
