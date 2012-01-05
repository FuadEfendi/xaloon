package org.xaloon.wicket.component.plugin.auth.oauth;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.Session;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Token;
import org.scribe.oauth.OAuthService;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPluginBean;

/**
 * @author vytautas.r
 */
@Named("twitterExternalAuthenticationConsumer")
public class TwitterExternalAuthenticationConsumer extends OauthExternalAuthenticationConsumer {
	private static final long serialVersionUID = 1L;

	private static final String TWITTER_ACCOUNT_VERIFY_CREDENTIALS_XML = "https://twitter.com/account/verify_credentials.xml";

	private static Map<String, String> authenticationAttributes;

	@Override
	protected OAuthProviderProperties getCustomAuthenticationProvider(ExternalAuthenticationPluginBean pluginBean) {
		return pluginBean.getTwitterProvider();
	}

	@Override
	protected Map<String, String> getAuthenticationAttributes() {
		if (authenticationAttributes == null) {
			authenticationAttributes = getAuthenticationAttributesInstance();
		}
		return authenticationAttributes;
	}

	private Map<String, String> getAuthenticationAttributesInstance() {
		Map<String, String> authenticationAttributes = new HashMap<String, String>();
		authenticationAttributes.put("screen_name", "name");
		authenticationAttributes.put("profile_image_url", AuthenticationConsumer.PARAM_PICTURE_SMALL);

		return authenticationAttributes;
	}

	@Override
	protected Class<? extends Api> getServiceProvider() {
		return TwitterApi.class;
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
		return "http://api.twitter.com/1/account/verify_credentials.xml";
	}
}
