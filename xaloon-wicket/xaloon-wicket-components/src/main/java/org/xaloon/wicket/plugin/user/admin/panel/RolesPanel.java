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

import javax.inject.Inject;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;
import org.xaloon.wicket.plugin.user.admin.page.RolesPage;
import org.xaloon.wicket.util.Link;

/**
 * @author vytautas r.
 */
public class RolesPanel extends AbstractAdministrationPanel {

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
	public RolesPanel(String id, PageParameters parameters) {
		super(id);
	}

	@Override
	protected void onInitialize(SystemPlugin plugin, SystemPluginBean pluginBean) {
		// Add paging navigation container with navigation toolbar
		final DecoratedPagingNavigatorContainer<SecurityRole> dataContainer = new DecoratedPagingNavigatorContainer<SecurityRole>("container",
			getCurrentRedirectLink());
		addOrReplace(dataContainer);

		// Add blog list data view
		final DataView<SecurityRole> securityGroupDataView = new DataView<SecurityRole>("security-roles", new JpaSecurityRoleDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<SecurityRole> item) {
				SecurityRole role = item.getModelObject();
				item.add(new Label("name", new Model<String>(role.getName())));
			}

		};
		dataContainer.addAbstractPageableView(securityGroupDataView);
	}

	protected Link getCurrentRedirectLink() {
		return new Link(RolesPage.class, getPageRequestParameters());
	}

	class JpaSecurityRoleDataProvider implements IDataProvider<SecurityRole> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
		}

		@Override
		public Iterator<SecurityRole> iterator(int first, int count) {
			return roleGroupService.getRoleList(first, count).iterator();
		}

		@Override
		public int size() {
			return roleGroupService.getRoleCount();
		}

		@Override
		public IModel<SecurityRole> model(SecurityRole object) {
			return new Model<SecurityRole>(object);
		}
	}

}
