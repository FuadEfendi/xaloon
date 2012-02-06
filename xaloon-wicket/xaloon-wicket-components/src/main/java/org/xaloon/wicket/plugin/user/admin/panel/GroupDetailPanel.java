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
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.model.SecurityGroup;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.wicket.component.custom.ConfirmationAjaxLink;
import org.xaloon.wicket.plugin.user.admin.page.GroupsPage;
import org.xaloon.wicket.plugin.user.admin.page.RoleDetailPage;
import org.xaloon.wicket.plugin.user.admin.renderer.RoleChoiceRenderer;

/**
 * @author vytautas r.
 */
public class GroupDetailPanel extends AbstractAdministrationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RoleGroupService roleGroupService;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param parameters
	 */
	public GroupDetailPanel(String id, PageParameters parameters) {
		super(id, parameters);
		setOutputMarkupId(true);
		if (getPageRequestParameters().get(Bookmarkable.PARAM_PATH).isEmpty()) {
			throw new RestartResponseException(GroupsPage.class);
		}
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		String path = getPageRequestParameters().get(Bookmarkable.PARAM_PATH).toString();

		final SecurityGroup group = roleGroupService.getGroupByPath(path);

		// Add name
		add(new Label("name", new Model<String>(group.getName())));

		final List<SecurityRole> availableItemsForSelection = roleGroupService.getRoleList(0, -1);

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
						roleGroupService.revokeRoleFromGroup(group, role);
						target.add(GroupDetailPanel.this);
					}
				});
			}

			@Override
			protected void onAssign(List<SecurityRole> selections) {
				roleGroupService.assignRolesToGroup(group, selections);
			}

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(GroupDetailPanel.this);
			};

			@Override
			protected List<SecurityRole> getAvailableItemsForSelection() {
				return availableItemsForSelection;
			}

			@Override
			protected List<SecurityRole> getProvidedSelections() {
				return group.getRoles();
			}
		}.setChoiceRenderer(new RoleChoiceRenderer()));
	}
}
