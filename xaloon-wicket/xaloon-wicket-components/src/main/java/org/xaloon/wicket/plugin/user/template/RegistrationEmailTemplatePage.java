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
package org.xaloon.wicket.plugin.user.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.plugin.email.template.EmailContentTemplatePage;
import org.xaloon.wicket.plugin.user.page.ActivationPage;
import org.xaloon.wicket.plugin.user.panel.ActivationPanel;
import org.xaloon.wicket.util.UrlUtils;


/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class RegistrationEmailTemplatePage extends EmailContentTemplatePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param username
	 * @param password
	 * @param activationKey
	 */
	public RegistrationEmailTemplatePage(String username, String password, String activationKey) {
		add(new Label("username", new Model<String>(username)));
		add(new Label("username2", new Model<String>(username)));
		add(new Label("password", new Model<String>(password)));
		add(new Label("activation-key", new Model<String>(activationKey)));

		PageParameters parameters = new PageParameters();
		parameters.set(ActivationPanel.ACTIVATION_KEY, activationKey);

		String url = UrlUtils.toAbsolutePath(ActivationPage.class, parameters);
		Label activation_key_name = new Label("activation-link-name", new Model<String>(url));
		ExternalLink activation_link = new ExternalLink("activation-link", url);
		activation_link.add(activation_key_name);
		add(activation_link);
	}
}
