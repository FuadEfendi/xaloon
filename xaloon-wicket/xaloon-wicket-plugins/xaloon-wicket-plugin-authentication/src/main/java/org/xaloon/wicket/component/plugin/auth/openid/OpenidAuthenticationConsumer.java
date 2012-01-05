package org.xaloon.wicket.component.plugin.auth.openid;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NotImplementedException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.http.WebRequest;
import org.openid4java.consumer.ConsumerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.openid.OpenID4JavaConsumer;
import org.springframework.security.openid.OpenIDConsumer;
import org.springframework.security.openid.OpenIDConsumerException;
import org.xaloon.core.api.security.external.AuthenticationAttribute;
import org.xaloon.core.api.security.external.AuthenticationConsumer;
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.wicket.component.plugin.auth.AbstractExternalAuthenticationConsumer;
import org.xaloon.wicket.component.plugin.auth.ExternalAuthenticationPluginBean;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
@Named("openIdAuthenticationConsumer")
public class OpenidAuthenticationConsumer extends AbstractExternalAuthenticationConsumer<OpenIdProviderProperties> {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(OpenidAuthenticationConsumer.class);

	private static final String CANCEL = "cancel";

	private static List<AuthenticationAttribute> attributes = new ArrayList<AuthenticationAttribute>();


	private String openIdUrl;

	private OpenIDConsumer consumer;

	static {
		attributes.add(new AuthenticationAttribute(PARAM_EMAIL, OpenIdAttributes.SCHEMA_EMAIL, null));
		attributes.add(new AuthenticationAttribute(PARAM_FIRST_NAME, OpenIdAttributes.SCHEMA_FIRST_NAME, null));
		attributes.add(new AuthenticationAttribute(PARAM_LAST_NAME, OpenIdAttributes.SCHEMA_LAST_NAME, null));
		attributes.add(new AuthenticationAttribute(PARAM_PICTURE_SMALL, OpenIdAttributes.SCHEMA_PICTURE_SMALL, null));
		attributes.add(new AuthenticationAttribute(PARAM_PICTURE_BIG, OpenIdAttributes.SCHEMA_PICTURE_BIG, null));
		attributes.add(new AuthenticationAttribute("scope",
			"http://docs.google.com/feeds/+http://spreadsheets.google.com/feeds/+http://www.opensocial.googleusercontent.com/api/people/", null));
		attributes.add(new AuthenticationAttribute("ext2", "http://specs.openid.net/extensions/oauth/1.0", null));
	}

	public void beginConsumption(String absoluteRequestURL) {

		try {
			HttpServletRequest request = (HttpServletRequest)((WebRequest)RequestCycle.get().getRequest()).getContainerRequest();
			String uriToRedirect = getConsumer().beginConsumption(request, openIdUrl, absoluteRequestURL, null);
			throw new RedirectToUrlException(uriToRedirect);
		} catch (ConsumerException e) {
			logger.error("Consumer error while starting consumption", e);
		} catch (OpenIDConsumerException e) {
			logger.error("OpenID error while starting consumption", e);
		}

	}

	private OpenIDConsumer getConsumer() throws ConsumerException {
		if (consumer != null) {
			return consumer;
		}
		consumer = new OpenID4JavaConsumer(attributes);
		return consumer;

	}

	public AuthenticationToken endConsumption(String loginType, String absoluteRequestURL) {
		HttpServletRequest request = (HttpServletRequest)((WebRequest)RequestCycle.get().getRequest()).getContainerRequest();
		AuthenticationToken token = null;
		if ((request.getParameter(AuthenticationConsumer.PARAM_OPENID_MODE) != null) &&
			CANCEL.equals(request.getParameter(AuthenticationConsumer.PARAM_OPENID_MODE))) {
			throw new RedirectToUrlException(UrlUtils.toAbsolutePath(WebApplication.get().getHomePage(), null));
		}
		try {
			AuthenticationToken openIDAuthenticationToken = getConsumer().endConsumption(request);
			// recreate with fixed username
			if (!openIDAuthenticationToken.isAuthenticated()) {
				token = new AuthenticationToken(openIDAuthenticationToken.getName(), openIDAuthenticationToken.getMessage());
			} else {
				token = new AuthenticationToken(openIDAuthenticationToken.getName(), openIDAuthenticationToken.getAttributes());
			}
			token.setLoginType(loginType);
		} catch (ConsumerException e) {
			logger.error("Consumer error while finishing consumption", e);
		} catch (OpenIDConsumerException e) {
			logger.error("OpenID error while starting consumption", e);
		}
		return token;

	}

	/**
	 * @param openIdUrl
	 */
	public void setOpenIdUrl(String openIdUrl) {
		this.openIdUrl = openIdUrl;
	}

	@Override
	public boolean isEnabled() {
		throw new NotImplementedException("This call should not be used!");
	}

	@Override
	protected OpenIdProviderProperties getCustomAuthenticationProvider(ExternalAuthenticationPluginBean pluginBean) {
		throw new NotImplementedException("This call should not be used!");
	}
}
