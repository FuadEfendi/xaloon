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
package org.xaloon.core.api.plugin.comment;

import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * Configuration bean for commenting plugin
 * 
 * @author vytautas r.
 */
public class CommentPluginBean extends AbstractPluginBean {
	private static final long serialVersionUID = 1L;

	private boolean websiteVisible;

	private boolean applyByAdministrator = true;

	private boolean allowPostForAnonymous;

	private boolean sendEmail = true;

	private boolean enableCaptcha = true;

	/**
	 * @return true if website is publicly available
	 */
	public boolean isWebsiteVisible() {
		return websiteVisible;
	}

	/**
	 * @param websiteVisible
	 */
	public void setWebsiteVisible(boolean websiteVisible) {
		this.websiteVisible = websiteVisible;
	}

	/**
	 * @return true if administrator needs to apply comments
	 */
	public boolean isApplyByAdministrator() {
		return applyByAdministrator;
	}

	/**
	 * @param applyByAdministrator
	 */
	public void setApplyByAdministrator(boolean applyByAdministrator) {
		this.applyByAdministrator = applyByAdministrator;
	}

	/**
	 * @return true if anonymous posts are allowed
	 */
	public boolean isAllowPostForAnonymous() {
		return allowPostForAnonymous;
	}

	/**
	 * @param allowPostForAnonymous
	 */
	public void setAllowPostForAnonymous(boolean allowPostForAnonymous) {
		this.allowPostForAnonymous = allowPostForAnonymous;
	}

	/**
	 * @return true if email should be sent to administrator
	 */
	public boolean isSendEmail() {
		return sendEmail;
	}

	/**
	 * @param sendEmail
	 */
	public void setSendEmail(boolean sendEmail) {
		this.sendEmail = sendEmail;
	}

	/**
	 * @return true if captcha should be used by commenting plugin
	 */
	public boolean isEnableCaptcha() {
		return enableCaptcha;
	}

	/**
	 * @param enableCaptcha
	 */
	public void setEnableCaptcha(boolean enableCaptcha) {
		this.enableCaptcha = enableCaptcha;
	}
}
