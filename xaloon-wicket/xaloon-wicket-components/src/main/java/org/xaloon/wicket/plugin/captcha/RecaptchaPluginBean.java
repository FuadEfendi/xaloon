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
package org.xaloon.wicket.plugin.captcha;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * @author vytautas r.
 */
public class RecaptchaPluginBean extends AbstractPluginBean {

	private static final String DEFAULT_RECAPTCHA_VERIFY_URL = "http://api-verify.recaptcha.net/verify";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String privateKey;

	private String publicKey;

	private String verificationUrl = DEFAULT_RECAPTCHA_VERIFY_URL;

	/**
	 * @return private recaptcha key
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	/**
	 * @return public recaptcha key
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return recaptcha verification link
	 */
	public String getVerificationUrl() {
		return verificationUrl;
	}

	/**
	 * @param verificationUrl
	 */
	public void setVerificationUrl(String verificationUrl) {
		this.verificationUrl = verificationUrl;
	}

	/**
	 * @return validates ir required fields are filled in
	 */
	@Override
	public boolean isValid() {
		return !StringUtils.isEmpty(getPrivateKey()) && !StringUtils.isEmpty(getPublicKey()) && !StringUtils.isEmpty(getVerificationUrl());
	}
}
