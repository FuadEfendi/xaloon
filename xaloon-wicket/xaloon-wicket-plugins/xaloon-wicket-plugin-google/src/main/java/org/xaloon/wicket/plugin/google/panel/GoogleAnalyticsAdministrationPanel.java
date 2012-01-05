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
package org.xaloon.wicket.plugin.google.panel;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.xaloon.wicket.plugin.admin.AbstractPluginAdministrationPanel;
import org.xaloon.wicket.plugin.google.GoogleAnalyticsPlugin;
import org.xaloon.wicket.plugin.google.GoogleAnalyticsPluginBean;

/**
 * Administration panel for addthis plugin.
 * <p>
 * It is possible to change username only at this time.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */

public class GoogleAnalyticsAdministrationPanel extends AbstractPluginAdministrationPanel<GoogleAnalyticsPluginBean, GoogleAnalyticsPlugin> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            component string id tag used in html
	 */
	public GoogleAnalyticsAdministrationPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize(GoogleAnalyticsPlugin plugin, GoogleAnalyticsPluginBean pluginBean) {
		add(new RequiredTextField<String>("value"));
		add(new CheckBox("asynchronous"));
	}
}
