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

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;

/**
 * Dynamic menu panel displays all menu levels as flat menu (not nested) for example:
 * <p>
 * Menu1, Menu2, Menu3 SubMenu1, SubMenu2
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */

public class FlatDynamicMenuPanel extends AbstractMenuItemPanel {
	private static final String DEFAULT_MENU_LEVEL = "menu-level";

	private static final String CSS_CLASS = "class";

	private static final long serialVersionUID = 1L;

	private String menuLevelCssClass = DEFAULT_MENU_LEVEL;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param menuItems
	 */
	public FlatDynamicMenuPanel(String id, List<List<GenericTreeNode<MenuItem>>> menuItems) {
		super(id, Model.ofList(menuItems));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		List<List<GenericTreeNode<MenuItem>>> menuItems = (List<List<GenericTreeNode<MenuItem>>>)getDefaultModelObject();
		RepeatingView repeatingView = new RepeatingView("menu-list");
		add(repeatingView);
		int currentMenuLevel = 1;
		for (List<GenericTreeNode<MenuItem>> item : menuItems) {
			WebMarkupContainer inner = new WebMarkupContainer(repeatingView.newChildId());
			repeatingView.add(inner);
			DynamicMenuItemPanel dynamicMenuItemPanel = new DynamicMenuItemPanel("menu-item-panel", item);
			dynamicMenuItemPanel.setUseMenuDelimiter(isUseMenuDelimiter());
			dynamicMenuItemPanel.setMenuItemPageClass(getMenuItemPageClass());
			String currentLevelCssClass = menuLevelCssClass + ((currentMenuLevel > 1) ? "-" + currentMenuLevel : DelimiterEnum.EMPTY.value());
			dynamicMenuItemPanel.add(AttributeModifier.replace(CSS_CLASS, currentLevelCssClass));
			inner.add(dynamicMenuItemPanel);
			currentMenuLevel++;
		}
	}

	/**
	 * @param menuLevelCssClass
	 */
	public void setMenuLevelCssClass(String menuLevelCssClass) {
		this.menuLevelCssClass = menuLevelCssClass;
	}
}
