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

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.component.custom.ConfirmationAjaxLink;
import org.xaloon.wicket.plugin.user.admin.page.GroupDetailPage;
import org.xaloon.wicket.plugin.user.admin.page.RoleDetailPage;
import org.xaloon.wicket.plugin.user.admin.page.UsersPage;
import org.xaloon.wicket.plugin.user.admin.renderer.AuthorityChoiceRenderer;
import org.xaloon.wicket.plugin.user.admin.renderer.GroupChoiceRenderer;
import org.xaloon.wicket.plugin.user.admin.renderer.RoleChoiceRenderer;
import org.xaloon.wicket.plugin.user.panel.UserProfilePanel;

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

	@Inject
	private StringResourceLoader stringResourceLoader;

	private String username;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param parameters
	 */
	public UserSecurityPanel(String id, PageParameters parameters) {
		super(id, parameters);
		if (getPageRequestParameters().isEmpty() || getPageRequestParameters().get(UsersPage.PARAM_USER_ID).isEmpty()) {
			throw new RestartResponseException(UsersPage.class);
		}
		username = getPageRequestParameters().get(UsersPage.PARAM_USER_ID).toString();
		setOutputMarkupId(true);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		UserDetails userDetails = userFacade.loadUserDetails(username);

		User userInfo = userFacade.getUserByUsername(username);


		if (userDetails == null || userInfo == null) {
			throw new RestartResponseException(UsersPage.class);
		}

		// Add user details panel
		addUserDetails(userDetails, userInfo);

		// Add user authorities/permissions
		WebMarkupContainer authorityMarkupContainer = addAuthorities();

		// Add user roles
		WebMarkupContainer roleMarkupContainer = addRoles(authorityMarkupContainer);

		// Add user groups
		addGroups(roleMarkupContainer, authorityMarkupContainer);
	}


	private void addUserDetails(UserDetails userDetails, User userInfo) {
		add(new UserProfilePanel<User>("user-details", getPageRequestParameters()));
	}

	private void addGroups(final WebMarkupContainer roleMarkupContainer, final WebMarkupContainer authorityMarkupContainer) {
		final WebMarkupContainer groupContainer = new WebMarkupContainer("group-container");
		groupContainer.setOutputMarkupId(true);
		add(groupContainer);

		// Add role list
		groupContainer.add(new AuthorityManagementContainer<SecurityGroup>("group-admin") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onItemAddedToView(ListItem<SecurityGroup> item) {
				final SecurityGroup group = item.getModelObject();

				// Add link to role details
				PageParameters pageParams = new PageParameters();
				pageParams.add(Bookmarkable.PARAM_PATH, group.getPath());
				BookmarkablePageLink<Void> roleLink = new BookmarkablePageLink<Void>("groupDetails", GroupDetailPage.class, pageParams);
				item.add(roleLink);
				roleLink.add(new Label("name", new Model<String>(group.getName())));

				item.add(new ConfirmationAjaxLink<Void>("revoke") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						roleGroupService.revokeGroup(getUserDetails(), group);
						target.add(groupContainer);
						target.add(authorityMarkupContainer);
						target.add(roleMarkupContainer);
					}
				});
			}

			@Override
			protected void onAssign(List<SecurityGroup> selections) {
				roleGroupService.assignGroups(getUserDetails(), selections);
			}

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(groupContainer);
				components.add(authorityMarkupContainer);
				components.add(roleMarkupContainer);
			};

			@Override
			protected List<SecurityGroup> getAvailableItemsForSelection() {
				return roleGroupService.getGroupList(0, -1);
			}

			@Override
			protected List<SecurityGroup> getProvidedSelections() {
				return getUserDetails().getGroups();
			}
		}.setChoiceRenderer(new GroupChoiceRenderer()));
	}

	private WebMarkupContainer addRoles(final WebMarkupContainer authorityMarkupContainer) {
		final WebMarkupContainer roleContainer = new WebMarkupContainer("role-container");
		roleContainer.setOutputMarkupId(true);
		add(roleContainer);

		// Add role list
		roleContainer.add(new AuthorityManagementContainer<SecurityRole>("role-admin") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onItemAddedToView(ListItem<SecurityRole> item) {
				final SecurityRole role = item.getModelObject();

				// Add link to role details
				PageParameters pageParams = new PageParameters();
				pageParams.add(Bookmarkable.PARAM_PATH, role.getPath());
				BookmarkablePageLink<Void> roleLink = new BookmarkablePageLink<Void>("roleDetails", RoleDetailPage.class, pageParams);
				item.add(roleLink);
				roleLink.add(new Label("name", new Model<String>(role.getName())));

				item.add(new ConfirmationAjaxLink<Void>("revoke") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						roleGroupService.revokeRole(getUserDetails(), role);
						target.add(roleContainer);
						target.add(authorityMarkupContainer);
					}
				});
			}

			@Override
			protected void onAssign(List<SecurityRole> selections) {
				roleGroupService.assignRoles(getUserDetails(), selections);
			}

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(roleContainer);
				components.add(authorityMarkupContainer);
			};

			@Override
			protected List<SecurityRole> getAvailableItemsForSelection() {
				List<SecurityRole> userRoles = userFacade.getRoles(username);
				List<SecurityRole> allRoles = roleGroupService.getRoleList(0, -1);
				allRoles.removeAll(userRoles);
				return allRoles;
			}

			@Override
			protected List<SecurityRole> getProvidedSelections() {
				return getUserDetails().getRoles();
			}
		}.setChoiceRenderer(new RoleChoiceRenderer()));
		return roleContainer;
	}

	private WebMarkupContainer addAuthorities() {
		final WebMarkupContainer authorityContainer = new WebMarkupContainer("authority-container");
		authorityContainer.setOutputMarkupId(true);
		add(authorityContainer);

		// Add permission list
		authorityContainer.add(new AuthorityManagementContainer<Authority>("authority-admin") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onItemAddedToView(ListItem<Authority> item) {
				final Authority authority = item.getModelObject();
				item.add(new Label("name", new Model<String>(stringResourceLoader.getString(SecurityAuthorities.class, authority.getName()))));
				item.add(new ConfirmationAjaxLink<Void>("revoke") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						roleGroupService.revokeAuthority(getUserDetails(), authority);
						target.add(authorityContainer);
					}
				});
			}

			@Override
			protected void onAssign(List<Authority> selections) {
				roleGroupService.assignAuthorities(getUserDetails(), selections);
			}

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(authorityContainer);
			};

			@Override
			protected List<Authority> getAvailableItemsForSelection() {
				List<Authority> userAuthorities = userFacade.getAuthorities(username);
				List<Authority> allAuthorities = roleGroupService.getAuthorityList(0, -1);
				allAuthorities.removeAll(userAuthorities);
				return allAuthorities;
			}

			@Override
			protected List<Authority> getProvidedSelections() {
				return getUserDetails().getAuthorities();
			}
		}.setChoiceRenderer(new AuthorityChoiceRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Authority object) {
				return stringResourceLoader.getString(SecurityAuthorities.class, object.getName());
			}
		}));

		return authorityContainer;
	}

	private UserDetails getUserDetails() {
		return userFacade.loadUserDetails(username);
	}
}
