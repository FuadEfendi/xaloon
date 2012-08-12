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
package org.xaloon.wicket.plugin.email.panel;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.xaloon.core.api.plugin.email.EmailPluginBean;
import org.xaloon.wicket.plugin.admin.AbstractPluginAdministrationPanel;
import org.xaloon.wicket.plugin.email.EmailPlugin;

/**
 * @author vytautas r.
 */
public class EmailAdministrationPanel extends AbstractPluginAdministrationPanel<EmailPluginBean, EmailPlugin> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public EmailAdministrationPanel(String id) {
		super(id);
	}


	@Override
	protected void onInitialize(EmailPlugin plugin, EmailPluginBean pluginBean) {
		add(new CheckBox("debug"));
		add(new CheckBox("requiresAuthentication"));
		add(new CheckBox("startTLS"));

		add(new TextField<String>("host"));
		add(new TextField<String>("username"));
		add(new PasswordTextField("password") {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isRequired() {
				return false;
			}
		});
		add(new TextField<String>("port"));
		add(new TextField<String>("charset"));

		add(new TextField<String>("fromSubject"));
		add(new TextField<String>("fromTitle"));
		add(new TextField<String>("fromEmail"));

		add(new TextField<String>("toSubject"));
		add(new TextField<String>("toTitle"));
		add(new TextField<String>("toEmail"));
	}
}
