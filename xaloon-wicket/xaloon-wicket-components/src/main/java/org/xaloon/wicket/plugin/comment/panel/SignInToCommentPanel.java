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
package org.xaloon.wicket.plugin.comment.panel;

import javax.inject.Inject;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.plugin.comment.CommentPluginBean;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.wicket.component.security.page.SignInPage;
import org.xaloon.wicket.component.security.panel.SignInPanel;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.comment.CommentPlugin;

/**
 * @author vytautas r.
 */
public class SignInToCommentPanel extends AbstractPluginPanel<CommentPluginBean, CommentPlugin> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AuthenticationFacade authenticationFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public SignInToCommentPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize(CommentPlugin plugin, CommentPluginBean pluginBean) {
		BookmarkablePageLink<Void> googleLink = createLink(AuthenticationFacade.LOGIN_TYPE_GOOGLE);
		add(googleLink);

		BookmarkablePageLink<Void> facebookLink = createLink(AuthenticationFacade.LOGIN_TYPE_FACEBOOK);
		add(facebookLink);

		setVisible(!getSecurityFacade().isLoggedIn() && (googleLink.isVisible() || facebookLink.isVisible()));
	}

	private BookmarkablePageLink<Void> createLink(String loginType) {
		PageParameters pageParameters = new PageParameters();
		pageParameters.set(SignInPanel.LOGIN_TYPE, loginType);
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(loginType, SignInPage.class, pageParameters);
		link.setVisible(authenticationFacade.isPluginEnabled() && authenticationFacade.isEnabled(loginType));
		return link;
	}
}
