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
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.RoleGroupService;
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
		setOutputMarkupId(true);
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
		addAuthorities(userDetails.getAuthorities(), userDetails);

		// Add user roles
		addRoles(userDetails);

		// Add user groups
		addGroups(userDetails);
	}


	private void addGroups(final UserDetails userDetails) {
		final List<SecurityGroup> providedSelections = userDetails.getGroups();
		List<SecurityGroup> availableItemsForSelection = roleGroupService.getGroupList(0, -1);

		// Add role list
		add(new AuthorityManagementContainer<SecurityGroup>("group-admin") {
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
						roleGroupService.revokeGroup(userDetails, group);
						target.add(UserSecurityPanel.this);
					}
				});
			}

			@Override
			protected void onAssign(List<SecurityGroup> selections) {
				roleGroupService.assignGroups(userDetails, selections);
			}

			@Override
			protected Component getOnCloseComponent() {
				return UserSecurityPanel.this;
			}
		}.setChoiceRenderer(new GroupChoiceRenderer())
			.setAvailableItemsForSelection(availableItemsForSelection)
			.setProvidedSelections(providedSelections));
	}

	private void addRoles(final UserDetails userDetails) {
		List<SecurityRole> availableItemsForSelection = roleGroupService.getRoleList(0, -1);
		List<SecurityRole> providedSelections = userDetails.getRoles();

		// Add role list
		add(new AuthorityManagementContainer<SecurityRole>("role-admin") {
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
						roleGroupService.revokeRole(userDetails, role);
						target.add(UserSecurityPanel.this);
					}
				});
			}

			@Override
			protected void onAssign(List<SecurityRole> selections) {
				roleGroupService.assignRoles(userDetails, selections);
			}

			@Override
			protected Component getOnCloseComponent() {
				return UserSecurityPanel.this;
			}
		}.setChoiceRenderer(new RoleChoiceRenderer())
			.setAvailableItemsForSelection(availableItemsForSelection)
			.setProvidedSelections(providedSelections));

	}

	private void addAuthorities(List<Authority> userAuthorities, final UserDetails userDetails) {
		final List<Authority> providedSelections = userDetails.getAuthorities();
		List<Authority> availableItemsForSelection = roleGroupService.getAuthorityList(0, -1);

		// Add permission list
		add(new AuthorityManagementContainer<Authority>("authority-admin") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onItemAddedToView(ListItem<Authority> item) {
				final Authority authority = item.getModelObject();
				item.add(new Label("name", new Model<String>(authority.getAuthority())));
				item.add(new ConfirmationAjaxLink<Void>("revoke") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						roleGroupService.revokeAuthority(userDetails, authority);
						target.add(UserSecurityPanel.this);
					}
				});
			}

			@Override
			protected void onAssign(List<Authority> selections) {
				roleGroupService.assignAuthorities(userDetails, selections);
			}

			@Override
			protected Component getOnCloseComponent() {
				return UserSecurityPanel.this;
			}
		}.setChoiceRenderer(new AuthorityChoiceRenderer())
			.setAvailableItemsForSelection(availableItemsForSelection)
			.setProvidedSelections(providedSelections));
	}
}
