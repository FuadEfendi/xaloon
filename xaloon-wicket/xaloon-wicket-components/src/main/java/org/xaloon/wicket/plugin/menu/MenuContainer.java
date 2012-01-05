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
package org.xaloon.wicket.plugin.menu;

import javax.inject.Inject;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.plugin.menu.panel.DynamicMenuItemPanel;
import org.xaloon.wicket.plugin.menu.panel.HierarchyMenuPanel;

/**
 * @author vytautas r.
 */
public class MenuContainer extends WebMarkupContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PluginRegistry pluginRegistry;

	DynamicMenuPlugin dynamicMenuPlugin;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public MenuContainer(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		// if (pluginRegistry.isEnabled(DynamicMenuPlugin.class)) {
		DynamicMenuPluginBean dynamicMenuPluginBean = pluginRegistry.getPluginBean(DynamicMenuPlugin.class);
		GenericTreeNode<MenuItem> menuItems = getDynamicMenuPlugin().getTree();

		if (dynamicMenuPluginBean.isHierarchyMenu()) {
			add(new HierarchyMenuPanel("menu-panel", menuItems.getChildren(), false));
		} else {
			add(new DynamicMenuItemPanel("menu-panel", menuItems.getChildren()));
		}
		// } else {
		// setVisible(false);
		// }
	}

	private DynamicMenuPlugin getDynamicMenuPlugin() {
		if (dynamicMenuPlugin == null) {
			dynamicMenuPlugin = pluginRegistry.lookup(DynamicMenuPlugin.class);
		}
		return dynamicMenuPlugin;
	}
}
