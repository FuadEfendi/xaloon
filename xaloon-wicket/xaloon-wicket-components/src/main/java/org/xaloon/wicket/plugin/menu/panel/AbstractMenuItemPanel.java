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
package org.xaloon.wicket.plugin.menu.panel;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.util.ClassUtil;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.component.mount.annotation.MountPageGroup;

/**
 * Simple dynamic menu helper panel with common methods
 * 
 * @author vytautas r.
 * 
 */
class AbstractMenuItemPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@Inject
	private PluginRegistry pluginRegistry;

	/**
	 * Bookmarkable page link
	 */
	private Class<? extends Page> menuItemPageClass;

	private boolean useMenuDelimiter = true;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractMenuItemPanel(String id, IModel<?> model) {
		super(id, model);
	}

	@SuppressWarnings("unchecked")
	protected Class<? extends Page> getBookmarkablePageLink(PageParameters params, MenuItem menuItem) {
		Class<? extends Page> menuPageClass;
		if (menuItemPageClass != null) {
			menuPageClass = menuItemPageClass;
			fillPageParams(params, menuItem);
		} else {
			menuPageClass = (Class<? extends Page>)menuItem.getPageClass();
		}

		return menuPageClass;
	}

	private void fillPageParams(PageParameters params, MenuItem menuItem) {
		for (KeyValue<String, String> param : menuItem.getParameters()) {
			if (!param.isEmpty()) {
				params.set(param.getKey(), param.getValue());
			}
		}
	}

	/**
	 * @param menuItemPageClass
	 */
	public void setMenuItemPageClass(Class<? extends Page> menuItemPageClass) {
		this.menuItemPageClass = menuItemPageClass;
	}

	/**
	 * @return bookmarkable page class
	 */
	public Class<? extends Page> getMenuItemPageClass() {
		return menuItemPageClass;
	}

	/**
	 * @return true when use menu item delimiter
	 */
	public boolean isUseMenuDelimiter() {
		return useMenuDelimiter;
	}

	/**
	 * @param useMenuDelimiter
	 * @return this panel
	 */
	public Component setUseMenuDelimiter(boolean useMenuDelimiter) {
		this.useMenuDelimiter = useMenuDelimiter;
		return this;
	}

	boolean isPluginEnabled(Class<?> pageClass) {
		MountPageGroup annotation = ClassUtil.getAnnotation(pageClass, MountPageGroup.class);
		if (annotation != null) {
			Class<?> pluginClass = annotation.plugin();
			if (pluginClass != null) {
				return getPluginRegistry().isEnabled(pluginClass);
			}
		}
		return true;
	}

	protected PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}
}
