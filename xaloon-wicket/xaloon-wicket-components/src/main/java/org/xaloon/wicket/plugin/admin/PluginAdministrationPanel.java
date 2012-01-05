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
package org.xaloon.wicket.plugin.admin;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.value.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.EmptyPlugin;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.impl.plugin.category.CategoryConstants;
import org.xaloon.core.impl.plugin.category.CategoryGroupPlugin;
import org.xaloon.core.impl.plugin.category.CategoryMenuPlugin;
import org.xaloon.core.impl.plugin.category.DefaultPluginCategories;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.component.classifier.panel.CustomModalWindow;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.menu.panel.FlatDynamicMenuPanel;


/**
 * Plugin administration panel shows configuration panel for available plugins
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */

public class PluginAdministrationPanel extends AbstractPluginPanel<AbstractPluginBean, EmptyPlugin> {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(PluginAdministrationPanel.class);

	private static final String PLUGIN_PROPERTY_CONTAINER = "plugin_enabled_container";

	private PageParameters pageParameters;

	private CategoryGroupPlugin categoryGroupPlugin;

	private CategoryMenuPlugin categoryMenuPlugin;


	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public PluginAdministrationPanel(String id, PageParameters pageParameters) {
		super(id);
		this.pageParameters = pageParameters;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		init();
	}

	private void init() {
		List<List<GenericTreeNode<MenuItem>>> menuItems = new ArrayList<List<GenericTreeNode<MenuItem>>>();
		menuItems.add(getCategoryMenuPlugin().getTree().getChildren());
		FlatDynamicMenuPanel hierarchyMenu = new FlatDynamicMenuPanel("hierarchy-menu", menuItems);
		hierarchyMenu.setMenuItemPageClass(PluginAdministrationPage.class);
		add(hierarchyMenu);


		String filter = pageParameters.get(CategoryConstants.PAGE_NAMED_PARAMETER_PARENT_CATEGORY).toString();
		List<GenericTreeNode<Plugin>> pluginCollection;
		if (StringUtils.isEmpty(filter)) {
			pluginCollection = getCategoryGroupPlugin().getTree().getChildren();
		} else {
			pluginCollection = getCategoryGroupPlugin().getTreeNodesByContext().get("/" + filter).getChildren();
		}
		add(new ListView<GenericTreeNode<Plugin>>("plugin-view", pluginCollection) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(final ListItem<GenericTreeNode<Plugin>> item) {
				item.setOutputMarkupId(true);
				final ValueMap properties = new ValueMap();

				final Plugin plugin = item.getModelObject().getData();
				if (plugin == null || plugin.getAdministratorFormClass() == null) {
					item.setVisible(false);
					return;
				}
				boolean pluginEnabled = getPluginRegistry().isEnabled(plugin);
				properties.put(PLUGIN_PROPERTY_CONTAINER, pluginEnabled);

				item.add(new Label("plugin-name", new Model<String>(plugin.getName())));
				item.add(new Label("plugin-version", new Model<String>(plugin.getVersion())));
				item.add(new Label("plugin-description", new Model<String>(plugin.getDescription())).setVisible(!StringUtils.isEmpty(plugin.getDescription())));
				final ModalWindow pluginConfigurationModalWindow = new CustomModalWindow("plugin-config", "title") {
					private static final long serialVersionUID = 1L;

					@Override
					protected Component getOnCloseComponent() {
						return null;
					}
				};
				item.add(pluginConfigurationModalWindow);

				pluginConfigurationModalWindow.setContent(new PluginConfigurationPanel(pluginConfigurationModalWindow, new Model<Plugin>(plugin)));

				// add link to open configuration window
				final AjaxLink<Void> openConfigWindow = new AjaxLink<Void>("link-plugin-config") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						pluginConfigurationModalWindow.show(target);
					}
				};
				item.add(openConfigWindow);
				openConfigWindow.setVisible(pluginEnabled);

				// } catch (CreateClassInstanceException e) {
				// setVisible(false);// TODO double check
				// if (logger.isWarnEnabled()) {
				// logger.warn("Could not create instance for administration class: " + plugin.getAdministratorFormClass());
				// }
				// }

				AjaxCheckBox pluginEnabledCheckbox = new AjaxCheckBox(PLUGIN_PROPERTY_CONTAINER, new PropertyModel<Boolean>(properties,
					PLUGIN_PROPERTY_CONTAINER)) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						Boolean pluginEnabled = getModelObject();
						getPluginRegistry().setEnabled(plugin, pluginEnabled);
						openConfigWindow.setVisible(pluginEnabled);
						target.add(item);
					}
				};
				item.add(pluginEnabledCheckbox);
				pluginEnabledCheckbox.setEnabled(!DefaultPluginCategories.ADMINISTRATION.equals(plugin.getCategory()));
			}
		});
	}

	private CategoryGroupPlugin getCategoryGroupPlugin() {
		if (categoryGroupPlugin == null) {
			categoryGroupPlugin = getPluginRegistry().lookup(CategoryGroupPlugin.class);
		}
		return categoryGroupPlugin;
	}

	private CategoryMenuPlugin getCategoryMenuPlugin() {
		if (categoryMenuPlugin == null) {
			categoryMenuPlugin = getPluginRegistry().lookup(CategoryMenuPlugin.class);
		}
		return categoryMenuPlugin;
	}

	@Override
	protected void onInitialize(EmptyPlugin plugin, AbstractPluginBean pluginBean) {
	}
}
