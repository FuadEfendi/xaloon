package org.xaloon.wicket.component.plugin.auth.oauth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.request.flow.RedirectToUrlException;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.FacebookApi;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPluginBean;

/**
 * @author vytautas.r
 */
@Named("facebookExternalAuthenticationConsumer")
public class FacebookExternalAuthenticationConsumer extends OauthExternalAuthenticationConsumer {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FacebookExternalAuthenticationConsumer.class);

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
		authenticationAttributes.put("username", "name");
		authenticationAttributes.put("first_name", AuthenticationConsumer.PARAM_FIRST_NAME);
		authenticationAttributes.put("last_name", AuthenticationConsumer.PARAM_LAST_NAME);

		return authenticationAttributes;
	}

	@Override
	protected OAuthProviderProperties getCustomAuthenticationProvider(ExternalAuthenticationPluginBean pluginBean) {
		return pluginBean.getFacebookProvider();
	}


	@Override
	protected Class<? extends Api> getServiceProvider() {
		return FacebookApi.class;
	}

	@Override
	protected void throwAuthorizationFlow(OAuthService service) {
		throw new RedirectToUrlException(service.getAuthorizationUrl(null));
	}

	@Override
	protected String getVerificationValue(HttpServletRequest request) {
		return request.getParameter("code");
	}

	@Override
	protected String getAuthenticationResourceUrlToCheck() {
		return "https://graph.facebook.com/me";
	}

	@Override
	protected boolean isDataRequiredInXmlFormat() {
		return false;
	}


}
