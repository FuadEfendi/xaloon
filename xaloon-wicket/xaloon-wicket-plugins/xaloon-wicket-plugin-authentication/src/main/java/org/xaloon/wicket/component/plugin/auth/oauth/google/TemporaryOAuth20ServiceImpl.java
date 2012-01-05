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
package org.xaloon.wicket.component.plugin.auth.oauth.google;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;

/**
 * This class will be replaced with scribe API when scribe will support Google OAuth 2.0
 * 
 * @author vytautas r.
 */
public class TemporaryOAuth20ServiceImpl extends org.scribe.oauth.OAuth20ServiceImpl {

	private static final String GRANT_TYPE = "grant_type";

	// GrantType
	private static final String AUTHORIZATION_CODE = "authorization_code";

	private final OAuthConfig config;
	private final DefaultApi20 api;

	/**
	 * Construct.
	 * 
	 * @param api
	 * @param config
	 */
	public TemporaryOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config) {
		super(api, config);
		this.config = config;
		this.api = api;
	}

	@Override
	public Token getAccessToken(Token requestToken, Verifier verifier) {
		TemporaryOAuthRequest request = new TemporaryOAuthRequest(api.getAccessTokenVerb(), api.getAccessTokenEndpoint());
		request.addQuerystringParameter(OAuthConstants.CLIENT_ID, config.getApiKey());
		request.addQuerystringParameter(OAuthConstants.CLIENT_SECRET, config.getApiSecret());
		// In case of Client Credentials, verfier is not required
		if (verifier != null) {
			request.addQuerystringParameter(OAuthConstants.CODE, verifier.getValue());
		}
		request.addQuerystringParameter(OAuthConstants.REDIRECT_URI, config.getCallback());
		if (config.hasScope()) {
			request.addQuerystringParameter(OAuthConstants.SCOPE, config.getScope());
		}
		request.addQuerystringParameter(GRANT_TYPE, AUTHORIZATION_CODE);
		Response response = request.send();
		return api.getAccessTokenExtractor().extract(response.getBody());
	}
}
