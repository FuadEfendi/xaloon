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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.util.ClassUtil;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.component.mount.MountScanner;
import org.xaloon.wicket.component.mount.MountScannerListener;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.component.mount.annotation.MountPageGroup;
import org.xaloon.wicket.util.UrlUtils;

/**
 * Dynamic menu observer is used to register menu items when packages are scanned for mounted pages
 * <p>
 * This class is used together with {@link MountScanner}. List of scanned web page classes is passed to this observer and will be added to menu tree.
 * <p>
 * This class should be used on application startup.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */
@Named("dynamicMenuMountScannerListener")
public class DynamicMenuMountScannerListener implements MountScannerListener {
	private static final String PARENT_MENU_ITEM_PREFIX = "parent.";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DynamicMenuMountScannerListener.class);

	/**
	 * Resource loader for current application. It is injected via spring IoC
	 */
	@Inject
	private StringResourceLoader stringResourceLoader;

	@Inject
	@Named("pluginRegistry")
	private PluginRegistry pluginRegistry;

	private DynamicMenuPlugin dynamicMenuPlugin;

	private void insertIntoTree(GenericTreeNode<MenuItem> child, Class<?> pageClass) {
		MountPageGroup mountPageGroup = ClassUtil.getAnnotation(pageClass, MountPageGroup.class);
		if (mountPageGroup != null) {
			GenericTreeNode<MenuItem> parentNode = getDynamicMenuPlugin().getTreeNodesByContext().get(mountPageGroup.value());
			if (parentNode == null) {
				parentNode = createContextItem(child.getData(), mountPageGroup, pageClass);
				Class<?> mountableWebPageClass = ClassUtil.getClassByAnnottation(pageClass, MountPageGroup.class);

				insertIntoTree(parentNode, mountableWebPageClass.getSuperclass());
			}
			parentNode.addChild(child);
			if (parentNode.getChildren().size() > 0 && isFirstChildChanged(parentNode, parentNode.getChildren().get(0))) {
				setChildInformationToParent(parentNode.getData(), child.getData());
			}
			// add child to parent
		} else {
			// add child to root?
			getDynamicMenuPlugin().getTree().addChild(child);
		}
	}

	private boolean isFirstChildChanged(GenericTreeNode<MenuItem> parentNode, GenericTreeNode<MenuItem> childTreeNode) {
		MenuItem parentMenuItem = parentNode.getData();
		MenuItem childMenuItem = childTreeNode.getData();
		if (parentMenuItem.getPageClass() == null || childMenuItem.getPageClass() == null) {
			// names cannot be null. There might a bug in creating item if such evaluation is true
			return true;
		}
		return !parentMenuItem.getPageClass().equals(childMenuItem.getPageClass());
	}

	private void setChildInformationToParent(MenuItem parentData, MenuItem childData) {
		parentData.setPageClass(childData.getPageClass());
		parentData.setKey(PARENT_MENU_ITEM_PREFIX + childData.getKey());
	}

	private GenericTreeNode<MenuItem> createContextItem(MenuItem child, MountPageGroup mountPageGroup, Class<?> pageClass) {

		GenericTreeNode<MenuItem> resultNode = new GenericTreeNode<MenuItem>();
		getDynamicMenuPlugin().getTreeNodesByContext().put(mountPageGroup.value(), resultNode);

		MenuItem menuItem = new MenuItem();
		menuItem.setOrder(mountPageGroup.order());
		setChildInformationToParent(menuItem, child);
		resultNode.setData(menuItem);

		return resultNode;
	}

	private GenericTreeNode<MenuItem> createMenuItem(Class<?> pageClass) {
		MountPage mountPage = ClassUtil.getAnnotation(pageClass, MountPage.class);
		if (!mountPage.visible()) {
			return null;
		}
		String url = UrlUtils.generateFullvalue(pageClass);

		if (getDynamicMenuPlugin().getTreeNodesByUrl().containsKey(url)) {
			// Remove existing and later override with new one
			GenericTreeNode<MenuItem> found = getDynamicMenuPlugin().getTreeNodesByUrl().get(url);
			found.getParent().getChildren().remove(found);
		}
		GenericTreeNode<MenuItem> resultNode = new GenericTreeNode<MenuItem>();
		getDynamicMenuPlugin().getTreeNodesByUrl().put(url, resultNode);

		// int order = getPageOrder(pageClass, mountPage);
		MenuItem menuItem = new MenuItem();
		menuItem.setPageClass(pageClass);
		menuItem.setKey(pageClass.getName());
		menuItem.setOrder(mountPage.order());
		resultNode.setData(menuItem);

		return resultNode;
	}

	public void onMount(List<Class<?>> classesToMount) {
		for (Class<?> item : classesToMount) {
			LOGGER.info("mounting: " + item);
			GenericTreeNode<MenuItem> child = createMenuItem(item);
			if (child != null) {
				insertIntoTree(child, item);
			}
		}
	}

	private DynamicMenuPlugin getDynamicMenuPlugin() {
		if (dynamicMenuPlugin == null) {
			dynamicMenuPlugin = pluginRegistry.lookup(DynamicMenuPlugin.class);
		}
		return dynamicMenuPlugin;
	}

	@Override
	public void onMount(Map<String, Class<?>> mountPages) {
		onMount(new ArrayList<Class<?>>(mountPages.values()));
	}
}
