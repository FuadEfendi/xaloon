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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.tree.TreeNode;
import org.xaloon.core.api.util.ClassUtil;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.component.mount.annotation.MountPageGroup;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
@Named("dynamicMenuFacade")
public class DefaultDynamicMenuFacade implements DynamicMenuFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DynamicMenuPlugin dynamicMenuPlugin;

	@Inject
	private PluginRegistry pluginRegistry;

	/**
	 * 
	 * @see org.xaloon.wicket.plugin.menu.DynamicMenuFacade#getMenu(java.lang.Class)
	 */
	public GenericTreeNode<MenuItem> getMenu(Class<?> pageClass) {
		String fullPagePath = UrlUtils.generateFullvalue(pageClass);
		if (!StringUtils.isEmpty(fullPagePath)) {
			return getDynamicMenuPlugin().getTreeNodesByUrl().get(fullPagePath);
		}
		throw new IllegalArgumentException("Menu item for provided class was not found: " + pageClass);
	}

	private GenericTreeNode<MenuItem> getMenuByContext(Class<?> pageClass) {
		MountPageGroup mountPageGroup = ClassUtil.getAnnotation(pageClass, MountPageGroup.class);
		if (mountPageGroup == null) {
			return null;
		}
		return getDynamicMenuPlugin().getTreeNodesByContext().get(mountPageGroup.value());
	}

	/**
	 * 
	 * @see org.xaloon.wicket.plugin.menu.DynamicMenuFacade#getRootMenu()
	 */
	public List<GenericTreeNode<MenuItem>> getRootMenu() {
		return getDynamicMenuPlugin().getTree().getChildren();
	}

	protected DynamicMenuPlugin getDynamicMenuPlugin() {
		if (dynamicMenuPlugin == null) {
			dynamicMenuPlugin = pluginRegistry.lookup(DynamicMenuPlugin.class);
		}
		return dynamicMenuPlugin;
	}

	/**
	 * 
	 * @see org.xaloon.wicket.plugin.menu.DynamicMenuFacade#getParent(java.lang.Class)
	 */
	public TreeNode<MenuItem> getParent(Class<?> pageClass) {
		TreeNode<MenuItem> currentMenuTreeItem = getMenu(pageClass);
		if (currentMenuTreeItem != null) {
			return currentMenuTreeItem.getParent();
		}
		currentMenuTreeItem = getMenuByContext(pageClass);
		if (currentMenuTreeItem != null) {
			return currentMenuTreeItem;
		}
		return null;
	}
}
