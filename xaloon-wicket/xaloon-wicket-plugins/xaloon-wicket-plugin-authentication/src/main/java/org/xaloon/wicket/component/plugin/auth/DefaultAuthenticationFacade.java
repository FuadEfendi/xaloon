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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.core.api.security.external.AuthenticationToken;

/**
 * TODO manage in better way
 * 
 * @author vytautas r.
 */
@Named
public class DefaultAuthenticationFacade implements AuthenticationFacade {
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("linkedInExternalAuthenticationConsumer")
	private AuthenticationConsumer linkedInExternalAuthenticationConsumer;

	@Inject
	@Named("twitterExternalAuthenticationConsumer")
	private AuthenticationConsumer twitterExternalAuthenticationConsumer;

	@Inject
	@Named("facebookExternalAuthenticationConsumer")
	private AuthenticationConsumer facebookExternalAuthenticationConsumer;

	@Inject
	@Named("googleExternalAuthenticationConsumer")
	private AuthenticationConsumer googleExternalAuthenticationConsumer;

	@Inject
	@Named("openIdAuthenticationConsumer")
	private AuthenticationConsumer openIdAuthenticationConsumer;

	@Inject
	private PluginRegistry pluginRegistry;

	@Override
	public void beginConsumption(String loginType, String requestUrl) {
		if (AuthenticationFacade.LOGIN_TYPE_GOOGLE.equals(loginType)) {
			googleExternalAuthenticationConsumer.beginConsumption(requestUrl);
		} else if (AuthenticationFacade.LOGIN_TYPE_LINKEDIN.equals(loginType)) {
			linkedInExternalAuthenticationConsumer.beginConsumption(requestUrl);
		} else if (AuthenticationFacade.LOGIN_TYPE_TWITTER.equals(loginType)) {
			twitterExternalAuthenticationConsumer.beginConsumption(requestUrl);
		} else if (AuthenticationFacade.LOGIN_TYPE_FACEBOOK.equals(loginType)) {
			facebookExternalAuthenticationConsumer.beginConsumption(requestUrl);
		} else if (AuthenticationFacade.LOGIN_TYPE_YAHOO.equals(loginType)) {
			openIdAuthenticationConsumer.setOpenIdUrl(getExternalAuthenticationPluginBean().getYahooOpenIdProvider().getLink());
			openIdAuthenticationConsumer.beginConsumption(requestUrl);
		}
	}

	@Override
	public AuthenticationToken endConsumption(String loginType, String absoluteRequestURL) {
		if (AuthenticationFacade.LOGIN_TYPE_GOOGLE.equals(loginType)) {
			return googleExternalAuthenticationConsumer.endConsumption(loginType, absoluteRequestURL);
		} else if (AuthenticationFacade.LOGIN_TYPE_YAHOO.equals(loginType)) {
			return openIdAuthenticationConsumer.endConsumption(loginType, absoluteRequestURL);
		} else if (AuthenticationFacade.LOGIN_TYPE_LINKEDIN.equals(loginType)) {
			return linkedInExternalAuthenticationConsumer.endConsumption(loginType, absoluteRequestURL);
		} else if (AuthenticationFacade.LOGIN_TYPE_TWITTER.equals(loginType)) {
			return twitterExternalAuthenticationConsumer.endConsumption(loginType, absoluteRequestURL);
		} else if (AuthenticationFacade.LOGIN_TYPE_FACEBOOK.equals(loginType)) {
			return facebookExternalAuthenticationConsumer.endConsumption(loginType, absoluteRequestURL);
		}
		return null;
	}

	@Override
	public boolean isResponseToEndConsumption(Set<String> namedKeys) {
		return namedKeys.contains(AuthenticationConsumer.PARAM_OPENID_MODE) || namedKeys.contains(AuthenticationConsumer.PARAM_OAUTH_TOKEN) ||
			namedKeys.contains(AuthenticationConsumer.PARAM_AUTH_TOKEN) || namedKeys.contains(AuthenticationConsumer.PARAM_OAUTH_DENIED) ||
			namedKeys.contains("code");
	}

	private ExternalAuthenticationPluginBean getExternalAuthenticationPluginBean() {
		return pluginRegistry.getPluginBean(ExternalAuthenticationPlugin.class);
	}

	public boolean isPluginEnabled() {
		return pluginRegistry.isEnabled(ExternalAuthenticationPlugin.class);
	}

	@Override
	public boolean isEnabled(String loginType) {
		if (!isPluginEnabled()) {
			return false;
		}
		if (AuthenticationFacade.LOGIN_TYPE_GOOGLE.equals(loginType)) {
			return googleExternalAuthenticationConsumer.isEnabled();
		} else if (AuthenticationFacade.LOGIN_TYPE_LINKEDIN.equals(loginType)) {
			return linkedInExternalAuthenticationConsumer.isEnabled();
		} else if (AuthenticationFacade.LOGIN_TYPE_TWITTER.equals(loginType)) {
			return twitterExternalAuthenticationConsumer.isEnabled();
		} else if (AuthenticationFacade.LOGIN_TYPE_FACEBOOK.equals(loginType)) {
			return facebookExternalAuthenticationConsumer.isEnabled();
		} else if (AuthenticationFacade.LOGIN_TYPE_YAHOO.equals(loginType)) {
			return getExternalAuthenticationPluginBean().getYahooOpenIdProvider().isEnabled();
		}
		return false;
	}

	@Override
	public List<String> getAvailableProviderSet() {
		List<String> availableProviders = new ArrayList<String>();
		if (googleExternalAuthenticationConsumer.isEnabled()) {
			availableProviders.add(AuthenticationFacade.LOGIN_TYPE_GOOGLE);
		}
		if (getExternalAuthenticationPluginBean().getYahooOpenIdProvider().isEnabled()) {
			availableProviders.add(AuthenticationFacade.LOGIN_TYPE_YAHOO);
		}
		if (linkedInExternalAuthenticationConsumer.isEnabled()) {
			availableProviders.add(AuthenticationFacade.LOGIN_TYPE_LINKEDIN);
		}
		if (twitterExternalAuthenticationConsumer.isEnabled()) {
			availableProviders.add(AuthenticationFacade.LOGIN_TYPE_TWITTER);
		}
		if (facebookExternalAuthenticationConsumer.isEnabled()) {
			availableProviders.add(AuthenticationFacade.LOGIN_TYPE_FACEBOOK);
		}
		return availableProviders;
	}
}
