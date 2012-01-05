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
package org.xaloon.wicket.application.page;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.wicket.component.html.MetaTagWebContainer;

/**
 * Layout page. It has default properties which will be injected when creating a new virtual page It is also contains core design elements for web
 * application
 * 
 * @author vytautas r.
 * 
 */
public abstract class LayoutWebPage extends WebPage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LayoutWebPage.class);

	/** wicket id for page title tag in html layout page **/
	public static final String PAGE_TITLE = "title";

	/** wicket id for page keywords tag in html layout page **/
	public static final String PAGE_KEYWORDS = "keywords";

	/** wicket id for page description tag in html layout page **/
	public static final String PAGE_DESCRIPTION = "description";

	/** wicket id for content panel id in html layout page **/
	public static final String CONTENT_PANEL_ID = "content";

	@Inject
	private LayoutComponentInitializer layoutComponentInitializer;

	@Override
	protected void onInitialize() {
		super.onInitialize();

		Panel content = getContentPanel(CONTENT_PANEL_ID, getPageParameters());
		addOrReplace(content);

		addTitle(PAGE_TITLE, getDefaultString(PAGE_TITLE));
		addMetaTag(PAGE_DESCRIPTION, getDefaultString(PAGE_DESCRIPTION));
		addMetaTag(PAGE_KEYWORDS, getDefaultString(PAGE_KEYWORDS));

		getLayoutComponentInitializer().onInitialize(this, content);
	}

	private String getDefaultString(String key) {
		String value = getString(key, null, key);
		if (key.equals(value)) {
			LOGGER.warn(this.getClass().getName() + ": Could not get key value for - " + key);
		}
		return value;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		getLayoutComponentInitializer().onBeforeRender(this);
	}

	protected abstract Panel getContentPanel(String id, PageParameters pageParameters);

	/**
	 * Use this method to store more additional information to layout page
	 */
	void addAdditionalInformation() {
	}

	/**
	 * Add title to html head. Do not use this method directly if using with {@link VirtualPageFactory}
	 * 
	 * @param name
	 *            name of attribute, for example - "title"
	 * @param value
	 *            value of attribute
	 */
	void addTitle(String name, String value) {
		if (!StringUtils.isEmpty(value)) {
			addOrReplace(new Label(name, new Model<String>(value)));
		}
	}

	/**
	 * Add meta tag to html head. Do not use this method directly if using with {@link VirtualPageFactory}
	 * 
	 * @param name
	 *            name of meta tag, for example - "description"
	 * @param value
	 *            content of meta tag
	 */
	protected void addMetaTag(String name, String value) {
		addOrReplaceMetaTag(name, value);
	}

	protected LayoutComponentInitializer getLayoutComponentInitializer() {
		return layoutComponentInitializer;
	}

	private void addOrReplaceMetaTag(String name, String value) {
		if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(value)) {
			addOrReplace(new MetaTagWebContainer(new Model<KeyValue<String, String>>(new DefaultKeyValue<String, String>(name, value))));
		}
	}
}
