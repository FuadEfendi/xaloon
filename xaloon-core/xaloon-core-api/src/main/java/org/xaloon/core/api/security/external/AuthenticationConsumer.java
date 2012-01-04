package org.xaloon.core.api.security.external;

import java.io.Serializable;


/**
 * Facade is used to authenticate user via external system (openid, facebook, linkedin, etc)
 * 
 * @author vytautas r.
 * 
 */
public interface AuthenticationConsumer extends Serializable {
	/**
	 * 
	 */
	String PARAM_LAST_NAME = "last";

	/**
	 * 
	 */
	String PARAM_FIRST_NAME = "first";

	/**
	 * 
	 */
	String PARAM_EMAIL = "email";

	/**
	 * 
	 */
	String PARAM_EXTERNAL_USERNAME = "external_username";

	/**
	 * 
	 */
	String PARAM_LOGIN_TYPE = "login_type";

	/**
	 * 
	 */
	String PARAM_PICTURE_SMALL = "defaultImage";

	/**
	 * 
	 */
	String PARAM_PICTURE_BIG = "picture_big";

	/**
	 * 
	 */
	String PARAM_OAUTH_TOKEN = "oauth_token";

	/**
	 * 
	 */
	String PARAM_OPENID_MODE = "openid.mode";

	/**
	 * 
	 */
	String PARAM_OAUTH_DENIED = "denied";

	/**
	 * FACEBOOK auth token
	 */
	String PARAM_AUTH_TOKEN = "auth_token";

	/**
	 * start authentication
	 * 
	 * @param absoluteRequestURL
	 * 
	 */
	void beginConsumption(String absoluteRequestURL);

	/**
	 * finish authentication process
	 * 
	 * @param loginType
	 * @param absoluteRequestURL
	 * @return authentication token based on consumption result
	 * 
	 */
	AuthenticationToken endConsumption(String loginType, String absoluteRequestURL);


	/**
	 * Open ID URL to be redirected to if openID authentication is used
	 * 
	 * @param openIdUrl
	 */
	void setOpenIdUrl(String openIdUrl);

	/**
	 * @return true if authentication consumer is enabled
	 */
	boolean isEnabled();
}
