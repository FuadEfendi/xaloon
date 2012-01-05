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
package org.xaloon.wicket.plugin.user;

import java.io.Serializable;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class RegistrationModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private String username;

	private String email;

	private String password;

	private String repeat_password;

	private Boolean agreement;

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return password once more time
	 */
	public String getRepeat_password() {
		return repeat_password;
	}

	/**
	 * @param repeat_password
	 */
	public void setRepeat_password(String repeat_password) {
		this.repeat_password = repeat_password;
	}

	/**
	 * @param agreement
	 */
	public void setAgreement(Boolean agreement) {
		this.agreement = agreement;
	}

	/**
	 * @return agreed with rules
	 */
	public Boolean getAgreement() {
		return agreement;
	}
}
