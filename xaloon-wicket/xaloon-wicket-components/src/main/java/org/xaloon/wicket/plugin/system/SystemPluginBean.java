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
package org.xaloon.wicket.plugin.system;

import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * Backbone properties of the system
 * 
 * @author vytautas r.
 */
public class SystemPluginBean extends AbstractPluginBean {
	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_SHORT_DATE_FORMAT = "dd/MM/yyyy";

	private static final String DEFAULT_LONG_DATE_FORMAT = "dd/MM/yyyy HH:mm";

	private boolean userRegistrationEnabled = true;

	private String shortDateFormat = DEFAULT_SHORT_DATE_FORMAT;

	private String longDateFormat = DEFAULT_LONG_DATE_FORMAT;

	private String temporaryFileLocation = "/tmp/";

	/**
	 * Gets temporaryFileLocation.
	 * 
	 * @return temporaryFileLocation
	 */
	public String getTemporaryFileLocation() {
		return temporaryFileLocation;
	}

	/**
	 * Sets temporaryFileLocation.
	 * 
	 * @param temporaryFileLocation
	 *            temporaryFileLocation
	 */
	public void setTemporaryFileLocation(String temporaryFileLocation) {
		this.temporaryFileLocation = temporaryFileLocation;
	}

	/**
	 * @return true if user registration is enabled
	 */
	public boolean isUserRegistrationEnabled() {
		return userRegistrationEnabled;
	}

	/**
	 * @param userRegistrationEnabled
	 */
	public void setUserRegistrationEnabled(boolean userRegistrationEnabled) {
		this.userRegistrationEnabled = userRegistrationEnabled;
	}

	/**
	 * @return
	 */
	public String getShortDateFormat() {
		return shortDateFormat;
	}

	/**
	 * @param shortDateFormat
	 */
	public void setShortDateFormat(String shortDateFormat) {
		this.shortDateFormat = shortDateFormat;
	}

	/**
	 * @return
	 */
	public String getLongDateFormat() {
		return longDateFormat;
	}

	/**
	 * @param longDateFormat
	 */
	public void setLongDateFormat(String longDateFormat) {
		this.longDateFormat = longDateFormat;
	}
}
