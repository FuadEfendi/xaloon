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
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.component.classifier.panel.CustomModalWindow;
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
		// Add the modal window to assign a group
		final ModalWindow addGroupModalWindow = new CustomModalWindow("modal-assign-group", "Assign group") {
			private static final long serialVersionUID = 1L;

			@Override
			protected Component getOnCloseComponent() {
				return UserSecurityPanel.this;
			}
		};

		Panel panel = new EmptyPanel(addGroupModalWindow.getContentId());
		Form form = new Form("form");
		form.add(new GroupsPanel("inner-panel", getPageRequestParameters()));
		form.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});

		addGroupModalWindow.setContent(panel);
		add(addGroupModalWindow);
		// Add assign group link
		add(new AjaxLink<Void>("assign-group") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				addGroupModalWindow.show(target);
			}
		});
		add(new ListView<SecurityGroup>("user-groups", userGroups) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<SecurityGroup> item) {
				SecurityGroup group = item.getModelObject();

				// Add name
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
