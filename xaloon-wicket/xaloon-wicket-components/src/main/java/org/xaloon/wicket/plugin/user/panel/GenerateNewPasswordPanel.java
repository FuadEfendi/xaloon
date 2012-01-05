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
package org.xaloon.wicket.plugin.user.panel;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.xaloon.core.api.user.UserFacade;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class GenerateNewPasswordPanel extends Panel {

	private static final long serialVersionUID = 1L;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public GenerateNewPasswordPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new FeedbackPanel("feedback-panel"));
		add(new GenerateNewPasswordForm("generate-new-password"));
	}

	class GenerateNewPasswordForm extends StatelessForm<Void> {
		private static final String EMAIL_NOT_FOUND = "EMAIL_NOT_FOUND";
		private static final String PASSWORD_EMAIL_SENT = "PASSWORD_EMAIL_SENT";

		private static final long serialVersionUID = 1L;

		private static final String EMAIL = "email";

		private final ValueMap properties = new ValueMap();


		public GenerateNewPasswordForm(String id) {
			super(id);

			// Add old password field
			RequiredTextField<String> emailField = new RequiredTextField<String>(EMAIL, new PropertyModel<String>(properties, EMAIL));
			add(emailField);

			emailField.add(EmailAddressValidator.getInstance());
		}

		@Override
		protected void onSubmit() {
			String errorCode = userFacade.sendNewPassword(getEmail());

			if (StringUtils.isEmpty(errorCode)) {
				getSession().info(getString(PASSWORD_EMAIL_SENT));
			} else {
				getSession().warn(getString(errorCode));
			}
			setResponsePage(getApplication().getHomePage());
		}

		public String getEmail() {
			return properties.getString(EMAIL);
		}

	}

}
