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

import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.asynchronous.SchedulerServices;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.plugin.email.EmailFacade;
import org.xaloon.core.api.plugin.email.EmailService;

/**
 * @author vytautas r.
 */
@Named("emailFacade")
public class DefaultEmailFacade implements EmailFacade {
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("emailService")
	private EmailService emailService;

	@Inject
	@Named("emailJobService")
	private ScheduledJobService<EmailJobParameters> scheduledJobService;

	/**
	 * Manually injected service. This allows to check if exists {@link SchedulerServices} implementation.
	 */
	private SchedulerServices schedulerServices;

	private SchedulerServices getSchedulerServices() {
		if (schedulerServices == null) {
			schedulerServices = ServiceLocator.get().getInstance(SchedulerServices.class);
		}
		return schedulerServices;
	}

	@Override
	public boolean sendMailToSystem(String emailContent, String fromEmail, String fromName) {
		// Schedule and start asynchronous job to send email to system
		EmailJobParameters params = new EmailJobParameters();
		params.setSendEmailToSystem(true);
		params.setEmailContent(emailContent);
		params.setFromEmail(fromEmail);
		params.setFromName(fromName);

		getSchedulerServices().runAsynchronous(scheduledJobService, params);
		return true;
	}

	@Override
	public boolean sendMailFromSystem(String emailContent, String subject, String toEmail, String toName) {
		// Schedule and start asynchronous job to send email to system
		EmailJobParameters params = new EmailJobParameters();
		params.setEmailContent(emailContent);
		params.setSubject(subject);
		params.setToEmail(toEmail);
		params.setToName(toName);

		getSchedulerServices().runAsynchronous(scheduledJobService, params);
		return true;
	}

	@Override
	public boolean isEnabled() {
		return emailService.isEnabled();
	}

}
