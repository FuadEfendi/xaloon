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
package org.xaloon.wicket.plugin.user.panel;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.wicket.component.security.page.SignInNoParamsPage;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;
import org.xaloon.wicket.plugin.user.page.ResetPasswordPage;
import org.xaloon.wicket.plugin.user.page.UserRegistrationPage;


/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class AnonymousHeaderPanel extends AbstractPluginPanel<SystemPluginBean, SystemPlugin> {
	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public AnonymousHeaderPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize(SystemPlugin plugin, SystemPluginBean pluginBean) {
		add(createRegistrationLink());
		add(new BookmarkablePageLink<Void>("login", SignInNoParamsPage.class));
		add(new BookmarkablePageLink<Void>("reset-password", ResetPasswordPage.class));
	}

	protected Component createRegistrationLink() {
		return new BookmarkablePageLink<Void>("register", UserRegistrationPage.class) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onConfigure() {
				setVisible(getPluginBean().isUserRegistrationEnabled());
				super.onConfigure();
			}
		};
	}

	@Override
	protected void onConfigure() {
		setVisible(!securityFacade.isLoggedIn());
	}
}
