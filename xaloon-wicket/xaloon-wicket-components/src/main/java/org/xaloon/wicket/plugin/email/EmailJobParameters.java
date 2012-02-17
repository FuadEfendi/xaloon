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

import org.xaloon.core.api.asynchronous.JobParameters;

/**
 * @author vytautas r.
 */
public class EmailJobParameters implements JobParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean sendEmailToSystem;

	private String subject;

	private String fromEmail;

	private String fromName;

	private String toEmail;

	private String toName;

	private String emailContent;

	/**
	 * Gets sendEmailToSystem.
	 * 
	 * @return sendEmailToSystem
	 */
	public boolean isSendEmailToSystem() {
		return sendEmailToSystem;
	}

	/**
	 * Sets sendEmailToSystem.
	 * 
	 * @param sendEmailToSystem
	 *            sendEmailToSystem
	 */
	public void setSendEmailToSystem(boolean sendEmailToSystem) {
		this.sendEmailToSystem = sendEmailToSystem;
	}

	/**
	 * Gets subject.
	 * 
	 * @return subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Sets subject.
	 * 
	 * @param subject
	 *            subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Gets fromEmail.
	 * 
	 * @return fromEmail
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * Sets fromEmail.
	 * 
	 * @param fromEmail
	 *            fromEmail
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * Gets fromName.
	 * 
	 * @return fromName
	 */
	public String getFromName() {
		return fromName;
	}

	/**
	 * Sets fromName.
	 * 
	 * @param fromName
	 *            fromName
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	/**
	 * Gets toEmail.
	 * 
	 * @return toEmail
	 */
	public String getToEmail() {
		return toEmail;
	}

	/**
	 * Sets toEmail.
	 * 
	 * @param toEmail
	 *            toEmail
	 */
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	/**
	 * Gets toName.
	 * 
	 * @return toName
	 */
	public String getToName() {
		return toName;
	}

	/**
	 * Sets toName.
	 * 
	 * @param toName
	 *            toName
	 */
	public void setToName(String toName) {
		this.toName = toName;
	}

	/**
	 * Gets emailContent.
	 * 
	 * @return emailContent
	 */
	public String getEmailContent() {
		return emailContent;
	}

	/**
	 * Sets emailContent.
	 * 
	 * @param emailContent
	 *            emailContent
	 */
	public void setEmailContent(String emailContent) {
		this.emailContent = emailContent;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
