package org.xaloon.wicket.component.plugin.auth.oauth;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.WebRequest;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.Api;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xaloon.core.api.security.external.AuthenticationAttribute;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.wicket.component.plugin.auth.AbstractExternalAuthenticationConsumer;
import org.xaloon.wicket.util.UrlUtils;
import org.xml.sax.SAXException;

/**
 * @author vytautas.r
 */
public abstract class OauthExternalAuthenticationConsumer extends AbstractExternalAuthenticationConsumer<OAuthProviderProperties> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(OauthExternalAuthenticationConsumer.class);

	protected static final MetaDataKey<Token> METADATAKEY_REQUEST_TOKEN = new MetaDataKey<Token>() {
		private static final long serialVersionUID = 1L;
	};
	protected static final String AUTHENTICATION_PROVIDER_NOT_REGISTERED = "AUTHENTICATION_PROVIDER_NOT_REGISTERED";

	// protected static final String AUTH_TOKEN_PARAMETER = "oauth_token";
	protected static final String AUTH_TOKEN_VERIFIER_PARAMETER = "oauth_verifier";

	protected static final String TAG_TEXT_END = "</";

	protected static final String TAG_TEXT_START = ">";

	protected abstract Class<? extends Api> getServiceProvider();

	protected abstract void throwAuthorizationFlow(OAuthService service);

	protected abstract String getAuthenticationResourceUrlToCheck();

	protected abstract String getVerificationValue(HttpServletRequest request);

	@Override
	public void setOpenIdUrl(String openIdUrl) {
		throw new RuntimeException("not supported!");
	}

	public boolean isEnabled() {
		return getAuthenticationProviderProperties().isEnabled();
	}

	public void beginConsumption(String absoluteRequestURL) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Got security backlink: %s", absoluteRequestURL));
		}
		// Get OAuth properties
		OAuthProviderProperties authenticationProviderProperties = getAuthenticationProviderProperties();
		if (authenticationProviderProperties == null) {
			WebSession.get().error(stringResourceLoader.getString(this.getClass(), AUTHENTICATION_PROVIDER_NOT_REGISTERED));
			throw new RedirectToUrlException(UrlUtils.toAbsolutePath(WebApplication.get().getHomePage(), null));
		}

		// Create service
		OAuthService service = buildService(absoluteRequestURL, authenticationProviderProperties, 1);


		throwAuthorizationFlow(service);
	}


	private OAuthService buildService(OAuthProviderProperties authenticationProviderProperties) {
		return buildService(null, authenticationProviderProperties, 2);
	}

	private OAuthService buildService(String absoluteRequestURL, OAuthProviderProperties authenticationProviderProperties, int phase) {
		ServiceBuilder serviceBuilder = new ServiceBuilder().provider(getServiceProvider())
			.apiKey(authenticationProviderProperties.getConsumerKey())
			.apiSecret(authenticationProviderProperties.getConsumerSecret());
		init(serviceBuilder, absoluteRequestURL, phase);
		OAuthService service = serviceBuilder.build();
		return service;
	}


	protected String processRequestTokenEndpointUrl(String requestTokenEndpointUrl) {
		return requestTokenEndpointUrl;
	}

	public AuthenticationToken endConsumption(String loginType, String absoluteRequestURL) {
		try {
			HttpServletRequest request = (HttpServletRequest)((WebRequest)RequestCycle.get().getRequest()).getContainerRequest();
			OAuthProviderProperties authenticationProviderProperties = getAuthenticationProviderProperties();

			OAuthService service = buildService(absoluteRequestURL, authenticationProviderProperties, 2);

			String verificationValue = getVerificationValue(request);
			Verifier verifier = new Verifier(verificationValue);

			// Get access token providing request token and verifier
			Token accessToken = service.getAccessToken(getRequestToken(), verifier);
			afterAccessTokenGot(accessToken);

			// Create and sign request to get user data
			OAuthRequest oAuthRequest = new OAuthRequest(Verb.GET, getAuthenticationResourceUrlToCheck());
			service.signRequest(accessToken, oAuthRequest);
			additionalActionsToTake(oAuthRequest);
			Response response = oAuthRequest.send();

			// Get and parse the response body
			String responseBody = response.getBody();

			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace(responseBody);
			}
			return parseResponse(loginType, responseBody);

		} catch (Exception e) {
			LOGGER.error("Exception occured when parsing response!", e);
		}
		return null;
	}

	private AuthenticationToken parseResponse(String loginType, String responseBody) throws SAXException, IOException, ParserConfigurationException,
		ParseException {
		List<AuthenticationAttribute> tokenAttributes = new ArrayList<AuthenticationAttribute>();
		AuthenticationToken token = new AuthenticationToken(null, tokenAttributes);
		token.setLoginType(loginType);
		if (isDataRequiredInXmlFormat()) {
			// Parse xml and fill details
			Element xmlElement = getXmlFromString(responseBody);
			if (xmlElement != null) {
				fillAuthenticationAttributes(token, xmlElement);
			}
		} else {
			// assume that data is json based
			JSONObject jsonObject = (JSONObject)new JSONParser().parse(responseBody);
			fillAuthenticationAttributes(token, jsonObject);
		}
		return token;
	}

	private Element getXmlFromString(String responseBody) throws SAXException, IOException, ParserConfigurationException {
		Element data = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder()
			.parse(new ByteArrayInputStream(responseBody.getBytes()))
			.getDocumentElement();
		return data;
	}

	private void init(ServiceBuilder serviceBuilder, String absoluteRequestURL, int phase) {
		if (!StringUtils.isEmpty(absoluteRequestURL)) {
			serviceBuilder.callback(absoluteRequestURL);
		}
		init(serviceBuilder, phase);
	}

	protected void afterAccessTokenGot(Token accessToken) {
	}

	/**
	 * Fill additional attributes of authentication token
	 * 
	 * @param token
	 * @param xmlElement
	 * 
	 */
	protected void fillAuthenticationAttributes(AuthenticationToken token, Element xmlElement) {
		if (xmlElement == null) {
			return;
		}
		NodeList nodes = xmlElement.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			short nodeType = node.getNodeType();
			if (nodeType == 3) {
				continue;
			}
			Element element = (Element)nodes.item(i);

			String attributeName = getAuthenticationAttributes().get(element.getNodeName());
			if (!StringUtils.isEmpty(attributeName)) {
				if (attributeName.equals("name")) {
					token.setName(element.getTextContent().toLowerCase());
				}
				if (attributeName.equals(AuthenticationConsumer.PARAM_LAST_NAME)) {
					String lastName = element.getTextContent().toUpperCase();
					token.getAttributes().add(new AuthenticationAttribute(attributeName, lastName));
				} else {
					token.getAttributes().add(new AuthenticationAttribute(attributeName, element.getTextContent()));
				}
			}
		}
	}

	protected void fillAuthenticationAttributes(AuthenticationToken token, JSONObject jsonObject) {
		if (jsonObject == null) {
			return;
		}
		Map<String, String> authenticationAttributes = getAuthenticationAttributes();

		for (Map.Entry<String, String> entry : authenticationAttributes.entrySet()) {
			if (jsonObject.containsKey(entry.getKey())) {
				String value = (String)jsonObject.get(entry.getKey());
				if (!StringUtils.isEmpty(value)) {
					if (entry.getValue().equals("name")) {
						token.setName(UrlUtil.encode(value).toLowerCase());
					}
					if (entry.getValue().equals(AuthenticationConsumer.PARAM_LAST_NAME)) {
						String lastName = value.toUpperCase();
						token.getAttributes().add(new AuthenticationAttribute(entry.getValue(), lastName));
					} else {
						token.getAttributes().add(new AuthenticationAttribute(entry.getValue(), value));
					}
				}
			}
		}
	}

	/**
	 * Methods which may be overriden
	 */

	/**
	 * 
	 * @return attributes to fill
	 */
	protected Map<String, String> getAuthenticationAttributes() {
		return new HashMap<String, String>();
	}

	protected void init(ServiceBuilder serviceBuilder, int phase) {
	}

	protected Token getRequestToken() {
		return null;
	}

	protected void additionalActionsToTake(OAuthRequest oAuthRequest) {
	}

	protected boolean isDataRequiredInXmlFormat() {
		return true;
	}
}
