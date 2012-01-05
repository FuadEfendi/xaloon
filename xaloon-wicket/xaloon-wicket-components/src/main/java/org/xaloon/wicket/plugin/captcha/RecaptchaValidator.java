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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.request.IRequestParameters;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.resource.StringResourceLoader;

/**
 * @author vytautas r.
 */
public class RecaptchaValidator implements IFormValidator {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RecaptchaValidator.class);

	private static final String REMOTEIP = "remoteip";
	private static final String PRIVATEKEY = "privatekey";
	private static final String RESPONSE = "response";
	private static final String CHALLENGE = "challenge";

	private static final String RECAPTCHA_VALIDATION_MESSAGE = "RECAPTCHA_VALIDATION_MESSAGE";

	private static final String RECAPTCHA_RESPONSE_FIELD = "recaptcha_response_field";
	private static final String RECAPTCHA_CHALLENGE_FIELD = "recaptcha_challenge_field";

	@Inject
	private StringResourceLoader stringResourceLoader;

	private RecaptchaPluginBean recaptchaPluginBean;

	/**
	 * Construct.
	 * 
	 * @param recaptchaPluginBean
	 * @param recaptchaPanel
	 */
	public RecaptchaValidator(RecaptchaPluginBean recaptchaPluginBean) {
		this.recaptchaPluginBean = recaptchaPluginBean;
		Injector.get().inject(this);
	}

	@Override
	public FormComponent<?>[] getDependentFormComponents() {
		return null;
	}

	@Override
	public void validate(Form<?> form) {
		Request request = RequestCycle.get().getRequest();
		IRequestParameters requestParameters = request.getPostParameters();
		String recaptcha_challenge_field = requestParameters.getParameterValue(RECAPTCHA_CHALLENGE_FIELD).toString();
		String recaptcha_response_field = requestParameters.getParameterValue(RECAPTCHA_RESPONSE_FIELD).toString();
		String remoteIpAddress = ((ServletWebRequest)request).getContainerRequest().getRemoteAddr();

		if (!validate(remoteIpAddress, recaptcha_challenge_field, recaptcha_response_field)) {
			form.error(stringResourceLoader.getString(RecaptchaValidator.class, RECAPTCHA_VALIDATION_MESSAGE));
		}
	}

	private boolean validate(String remoteIpAddress, String recaptcha_challenge_field, String recaptcha_response_field) {
		HttpClient httpClient = new HttpClient();

		PostMethod post = new PostMethod(recaptchaPluginBean.getVerificationUrl());
		post.addParameter(PRIVATEKEY, recaptchaPluginBean.getPrivateKey());

		post.addParameter(REMOTEIP, remoteIpAddress);
		post.addParameter(CHALLENGE, recaptcha_challenge_field);
		post.addParameter(RESPONSE, recaptcha_response_field);

		try {
			int code = httpClient.executeMethod(post);
			if (code != HttpStatus.SC_OK) {
				throw new RuntimeException("Could not send request: " + post.getStatusLine());
			}
			String resp = readString(post.getResponseBodyAsStream());
			if (resp.toLowerCase().startsWith(Boolean.TRUE.toString().toLowerCase())) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error("Could not process Recaptcha!", e);
		}
		return false;
	}

	private String readString(InputStream is) {
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
		} catch (Exception e) {
			LOGGER.error("Error while reading response for Recaptcha!", e);
		}
		return sb.toString();
	}
}
