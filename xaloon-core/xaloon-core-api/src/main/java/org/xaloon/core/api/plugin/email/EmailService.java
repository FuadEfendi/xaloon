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
package org.xaloon.core.api.plugin.email;

import java.io.Serializable;

/**
 * @author vytautas r.
 */
public interface EmailService extends Serializable {
	/**
	 * Error key when email plugin is not properly configured
	 */
	String EMAIL_PROPERTIES_NOT_CONFIGURED = "EMAIL_PROPERTIES_NOT_CONFIGURED";

	/**
	 * @param emailContent
	 * @param fromEmail
	 * @param fromName
	 * @return true if email sent successfully
	 */
	boolean sendMailToSystem(String emailContent, String fromEmail, String fromName);

	/**
	 * @param emailContent
	 * @param subject
	 * @param toEmail
	 * @param toName
	 * @return true if email sent successfully
	 */
	boolean sendMailFromSystem(String emailContent, String subject, String toEmail, String toName);

	/**
	 * 
	 * @return true if email plugin is enabled
	 */
	boolean isEnabled();

	/**
	 * Returns email of the system administrator
	 * 
	 * @return system administrator's email
	 */
	String getSystemEmail();
}
