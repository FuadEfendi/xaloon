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

import org.apache.wicket.Application;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.StatelessLink;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.wicket.component.security.AuthenticatedWebSession;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;
import org.xaloon.wicket.plugin.user.page.UserProfilePage;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class UserHeaderPanel extends AbstractPluginPanel<SystemPluginBean, SystemPlugin> {
	private static final long serialVersionUID = 1L;

	@Inject
	SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public UserHeaderPanel(String id) {
		super(id);
	}

	@Override
	protected void onConfigure() {
		setVisible(securityFacade.isLoggedIn());
	}

	/**
	 * Additional actions might be taken when actual sign-out was executed
	 */
	protected void onSignOut() {
		setResponsePage(Application.get().getHomePage());
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		// Add logout link
		add(new StatelessLink<Void>("logout") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				AuthenticatedWebSession.get().signOut();
				onSignOut();
			}
		});

		// Add profile link
		BookmarkablePageLink<Void> profileLink = new BookmarkablePageLink<Void>("profile-link", UserProfilePage.class);
		add(profileLink);

		// Add username display
		profileLink.add(new Label("username", new Model<String>(securityFacade.getCurrentUserDisplayName())));
	}

	@Override
	protected void onInitialize(SystemPlugin plugin, SystemPluginBean pluginBean) {
	}
}
