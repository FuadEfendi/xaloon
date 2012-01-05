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
package org.xaloon.wicket.component.plugin.auth;

import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.wicket.component.plugin.auth.oauth.OAuthProviderProperties;
import org.xaloon.wicket.component.plugin.auth.openid.OpenIdProviderProperties;

/**
 * @author vytautas r.
 */
public class ExternalAuthenticationPluginBean extends AbstractPluginBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String YAHOO_OPENID_LINK = "http://yahoo.com/";

	private OAuthProviderProperties linkedInProvider = new OAuthProviderProperties(AuthenticationFacade.LOGIN_TYPE_LINKEDIN);

	private OAuthProviderProperties twitterProvider = new OAuthProviderProperties(AuthenticationFacade.LOGIN_TYPE_TWITTER);

	private OAuthProviderProperties facebookProvider = new OAuthProviderProperties(AuthenticationFacade.LOGIN_TYPE_FACEBOOK);

	private OAuthProviderProperties googleProvider = new OAuthProviderProperties(AuthenticationFacade.LOGIN_TYPE_GOOGLE);

	private OpenIdProviderProperties yahooOpenIdProvider = new OpenIdProviderProperties(AuthenticationFacade.LOGIN_TYPE_YAHOO, YAHOO_OPENID_LINK);


	/**
	 * @param linkedInProvider
	 */
	public void setLinkedInProvider(OAuthProviderProperties linkedInProvider) {
		this.linkedInProvider = linkedInProvider;
	}

	/**
	 * @return linked-in provider details
	 */
	public OAuthProviderProperties getLinkedInProvider() {
		return linkedInProvider;
	}

	/**
	 * @param twitterProvider
	 */
	public void setTwitterProvider(OAuthProviderProperties twitterProvider) {
		this.twitterProvider = twitterProvider;
	}

	/**
	 * @return twitter provider details
	 */
	public OAuthProviderProperties getTwitterProvider() {
		return twitterProvider;
	}

	/**
	 * @param facebookProvider
	 */
	public void setFacebookProvider(OAuthProviderProperties facebookProvider) {
		this.facebookProvider = facebookProvider;
	}

	/**
	 * @return facebook provider details
	 */
	public OAuthProviderProperties getFacebookProvider() {
		return facebookProvider;
	}

	/**
	 * Gets googleProvider.
	 * 
	 * @return googleProvider
	 */
	public OAuthProviderProperties getGoogleProvider() {
		return googleProvider;
	}

	/**
	 * Sets googleProvider.
	 * 
	 * @param googleProvider
	 *            googleProvider
	 */
	public void setGoogleProvider(OAuthProviderProperties googleProvider) {
		this.googleProvider = googleProvider;
	}

	/**
	 * Gets yahooOpenIdProvider.
	 * 
	 * @return yahooOpenIdProvider
	 */
	public OpenIdProviderProperties getYahooOpenIdProvider() {
		return yahooOpenIdProvider;
	}

	/**
	 * Sets yahooOpenIdProvider.
	 * 
	 * @param yahooOpenIdProvider
	 *            yahooOpenIdProvider
	 */
	public void setYahooOpenIdProvider(OpenIdProviderProperties yahooOpenIdProvider) {
		this.yahooOpenIdProvider = yahooOpenIdProvider;
	}
}
