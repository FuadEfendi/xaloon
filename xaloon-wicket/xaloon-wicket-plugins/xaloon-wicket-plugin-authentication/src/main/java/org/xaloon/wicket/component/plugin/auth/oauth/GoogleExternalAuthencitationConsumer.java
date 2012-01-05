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
package org.xaloon.wicket.component.plugin.auth.oauth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Session;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPluginBean;
import org.xaloon.wicket.component.plugin.auth.oauth.google.TemporaryGoogleApi20;
import org.xaloon.wicket.component.security.AuthenticatedWebSession;

/**
 * @author vytautas.r
 */
@Named("googleExternalAuthenticationConsumer")
public class GoogleExternalAuthencitationConsumer extends OauthExternalAuthenticationConsumer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static Map<String, String> authenticationAttributes;

	@Override
	protected Map<String, String> getAuthenticationAttributes() {
		if (authenticationAttributes == null) {
			authenticationAttributes = getAuthenticationAttributesInstance();
		}
		return authenticationAttributes;
	}

	private Map<String, String> getAuthenticationAttributesInstance() {
		Map<String, String> authenticationAttributes = new HashMap<String, String>();
		authenticationAttributes.put("name", "name");
		authenticationAttributes.put("given_name", AuthenticationConsumer.PARAM_FIRST_NAME);
		authenticationAttributes.put("family_name", AuthenticationConsumer.PARAM_LAST_NAME);
		authenticationAttributes.put("email", AuthenticationConsumer.PARAM_EMAIL);
		authenticationAttributes.put("picture", AuthenticationConsumer.PARAM_PICTURE_SMALL);
		return authenticationAttributes;
	}


	@Override
	protected void init(ServiceBuilder serviceBuilder, int phase) {
		serviceBuilder.scope("https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email https://picasaweb.google.com/data/");
	}

	@Override
	protected OAuthProviderProperties getCustomAuthenticationProvider(ExternalAuthenticationPluginBean pluginBean) {
		return pluginBean.getGoogleProvider();
	}

	@Override
	protected Class<? extends Api> getServiceProvider() {
		return TemporaryGoogleApi20.class;
	}

	@Override
	protected void throwAuthorizationFlow(OAuthService service) {
		throw new RedirectToUrlException(service.getAuthorizationUrl(null));
	}

	@Override
	protected void additionalActionsToTake(OAuthRequest oAuthRequest) {
		oAuthRequest.addHeader("GData-Version", "3.0");
	}

	@Override
	protected String getVerificationValue(HttpServletRequest request) {
		return request.getParameter("code");
	}

	@Override
	protected String getAuthenticationResourceUrlToCheck() {
		return "https://www.googleapis.com/oauth2/v1/userinfo";
	}

	@Override
	protected boolean isDataRequiredInXmlFormat() {
		return false;
	}

	@Override
	protected void afterAccessTokenGot(Token accessToken) {
		Session.get().setMetaData(AuthenticatedWebSession.METADATAKEY_AUTH_TOKEN, accessToken);
	}

	@Override
	protected Token getRequestToken() {
		Token token = Session.get().getMetaData(METADATAKEY_REQUEST_TOKEN);
		return token;
	}
}
