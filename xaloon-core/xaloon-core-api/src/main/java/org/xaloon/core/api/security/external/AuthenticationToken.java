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


/**
 * @author vytautas r.
 */
public class AuthenticationToken implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;

	private final String message;

	private final List<AuthenticationAttribute> attributes;

	private boolean authenticated = false;

	private String loginType;

	private Object details;

	/**
	 * Construct.
	 * 
	 * @param name
	 * @param attributes
	 * 
	 */
	public AuthenticationToken(String name, List<AuthenticationAttribute> attributes) {
		this.name = name;
		this.attributes = attributes;
		authenticated = true;
		message = null;
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 * @param message
	 * @param attributes
	 * 
	 */
	public AuthenticationToken(String name, String message) {
		this.name = name;
		this.message = message;
		attributes = null;
		authenticated = false;
	}

	/**
	 * @return login type
	 */
	public String getLoginType() {
		return loginType;
	}

	/**
	 * Sets loginType.
	 * 
	 * @param loginType
	 *            loginType
	 */
	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return attributes
	 */
	public List<AuthenticationAttribute> getAttributes() {
		return attributes;
	}

	/**
	 * @return true if authenticated
	 */
	public boolean isAuthenticated() {
		return authenticated;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets details.
	 * 
	 * @return details
	 */
	public Object getDetails() {
		return details;
	}

	/**
	 * Sets details.
	 * 
	 * @param details
	 *            details
	 */
	public void setDetails(Object details) {
		this.details = details;
	}
}
