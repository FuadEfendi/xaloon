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
package org.xaloon.wicket.component.plugin.auth.panel;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPlugin;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPluginBean;
import org.xaloon.wicket.component.plugin.auth.oauth.OAuthProviderProperties;
import org.xaloon.wicket.component.plugin.auth.openid.OpenIdProviderProperties;
import org.xaloon.wicket.plugin.admin.AbstractPluginAdministrationPanel;

/**
 * @author vytautas r.
 */
public class ExternalAuthenticationAdministrationPanel extends
	AbstractPluginAdministrationPanel<ExternalAuthenticationPluginBean, ExternalAuthenticationPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public ExternalAuthenticationAdministrationPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize(ExternalAuthenticationPlugin plugin, ExternalAuthenticationPluginBean pluginBean) {
		ExternalAuthenticationPluginBean obj = (ExternalAuthenticationPluginBean)getDefaultModelObject();

		// LinkedIn authentication properties
		add(new OAuthProviderPropertiesPanel("linkedin-properties-panel", new CompoundPropertyModel<OAuthProviderProperties>(
			obj.getLinkedInProvider())));

		// Facebook authentication properties
		add(new OAuthProviderPropertiesPanel("facebook-properties-panel", new CompoundPropertyModel<OAuthProviderProperties>(
			obj.getFacebookProvider())));

		// Twitter authentication properties
		add(new OAuthProviderPropertiesPanel("twitter-properties-panel", new CompoundPropertyModel<OAuthProviderProperties>(obj.getTwitterProvider())));

		// Google authentication properties
		add(new OAuthProviderPropertiesPanel("google-properties-panel", new CompoundPropertyModel<OAuthProviderProperties>(obj.getGoogleProvider())));

		// Yahoo authentication properties
		add(new OpenIdProviderPropertiesPanel("yahoo-properties-panel", new CompoundPropertyModel<OpenIdProviderProperties>(
			obj.getYahooOpenIdProvider())));
	}

	@Override
	public void initModel(IModel<ExternalAuthenticationPluginBean> model) {
		setDefaultModel(model);
	}
}
