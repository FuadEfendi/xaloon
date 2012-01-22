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
package org.xaloon.core.impl.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.date.DateService;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;

/**
 * @author vytautas r.
 */
@Named
public class DefaultDateService implements DateService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	private PluginRegistry registry;

	@Override
	public DateFormat getShortDateFormat() {
		SystemPluginBean systemPluginBean = registry.getPluginBean(SystemPlugin.class);
		final DateFormat dateFormat = new SimpleDateFormat(systemPluginBean.getShortDateFormat());

		// Set user timezone if present
		TimeZone timeZone = getCurrentUserTimeZone();
		if (timeZone != null) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat;
	}

	@Override
	public DateFormat getLongDateFormat() {
		SystemPluginBean systemPluginBean = registry.getPluginBean(SystemPlugin.class);
		final DateFormat dateFormat = new SimpleDateFormat(systemPluginBean.getLongDateFormat());
		// Set user timezone if present
		TimeZone timeZone = getCurrentUserTimeZone();
		if (timeZone != null) {
			dateFormat.setTimeZone(timeZone);
		}
		return dateFormat;
	}

	private TimeZone getCurrentUserTimeZone() {
		// Get current user
		User currentUser = securityFacade.getCurrentUser();

		if (currentUser != null) {
			String userTimezone = currentUser.getTimezone();
			if (!StringUtils.isEmpty(userTimezone)) {
				return TimeZone.getTimeZone(userTimezone);
			}
		}
		return null;
	}
}
