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
package org.xaloon.wicket.plugin.email;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.SimpleEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.plugin.email.EmailPluginBean;
import org.xaloon.core.api.plugin.email.EmailService;

/**
 * @author vytautas r.
 */
@Named("emailService")
public class DefaultEmailService implements EmailService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEmailService.class);

	@Inject
	private PluginRegistry pluginRegistry;

	@Override
	public boolean sendMailToSystem(String emailContent, String fromEmail, String fromName) {
		if (!isEnabled()) {
			return false;
		}
		EmailPluginBean emailPluginBean = getPluginBean();
		String subject = emailPluginBean.getFromSubject();
		String toEmail = emailPluginBean.getToEmail();
		String toName = emailPluginBean.getToTitle();
		return sendEmailInternal(emailContent, subject, fromEmail, fromName, toEmail, toName);
	}

	private boolean sendEmailInternal(String emailContent, String subject, String fromEmail, String fromName, String toEmail, String toName) {
		EmailPluginBean emailPluginBean = getPluginBean();

		SimpleEmail email = new SimpleEmail();
		email.setDebug(emailPluginBean.isDebug());
		email.setHostName(emailPluginBean.getHost());
		email.setSmtpPort(emailPluginBean.getPort());
		if (emailPluginBean.isRequiresAuthentication()) {
			email.setAuthentication(emailPluginBean.getUsername(), emailPluginBean.getPassword());
		}
		email.setCharset(emailPluginBean.getCharset());
		try {
			email.setTLS(emailPluginBean.isStartTLS());
			email.setSSL(emailPluginBean.isStartTLS());
			email.addTo(toEmail, toName);
			email.setFrom(fromEmail, fromName);
			email.setSubject(subject);
			email.setContent(emailContent, "text/html; charset=" + emailPluginBean.getCharset());
			email.send();
		} catch (Exception e) {
			LOGGER.error("Message could not be sent!", e);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendMailFromSystem(String emailContent, String subject, String toEmail, String toName) {
		if (!isEnabled()) {
			return false;
		}

		EmailPluginBean emailPluginBean = getPluginBean();
		if (StringUtils.isEmpty(subject)) {
			subject = emailPluginBean.getToSubject();
		}
		String fromEmail = emailPluginBean.getFromEmail();
		String fromName = emailPluginBean.getFromTitle();
		return sendEmailInternal(emailContent, subject, fromEmail, fromName, toEmail, toName);
	}

	@Override
	public boolean isEnabled() {
		return pluginRegistry.isEnabled(EmailPlugin.class) && !getPluginBean().isEmpty();
	}

	private EmailPluginBean getPluginBean() {
		return pluginRegistry.getPluginBean(EmailPlugin.class);
	}

	@Override
	public String getSystemEmail() {
		return getPluginBean().getToEmail();
	}
}
