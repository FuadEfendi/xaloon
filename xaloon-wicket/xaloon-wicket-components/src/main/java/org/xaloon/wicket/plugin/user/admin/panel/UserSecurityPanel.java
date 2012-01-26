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
package org.xaloon.wicket.plugin.user.admin.panel;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.user.admin.page.UsersPage;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class UserSecurityPanel extends AbstractAdministrationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	@Inject
	private RoleGroupService roleGroupService;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param parameters
	 */
	public UserSecurityPanel(String id, PageParameters parameters) {
		super(id, parameters);
		if (getPageRequestParameters().isEmpty() || getPageRequestParameters().get(UsersPage.PARAM_USER_ID).isEmpty()) {
			setResponsePage(UsersPage.class);
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		String username = getPageRequestParameters().get(UsersPage.PARAM_USER_ID).toString();

		UserDetails userDetails = userFacade.loadUserDetails(username);

		User userInfo = userFacade.getUserByUsername(username);


		if (userDetails == null || userInfo == null) {
			String requestUrl = UrlUtils.toAbsolutePath(UsersPage.class, null);
			throw new RedirectToUrlException(requestUrl);
		}

		// Add user authorities/permissions
		List<String> userAuthorities = userFacade.getAuthoritiesByUsername(username);
		addAuthorities(userAuthorities);

		// Add user roles
		List<SecurityRole> userRoles = roleGroupService.getRolesByUsername(username);
		addRoles(userRoles);

		// Add user groups
		List<SecurityGroup> userGroups = roleGroupService.getGroupsByUsername(username);
		addGroups(userGroups);
	}

	private void addGroups(List<SecurityGroup> userGroups) {
		add(new ListView<SecurityGroup>("user-groups", userGroups) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SecurityGroup> item) {
				SecurityGroup group = item.getModelObject();
				item.add(new Label("name", new Model<String>(group.getName())));
			}
		});
	}

	private void addRoles(List<SecurityRole> userRoles) {
		add(new ListView<SecurityRole>("user-roles", userRoles) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SecurityRole> item) {
				SecurityRole role = item.getModelObject();
				item.add(new Label("name", new Model<String>(role.getName())));
			}
		});
	}

	private void addAuthorities(List<String> userAuthorities) {
		add(new ListView<String>("user-authorities", userAuthorities) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<String> item) {
				item.add(new Label("name", new Model<String>(item.getModelObject())));
			}
		});
	}
}
