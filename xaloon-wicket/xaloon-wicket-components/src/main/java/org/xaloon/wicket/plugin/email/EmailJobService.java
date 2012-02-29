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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.plugin.email.EmailService;

/**
 * @author vytautas r.
 */
@Named("emailJobService")
public class EmailJobService implements ScheduledJobService<EmailJobParameters> {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmailJobService.class);

	@Inject
	@Named("emailService")
	private EmailService emailService;

	@Override
	public <V> V execute(EmailJobParameters jobParameters, boolean isScheduled) {
		if (jobParameters.isSendEmailToSystem()) {
			emailService.sendMailToSystem(jobParameters.getEmailContent(), jobParameters.getFromEmail(), jobParameters.getFromName());
		} else {
			emailService.sendMailFromSystem(jobParameters.getEmailContent(), jobParameters.getSubject(), jobParameters.getToEmail(),
				jobParameters.getToName());
		}
		return null;
	}
}
