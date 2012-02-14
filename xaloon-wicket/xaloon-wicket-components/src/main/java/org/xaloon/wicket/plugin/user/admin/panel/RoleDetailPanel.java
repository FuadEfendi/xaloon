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

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.AuthorityService;
import org.xaloon.core.api.security.RoleService;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.wicket.component.custom.ConfirmationAjaxLink;
import org.xaloon.wicket.plugin.user.admin.page.RolesPage;
import org.xaloon.wicket.plugin.user.admin.renderer.AuthorityChoiceRenderer;

/**
 * @author vytautas r.
 */
public class RoleDetailPanel extends AbstractAdministrationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RoleService roleService;

	@Inject
	private AuthorityService authorityService;

	@Inject
	private StringResourceLoader stringResourceLoader;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param parameters
	 */
	public RoleDetailPanel(String id, PageParameters parameters) {
		super(id, parameters);
		setOutputMarkupId(true);
		if (parameters.get(Bookmarkable.PARAM_PATH).isEmpty()) {
			throw new RestartResponseException(RolesPage.class);
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		String path = getPageRequestParameters().get(Bookmarkable.PARAM_PATH).toString();

		final SecurityRole role = roleService.getAuthorityByPath(path);

		if (role == null) {
			throw new RestartResponseException(RolesPage.class);
		}
		// Add name
		add(new Label("name", new Model<String>(role.getName())));

		final List<Authority> availableItemsForSelection = authorityService.getAuthorities(0, -1);

		// Add permission list
		add(new AuthorityManagementContainer<Authority>("authority-admin") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onItemAddedToView(ListItem<Authority> item) {
				final Authority authority = item.getModelObject();
				item.add(new Label("name", new Model<String>(stringResourceLoader.getString(SecurityAuthorities.class, authority.getName()))));
				item.add(new ConfirmationAjaxLink<Void>("revoke") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						roleService.revokeChild(role, authority);
						target.add(RoleDetailPanel.this);
					}
				});
			}

			@Override
			protected void onAssign(List<Authority> selections) {
				roleService.assignChildren(role, selections);
			}

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(RoleDetailPanel.this);
			};

			@Override
			protected List<Authority> getAvailableItemsForSelection() {
				return availableItemsForSelection;
			}

			@Override
			protected List<Authority> getProvidedSelections() {
				return role.getAuthorities();
			}
		}.setChoiceRenderer(new AuthorityChoiceRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Authority object) {
				return stringResourceLoader.getString(SecurityAuthorities.class, object.getName());
			}
		}));
	}
}
