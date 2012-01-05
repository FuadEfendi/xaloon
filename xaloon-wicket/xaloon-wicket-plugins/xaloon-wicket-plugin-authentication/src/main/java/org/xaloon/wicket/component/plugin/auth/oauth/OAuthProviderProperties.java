package org.xaloon.wicket.component.plugin.auth.oauth;

import org.apache.commons.lang.StringUtils;
import org.xaloon.wicket.component.plugin.auth.ProviderProperties;

/**
 * @author vytautas r.
 */
public class OAuthProviderProperties extends ProviderProperties {
	private static final long serialVersionUID = 1L;

	private String consumerKey;

	private String consumerSecret;

	private String requestTokenEndpointUrl;

	private String accessTokenEndpointUrl;

	private String authorizationWebsiteUrl;

	/**
	 * Construct.
	 * 
	 * @param loginType
	 * @param defaultRequestTokenUrl
	 * @param defaultAccessTokenUrl
	 * @param defaultAuthorizeUrl
	 */
	public OAuthProviderProperties(String loginType, String defaultRequestTokenUrl, String defaultAccessTokenUrl, String defaultAuthorizeUrl) {
		super(loginType);
		requestTokenEndpointUrl = defaultRequestTokenUrl;
		accessTokenEndpointUrl = defaultAccessTokenUrl;
		authorizationWebsiteUrl = defaultAuthorizeUrl;
	}

	/**
	 * Construct.
	 * 
	 * @param loginType
	 */
	public OAuthProviderProperties(String loginType) {
		super(loginType);
	}

	/**
	 * @return consumer key
	 */
	public String getConsumerKey() {
		return consumerKey;
	}

	/**
	 * @param consumerKey
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	/**
	 * @return consumer secret
	 */
	public String getConsumerSecret() {
		return consumerSecret;
	}

	/**
	 * @param consumerSecret
	 */
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
	}

	/**
	 * @return request token endpoint link
	 */
	public String getRequestTokenEndpointUrl() {
		return requestTokenEndpointUrl;
	}

	/**
	 * @param requestTokenEndpointUrl
	 */
	public void setRequestTokenEndpointUrl(String requestTokenEndpointUrl) {
		this.requestTokenEndpointUrl = requestTokenEndpointUrl;
	}

	/**
	 * @return access token endpoint link
	 */
	public String getAccessTokenEndpointUrl() {
		return accessTokenEndpointUrl;
	}

	/**
	 * @param accessTokenEndpointUrl
	 */
	public void setAccessTokenEndpointUrl(String accessTokenEndpointUrl) {
		this.accessTokenEndpointUrl = accessTokenEndpointUrl;
	}

	/**
	 * @return authorization website link
	 */
	public String getAuthorizationWebsiteUrl() {
		return authorizationWebsiteUrl;
	}

	/**
	 * @param authorizationWebsiteUrl
	 */
	public void setAuthorizationWebsiteUrl(String authorizationWebsiteUrl) {
		this.authorizationWebsiteUrl = authorizationWebsiteUrl;
	}

	/**
	 * @return true if consumer key is empty
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(consumerKey);
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && !isEmpty();
	}
}
