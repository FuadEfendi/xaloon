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
package org.xaloon.wicket.plugin.user.page;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.plugin.user.panel.ActivationPanel;

/**
 * @author vytautas r.
 */
@MountPage(value = "/activation/${" + ActivationPanel.ACTIVATION_KEY + "}", visible = false)
public class ActivationPage extends WebPage {
	private static final long serialVersionUID = 1L;

	@Inject
	private UserFacade userFacade;

	/**
	 * Construct.
	 * 
	 * @param pageParameters
	 */
	public ActivationPage(PageParameters pageParameters) {
		String activationKey = pageParameters.get(ActivationPanel.ACTIVATION_KEY).toOptionalString();
		if (!StringUtils.isEmpty(activationKey)) {
			if (!userFacade.activate(activationKey)) {
				getSession().error(getString(ActivationPanel.MESSAGE_ERROR_ACTIVATION));
				setResponsePage(ActivationNoParamPage.class);
			} else {
				getSession().info(getString(ActivationPanel.MESSAGE_INFO_ACTIVATED));
				setResponsePage(WebApplication.get().getHomePage());
			}
		} else {
			setResponsePage(ActivationNoParamPage.class);
		}
	}
}
