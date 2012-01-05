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

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.wicket.plugin.user.validator.CurrentPasswordValidator;
import org.xaloon.wicket.plugin.user.validator.PasswordStrengthValidator;
import org.xaloon.wicket.plugin.user.validator.PasswordValidator;

/**
 * change password of currently logged in user
 * 
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class ChangePasswordPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public ChangePasswordPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new FeedbackPanel("feedback-panel"));
		add(new ChangePasswordForm("generate-new-password"));
	}

	class ChangePasswordForm extends Form<Void> {
		private static final long serialVersionUID = 1L;

		private static final String PASSWORD_WAS_NOT_CHANGED = "PASSWORD_WAS_NOT_CHANGED";
		private static final String PASSWORD_CHANGED = "PASSWORD_CHANGED";


		private static final String OLD_PASSWORD = "old_password";
		private static final String NEW_PASSWORD = "new_password";
		private static final String REPEAT_NEW_PASSWORD = "repeat_new_password";

		private final ValueMap properties = new ValueMap();

		public ChangePasswordForm(String id) {
			super(id);

			// Add old password field
			PasswordTextField oldPasswordField = new PasswordTextField(OLD_PASSWORD, new PropertyModel<String>(properties, OLD_PASSWORD));
			add(oldPasswordField);
			oldPasswordField.add(new CurrentPasswordValidator());

			// Add new password field
			PasswordTextField newPasswordField = new PasswordTextField(NEW_PASSWORD, new PropertyModel<String>(properties, NEW_PASSWORD));
			newPasswordField.add(PasswordStrengthValidator.getInstance());
			add(newPasswordField);

			// Add repeat new password field
			PasswordTextField repeatNewPasswordField = new PasswordTextField(REPEAT_NEW_PASSWORD, new PropertyModel<String>(properties,
				REPEAT_NEW_PASSWORD));
			add(repeatNewPasswordField);
			repeatNewPasswordField.add(new PasswordValidator(newPasswordField));

		}

		@Override
		protected void onSubmit() {
			// Just change password here, because passwords should be validated by validators already
			if (userFacade.changePassword(securityFacade.getCurrentUsername(), getNewPassword())) {
				getSession().info(getString(PASSWORD_CHANGED));
			} else {
				getSession().error(getString(PASSWORD_WAS_NOT_CHANGED));
			}
			setResponsePage(getApplication().getHomePage());
		}

		private String getNewPassword() {
			return properties.getString(NEW_PASSWORD);
		}
	}
}
