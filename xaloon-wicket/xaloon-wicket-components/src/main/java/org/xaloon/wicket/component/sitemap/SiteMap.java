/*
 *    xaloon - http://www.xaloon.org
 *    Copyright (C) 2008-2011 vytautas r.
 *
 *    This file is part of xaloon.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xaloon.wicket.component.sitemap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.util.ClassUtil;
import org.xaloon.core.impl.plugin.tree.GenericTreeNode;
import org.xaloon.core.impl.plugin.tree.MenuItem;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.plugin.menu.DynamicMenuPlugin;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class SiteMap extends WebPage {
	private static final double DEFAULT_PRIORITY_NODE = 1.0;

	private static final String DEFAULT_FREQUENCY_NODE = "1.0";

	private static final String DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** A XML markup type for sitempa */
	private final static MarkupType XML_MARKUP_TYPE = new MarkupType("xml", "text/html");

	private static final String CHANGE_DAILY = "daily";
	private static final String CHANGE_MONTHLY = "monthly";

	@Inject
	private PluginRegistry pluginRegistry;

	/**
	 * Construct.
	 */
	public SiteMap() {
		RepeatingView repeating = new RepeatingView("urlList");
		add(repeating);

		// Add menu items
		addMenuItemsToSiteMap(repeating);

		// Add dynamic items to sitemap
		addDynamicLinksToSiteMap(repeating);
	}

	private void addDynamicLinksToSiteMap(RepeatingView repeating) {
		List<DynamicLinkProvider> dynamicLinkProviders = ServiceLocator.get().getInstances(DynamicLinkProvider.class);
		if (!dynamicLinkProviders.isEmpty()) {
			for (DynamicLinkProvider dynamicLinkProvider : dynamicLinkProviders) {
				addLinksToSiteMap(repeating, dynamicLinkProvider.retrieveSiteMapPageList());
			}
		}
	}

	private void addLinksToSiteMap(RepeatingView repeating, List<KeyValue<Class<? extends Page>, PageParameters>> siteMapPageList) {
		if (siteMapPageList != null && !siteMapPageList.isEmpty()) {
			for (KeyValue<Class<? extends Page>, PageParameters> keyValue : siteMapPageList) {
				if (hasPermission(keyValue.getKey())) {
					addEntry(repeating, keyValue.getKey(), keyValue.getValue());
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void addMenuItemsToSiteMap(RepeatingView repeating) {
		DynamicMenuPlugin dynamicMenuPlugin = pluginRegistry.lookup(DynamicMenuPlugin.class);
		Map<String, GenericTreeNode<MenuItem>> menuItems = dynamicMenuPlugin.getTreeNodesByUrl();
		for (Map.Entry<String, GenericTreeNode<MenuItem>> entry : menuItems.entrySet()) {
			MenuItem menuItem = entry.getValue().getData();
			Class<? extends Page> pageClass = (Class<? extends Page>)menuItem.getPageClass();
			if (hasPermission(pageClass)) {
				PageParameters pageParameters = new PageParameters();
				fillPageParams(pageParameters, menuItem);
				addEntry(repeating, pageClass, pageParameters);
			}
		}
	}

	private void fillPageParams(PageParameters params, MenuItem menuItem) {
		for (KeyValue<String, String> param : menuItem.getParameters()) {
			if (!param.isEmpty()) {
				params.set(param.getKey(), param.getValue());
			}
		}
	}

	private void addEntry(RepeatingView repeating, Class<? extends Page> pageClass, PageParameters params) {
		WebMarkupContainer wmc = new WebMarkupContainer(repeating.newChildId());

		String freqNode = DEFAULT_FREQUENCY_NODE;
		double priorityNode = DEFAULT_PRIORITY_NODE;
		MountPage mountPageAnnotation = ClassUtil.getAnnotation(pageClass, MountPage.class);

		if (mountPageAnnotation != null) {
			freqNode = mountPageAnnotation.frequency();
			priorityNode = mountPageAnnotation.priority();
		}

		String locNode = UrlUtils.toAbsolutePath(pageClass, params);

		wmc.add(new Label("locNode", new Model<String>(locNode)));
		wmc.add(new Label("lastmodNode", new Model<String>((new SimpleDateFormat(DATE_FORMAT)).format(new Date()))));
		wmc.add(new Label("changefreqNode", new Model<String>(freqNode)));
		wmc.add(new Label("priorityNode", new Model<String>(String.valueOf(priorityNode))));
		repeating.add(wmc);
	}

	private boolean hasPermission(Class<? extends Component> entry) {
		return Session.get().getAuthorizationStrategy().isInstantiationAuthorized(entry);
	}

	@Override
	public MarkupType getMarkupType() {
		return XML_MARKUP_TYPE;
	}
}
