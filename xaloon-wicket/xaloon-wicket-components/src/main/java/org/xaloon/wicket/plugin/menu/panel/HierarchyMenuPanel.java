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
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;

/**
 * Hierarchy menu panel
 * 
 * @author vytautas r.
 * 
 */
public class HierarchyMenuPanel extends AbstractMenuItemPanel {
	private static final long serialVersionUID = 1L;

	@Inject
	private StringResourceLoader stringResourceLoader;

	private boolean removeMenuClass;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param children
	 * @param removeMenuClass
	 */
	public HierarchyMenuPanel(String id, List<GenericTreeNode<MenuItem>> children, boolean removeMenuClass) {
		super(id, Model.ofList(children));
		setVisible(!children.isEmpty());
		this.removeMenuClass = removeMenuClass;
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		init();
	}

	@SuppressWarnings("unchecked")
	private void init() {
		WebMarkupContainer ulContainer = new WebMarkupContainer("ul-container");
		if (removeMenuClass) {
			ulContainer.add(AttributeModifier.replace("class", DelimiterEnum.EMPTY.value()));
		}
		add(ulContainer);
		ulContainer.add(new ListView<GenericTreeNode<MenuItem>>("menu-list", (List<GenericTreeNode<MenuItem>>)getDefaultModelObject()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<GenericTreeNode<MenuItem>> item) {
				GenericTreeNode<MenuItem> menuTreeNode = item.getModelObject();
				MenuItem menuItem = menuTreeNode.getData();

				PageParameters params = new PageParameters();
				Class<? extends Page> menuItemLink = getBookmarkablePageLink(params, menuItem);

				boolean visibile = isPluginEnabled(menuItem.getPageClass()) &&
					Session.get().getAuthorizationStrategy().isInstantiationAuthorized(menuItemLink);
				item.setVisible(visibile);

				WebMarkupContainer container = new WebMarkupContainer("item-container");
				item.add(container);
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", menuItemLink, params);
				container.add(link);
				link.add(new Label("menu-item-label", new Model<String>(stringResourceLoader.getString(menuItem.getPageClass(), menuItem.getKey()))));
				if (menuTreeNode.hasMoreThanOneChildren()) {
					link.add(AttributeModifier.replace("class", "has_submenu"));
					HierarchyMenuPanel hmp = new HierarchyMenuPanel("sub-menu", menuTreeNode.getChildren(), true);
					hmp.setMenuItemPageClass(getMenuItemPageClass());
					container.add(hmp);
				} else {
					container.add(new EmptyPanel("sub-menu").setVisible(false));
				}

			}
		});
	}
}
