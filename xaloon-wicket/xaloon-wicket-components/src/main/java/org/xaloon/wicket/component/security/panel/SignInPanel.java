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
package org.xaloon.wicket.component.security.panel;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.value.ValueMap;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.wicket.component.security.AuthenticatedWebSession;
import org.xaloon.wicket.component.security.page.SignInNoParamsPage;
import org.xaloon.wicket.component.security.page.SignInPage;
import org.xaloon.wicket.plugin.user.page.UserProfilePage;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class SignInPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final String LOGIN_TYPE = "lgn_tp";

	/**
	 * 
	 */
	public static final MetaDataKey<String> METADATAKEY_LOGIN_TYPE = new MetaDataKey<String>() {
		private static final long serialVersionUID = 1L;
	};

	/**
	 * 
	 */
	public static final MetaDataKey<String> METADATAKEY_REFERER = new MetaDataKey<String>() {
		private static final long serialVersionUID = 1L;
	};

	@Inject
	private AuthenticationFacade authenticationFacade;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public SignInPanel(String id, PageParameters pageParameters) {
		super(id);
		checkAndExecuteExternalAuthentication(pageParameters);
	}

	private void checkAndExecuteExternalAuthentication(PageParameters pageParameters) {
		// process only when plugin is enabled
		if (isExternalAuthenticationEnabled()) {
			// Check if there is a parameter to login using external system
			if (!pageParameters.isEmpty() && !pageParameters.get(LOGIN_TYPE).isEmpty()) {
				beginConsumption(pageParameters);
			} else if (authenticationFacade.isResponseToEndConsumption(pageParameters.getNamedKeys())) {
				// Check if there is any parameter defining as a response from external system
				// If yes - finish authentication process
				endConsumption();
			}
			// If there are no any required parameters then assuming that it is a simple authentication panel
		}
	}

	private boolean isExternalAuthenticationEnabled() {
		try {
			return authenticationFacade.isPluginEnabled();
		} catch (NullPointerException e) {
			// If WELD is used then authenticationFacade is always not null, but exception is thrown when trying to access the method
			return false;
		}
	}

	private void endConsumption() {
		String loginType = getSession().getMetaData(METADATAKEY_LOGIN_TYPE);
		String requestUrl = UrlUtils.toAbsolutePath(SignInNoParamsPage.class, null);
		AuthenticationToken authenticationToken = authenticationFacade.endConsumption(loginType, requestUrl);
		if (authenticationToken != null && authenticationToken.isAuthenticated()) {
			executeAfterConsumption(loginType, authenticationToken);
		} else {
			// Authentication failed!
			setResponsePage(getApplication().getHomePage());
		}
	}

	private void executeAfterConsumption(String loginType, AuthenticationToken authenticationToken) {
		AuthenticatedWebSession session = AuthenticatedWebSession.get();
		if (!securityFacade.isLoggedIn()) {
			// Sign in user.
			session.signIn(authenticationToken);
		} else {
			// User is signed in. This means that he added external authentication as additional one.
			addAlias(loginType, authenticationToken.getName());
		}
		String refereUrl = getReferer(session);

		if (StringUtils.isEmpty(refereUrl) && !securityFacade.isRegistered()) {
			refereUrl = UrlUtils.toAbsolutePath(UserProfilePage.class, null);
		}
		if (!StringUtils.isEmpty(refereUrl)) {
			throw new RedirectToUrlException(refereUrl);
		} else {
			continueToOriginalDestination();// TODO fix me
		}
	}

	private String getReferer(WebSession session) {
		String refereUrl = session.getMetaData(METADATAKEY_REFERER);
		// Clean up referer
		session.setMetaData(METADATAKEY_REFERER, null);
		return refereUrl;
	}

	protected void addAlias(String loginType, String name) {
		userFacade.addAlias(securityFacade.getCurrentUsername(), new DefaultKeyValue<String, String>(loginType, name));
	}

	private void beginConsumption(PageParameters pageParameters) {
		String loginType = pageParameters.get(LOGIN_TYPE).toString();
		getSession().setMetaData(METADATAKEY_LOGIN_TYPE, loginType);
		getSession().bind();
		String requestUrl = UrlUtils.toAbsolutePath(SignInNoParamsPage.class, null);
		authenticationFacade.beginConsumption(loginType, requestUrl);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		// Create feedback panel and add to page
		add(new FeedbackPanel("feedback"));

		add(new SignInForm("sign-in-form"));
	}

	private BookmarkablePageLink<Void> createLink(String loginType) {
		PageParameters pageParameters = new PageParameters();
		pageParameters.set(LOGIN_TYPE, loginType);
		BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>(loginType, SignInPage.class, pageParameters);
		link.setVisible(isExternalAuthenticationEnabled() && authenticationFacade.isEnabled(loginType));
		return link;
	}

	class SignInForm extends StatelessForm<Void> {
		private static final long serialVersionUID = 1L;

		private static final String USERNAME = "username";
		private static final String PASSWORD = "password";

		private final ValueMap properties = new ValueMap();

		public SignInForm(String id) {
			super(id);
			add(new TextField<String>(USERNAME, new PropertyModel<String>(properties, USERNAME)));
			add(new PasswordTextField(PASSWORD, new PropertyModel<String>(properties, PASSWORD)));

			// External authentication links
			add(createLink(AuthenticationFacade.LOGIN_TYPE_GOOGLE));
			add(createLink(AuthenticationFacade.LOGIN_TYPE_FACEBOOK));
			add(createLink(AuthenticationFacade.LOGIN_TYPE_LINKEDIN));
			add(createLink(AuthenticationFacade.LOGIN_TYPE_TWITTER));
			add(createLink(AuthenticationFacade.LOGIN_TYPE_YAHOO));
		}

		@Override
		protected void onSubmit() {
			AuthenticationToken auth = AuthenticatedWebSession.get().signIn(getUsername(), getPassword());
			if (auth.isAuthenticated()) {
				onSignInSucceeded();
				continueToOriginalDestination();// TODO fix me
			} else {
				onSignInFailed(auth.getMessage());
			}
		}

		private String getUsername() {
			return properties.getString(USERNAME);
		}

		private String getPassword() {
			return properties.getString(PASSWORD);
		}
	}

	/**
	 * Called when sign in was successful
	 */
	protected void onSignInSucceeded() {
	}

	/**
	 * Called when sign in failed
	 * 
	 * @param messageKey
	 */
	protected void onSignInFailed(String messageKey) {
		// Try the component based localizer first. If not found try the
		// application localizer. Else use the default
		error(getLocalizer().getString(messageKey, this, messageKey));
	}
}
