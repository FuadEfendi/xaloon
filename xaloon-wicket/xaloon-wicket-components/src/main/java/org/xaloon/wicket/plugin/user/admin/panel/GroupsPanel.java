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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.wicket.component.classifier.panel.CustomModalWindow;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.plugin.user.admin.page.GroupsPage;
import org.xaloon.wicket.util.Link;

/**
 * @author vytautas r.
 */
public class GroupsPanel extends AbstractAdministrationPanel {

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
		setOutputMarkupId(true);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		// Add paging navigation container with navigation toolbar
		final DecoratedPagingNavigatorContainer<SecurityGroup> dataContainer = new DecoratedPagingNavigatorContainer<SecurityGroup>("container",
			getCurrentRedirectLink());
		dataContainer.setOutputMarkupId(true);
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

		// Add the modal window to create new group
		final ModalWindow addNewGroupModalWindow = new CustomModalWindow("modal-new-group", "New group") {
			private static final long serialVersionUID = 1L;

			@Override
			protected Component getOnCloseComponent() {
				return GroupsPanel.this;
			}
		};
		addNewGroupModalWindow.setContent(new CreateNewEntityPanel<SecurityGroup>(addNewGroupModalWindow.getContentId(), new Model<SecurityGroup>(
			roleGroupService.newGroup())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNewEntitySubmit(AjaxRequestTarget target, SecurityGroup entity) {
				roleGroupService.save(entity);
				addNewGroupModalWindow.close(target);
			}
		});
		add(addNewGroupModalWindow);

		// add new group link
		add(new AjaxLink<Void>("add-new-group") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				addNewGroupModalWindow.show(target);
			}
		});
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
