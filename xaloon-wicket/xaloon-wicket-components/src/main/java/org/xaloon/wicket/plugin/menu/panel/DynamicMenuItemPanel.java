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

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;

/**
 * Displays menu items for single level
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */

public class DynamicMenuItemPanel extends AbstractMenuItemPanel {
	private static final long serialVersionUID = 1L;

	@Inject
	private StringResourceLoader stringResourceLoader;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param menuItemList
	 */
	public DynamicMenuItemPanel(String id, List<GenericTreeNode<MenuItem>> menuItemList) {
		super(id, Model.ofList(menuItemList));
		if (menuItemList.isEmpty()) {
			setVisible(false);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		List<GenericTreeNode<MenuItem>> menuItemList = (List<GenericTreeNode<MenuItem>>)getDefaultModelObject();
		initDynamicMenuGroup(menuItemList);
	}

	private void initDynamicMenuGroup(List<GenericTreeNode<MenuItem>> menuGroupItems) {
		RepeatingView repeatingView = new RepeatingView("menu-list");
		add(repeatingView);

		int current = 0;
		for (GenericTreeNode<MenuItem> item : menuGroupItems) {
			WebMarkupContainer inner = new WebMarkupContainer(repeatingView.newChildId());
			repeatingView.add(inner);

			MenuItem menuItem = item.getData();
			PageParameters params = new PageParameters();
			Class<? extends Page> menuItemLink = getBookmarkablePageLink(params, menuItem);

			inner.setVisible(Session.get().getAuthorizationStrategy().isInstantiationAuthorized(menuItemLink));

			BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", menuItemLink, params);
			inner.add(link);
			link.add(new Label("menu-item-label", new Model<String>(stringResourceLoader.getString(menuItem.getPageClass(), menuItem.getKey()))));

			WebMarkupContainer menuSeparator = new WebMarkupContainer("menu_separator");
			inner.add(menuSeparator);
			menuSeparator.setVisible(isUseMenuDelimiter() && !isLast(current, menuGroupItems.size()));

			if (current == 0) {
				inner.add(AttributeModifier.replace("class", "is_first"));
			}
			if (isLast(current, menuGroupItems.size())) {
				inner.add(AttributeModifier.replace("class", "is_last"));
			}
			current++;
		}
	}

	private boolean isLast(int current, int totalSize) {
		return (current + 1) == totalSize;
	}
}
