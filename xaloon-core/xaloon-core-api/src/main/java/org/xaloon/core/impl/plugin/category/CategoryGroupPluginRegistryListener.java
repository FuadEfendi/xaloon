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
package org.xaloon.core.impl.plugin.category;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.plugin.PluginType;
import org.xaloon.core.impl.plugin.tree.AbstractTreePluginRegistryListener;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;

/**
 * 
 * @author vytautas r.
 * 
 */
@Named
public class CategoryGroupPluginRegistryListener extends AbstractTreePluginRegistryListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CategoryGroupPlugin categoryGroupPlugin;

	private void insertIntoTree(String category, GenericTreeNode<Plugin> child, Plugin plugin) {
		if (!StringUtils.isEmpty(category)) {
			GenericTreeNode<Plugin> parentNode = getCategoryGroupPlugin().getTreeNodesByContext().get(category);
			if (parentNode == null) {
				parentNode = new GenericTreeNode<Plugin>();
				getCategoryGroupPlugin().getTreeNodesByContext().put(category, parentNode);
			}
			parentNode.addChild(child);
			insertIntoTree(category.substring(0, category.lastIndexOf(DelimiterEnum.SLASH.value())), child, plugin);
		} else {
			getCategoryGroupPlugin().getTree().addChild(child);
		}
	}

	private GenericTreeNode<Plugin> createMenuItem(Plugin plugin) {
		GenericTreeNode<Plugin> resultNode = new GenericTreeNode<Plugin>();
		resultNode.setData(plugin);

		return resultNode;
	}

	public void onAfterPluginRegister(Plugin registeredPlugin) {
		if (PluginType.HIDDEN.equals(registeredPlugin.getType())) {
			return;
		}
		GenericTreeNode<Plugin> child = createMenuItem(registeredPlugin);
		insertIntoTree(registeredPlugin.getCategory(), child, registeredPlugin);
	}

	/**
	 * If somehow DI is not used properly then we try to inject using default xaloon solution
	 * 
	 * @return instance of {@link CategoryGroupPlugin}
	 */
	public CategoryGroupPlugin getCategoryGroupPlugin() {
		if (categoryGroupPlugin == null) {
			categoryGroupPlugin = getPluginRegistry().lookup(CategoryGroupPlugin.class);
		}
		return categoryGroupPlugin;
	}
}
