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
package org.xaloon.core.api.security.external;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


/**
 * @author vytautas r.
 */
public interface AuthenticationFacade extends Serializable {
	/**
	 * 
	 */
	String LOGIN_TYPE_GOOGLE = "google";

	/**
	 * 
	 */
	String LOGIN_TYPE_LINKEDIN = "linkedin";

	/**
	 * 
	 */
	String LOGIN_TYPE_TWITTER = "twitter";

	/**
	 * 
	 */
	String LOGIN_TYPE_FACEBOOK = "facebook";

	/**
	 * 
	 */
	String LOGIN_TYPE_YAHOO = "yahoo";

	/**
	 * @param loginType
	 * @param requestUrl
	 */
	void beginConsumption(String loginType, String requestUrl);

	/**
	 * @param loginType
	 * @param absoluteRequestURL
	 * @return external authentication token
	 */
	AuthenticationToken endConsumption(String loginType, String absoluteRequestURL);

	/**
	 * @param namedKeys
	 * @return true if parameters contain any specific key defined as external system response
	 */
	boolean isResponseToEndConsumption(Set<String> namedKeys);

	/**
	 * @param loginType
	 * @return true if authentication consumer is enabled
	 */
	boolean isEnabled(String loginType);

	/**
	 * @return true if external authentication plugin is enabled
	 */
	boolean isPluginEnabled();

	/**
	 * @return list of available provider set
	 */
	List<String> getAvailableProviderSet();
}
