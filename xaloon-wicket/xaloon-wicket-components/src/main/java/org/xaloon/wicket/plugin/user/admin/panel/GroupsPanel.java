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

import java.util.Iterator;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRoles;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;
import org.xaloon.wicket.plugin.user.admin.page.GroupsPage;
import org.xaloon.wicket.util.Link;

/**
 * @author vytautas r.
 */
@RolesAllowed({ SecurityRoles.SYSTEM_ADMINISTRATOR })
public class GroupsPanel extends AbstractPluginPanel<SystemPluginBean, SystemPlugin> {

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
	public GroupsPanel(String id, PageParameters parameters) {
		super(id);
	}

	@Override
	protected void onInitialize(SystemPlugin plugin, SystemPluginBean pluginBean) {
		// Add paging navigation container with navigation toolbar
		final DecoratedPagingNavigatorContainer<SecurityGroup> dataContainer = new DecoratedPagingNavigatorContainer<SecurityGroup>("container",
			getCurrentRedirectLink());
		addOrReplace(dataContainer);

		// Add blog list data view
		final DataView<SecurityGroup> securityGroupDataView = new DataView<SecurityGroup>("security-groups", new JpaSecurityGroupDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<SecurityGroup> item) {
				SecurityGroup group = item.getModelObject();
				item.add(new Label("name", new Model<String>(group.getName())));
			}

		};
		dataContainer.addAbstractPageableView(securityGroupDataView);
	}

	protected Link getCurrentRedirectLink() {
		return new Link(GroupsPage.class, getPageRequestParameters());
	}

	class JpaSecurityGroupDataProvider implements IDataProvider<SecurityGroup> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
		}

		@Override
		public Iterator<? extends SecurityGroup> iterator(int first, int count) {
			return roleGroupService.getGroupList(first, count).iterator();
		}

		@Override
		public int size() {
			return roleGroupService.getGroupCount();
		}

		@Override
		public IModel<SecurityGroup> model(SecurityGroup object) {
			return new Model<SecurityGroup>(object);
		}
	}
}
