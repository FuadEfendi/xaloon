package org.xaloon.wicket.component.plugin.auth.oauth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Session;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPluginBean;

/**
 * @author vytautas r.
 */
@Named("linkedInExternalAuthenticationConsumer")
public class LinkedInExternalAuthenticationConsumer extends OauthExternalAuthenticationConsumer {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(LinkedInExternalAuthenticationConsumer.class);

	private static Map<String, String> authenticationAttributes;

	private static final String API_LINKEDIN_PEOPLE_URL = "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,picture-url)";

	@Override
	protected Map<String, String> getAuthenticationAttributes() {
		if (authenticationAttributes == null) {
			authenticationAttributes = getAuthenticationAttributesInstance();
		}
		return authenticationAttributes;
	}

	private Map<String, String> getAuthenticationAttributesInstance() {
		Map<String, String> authenticationAttributes = new HashMap<String, String>();
		authenticationAttributes.put("id", "name");
		authenticationAttributes.put("first-name", AuthenticationConsumer.PARAM_FIRST_NAME);
		authenticationAttributes.put("last-name", AuthenticationConsumer.PARAM_LAST_NAME);
		authenticationAttributes.put("picture-url", AuthenticationConsumer.PARAM_PICTURE_SMALL);
		authenticationAttributes.put("email", AuthenticationConsumer.PARAM_EMAIL);

		return authenticationAttributes;
	}

	@Override
	public OAuthProviderProperties getCustomAuthenticationProvider(ExternalAuthenticationPluginBean pluginBean) {
		return pluginBean.getLinkedInProvider();
	}

	@Override
	protected Class<? extends Api> getServiceProvider() {
		return LinkedInApi.class;
	}


	@Override
	protected void throwAuthorizationFlow(OAuthService service) {
		Token requestToken = service.getRequestToken();
		Session.get().setMetaData(METADATAKEY_REQUEST_TOKEN, requestToken);
		throw new RedirectToUrlException(service.getAuthorizationUrl(requestToken));
	}


	@Override
	protected Token getRequestToken() {
		return Session.get().getMetaData(METADATAKEY_REQUEST_TOKEN);
	}

	@Override
	protected String getVerificationValue(HttpServletRequest request) {
		return request.getParameter(AUTH_TOKEN_VERIFIER_PARAMETER);
	}

	@Override
	protected String getAuthenticationResourceUrlToCheck() {
		return "http://api.linkedin.com/v1/people/~:(id,first-name,last-name,industry,picture-url)";
	}
}
