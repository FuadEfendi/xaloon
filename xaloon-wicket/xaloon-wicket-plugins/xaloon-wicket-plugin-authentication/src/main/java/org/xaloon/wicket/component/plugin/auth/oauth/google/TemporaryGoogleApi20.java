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
import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 * This class will be replaced with scribe API when scribe will support Google OAuth 2.0
 * 
 * @author vytautas r.
 */
public class TemporaryGoogleApi20 extends DefaultApi20 {
	private static final String AUTHORIZATION_URL = "https://accounts.google.com/o/oauth2/auth?client_id=%s&redirect_uri=%s&scope=%s&response_type=code";

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(AUTHORIZATION_URL, config.getApiKey(), config.getCallback(), config.getScope());
	}

	@Override
	public String getAccessTokenEndpoint() {
		return "https://accounts.google.com/o/oauth2/token";
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new TemporaryJsonTokenExtractor();
	}

	@Override
	public OAuthService createService(OAuthConfig config) {
		return new org.xaloon.wicket.component.plugin.auth.oauth.google.TemporaryOAuth20ServiceImpl(this, config);
	}
}
