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
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.xaloon.core.api.plugin.email.EmailFacade;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.captcha.RecaptchaPanel;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;
import org.xaloon.wicket.plugin.user.RegistrationModel;
import org.xaloon.wicket.plugin.user.validator.AgreementValidator;
import org.xaloon.wicket.plugin.user.validator.EmailUsageValidator;
import org.xaloon.wicket.plugin.user.validator.PasswordStrengthValidator;
import org.xaloon.wicket.plugin.user.validator.PasswordValidator;
import org.xaloon.wicket.plugin.user.validator.UsernamePatternValidator;
import org.xaloon.wicket.plugin.user.validator.UsernameValidator;

/**
 * @author vytautas r.
 * @param <T>
 *            extendable registration model
 * @param <K>
 *            User data object
 */
public class RegistrationPanel<T extends RegistrationModel, K extends User> extends AbstractPluginPanel<SystemPluginBean, SystemPlugin> {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final String AGREEMENT_MESSAGE = "agreement-message";

	private static final String EMAIL_SENT = "EMAIL_SENT";

	private static final String USER_ALREADY_EXSITS = "USER_ALREADY_EXSITS";

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	@Inject
	private EmailFacade emailFacade;

	@Inject
	private StringResourceLoader stringResourceLoader;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public RegistrationPanel(String id, IModel<T> model) {
		super(id, model);
		if (!getPluginBean().isUserRegistrationEnabled()) {
			setResponsePage(getApplication().getHomePage());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected void onInitialize(SystemPlugin plugin, SystemPluginBean pluginBean) {
		IModel<T> model = new CompoundPropertyModel(getDefaultModel());
		Form<T> registrationForm = new StatelessForm<T>("register-form", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				onFormSubmit(getModelObject());
			}
		};
		add(registrationForm);

		/** add form feedback panel */
		add(new ComponentFeedbackPanel("feedback-panel", registrationForm));

		PasswordTextField passwordField = createPasswordField(registrationForm);

		createUsernameField(registrationForm);
		createEmailField(registrationForm);
		createRepeatPasswordField(registrationForm, passwordField);
		createAgreementPanel(registrationForm);
		createCaptchaPanel(registrationForm);

		registrationForm.add(createAgreementMessageLabel());
		registrationForm.add(new EmailPluginEnabledValidator());

		onFormInitialize(registrationForm);
	}

	private void createCaptchaPanel(Form<T> registrationForm) {
		// Add captcha panel
		RecaptchaPanel recaptchaPanel = new RecaptchaPanel("recaptcha-panel", registrationForm);
		registrationForm.add(recaptchaPanel);
	}

	@SuppressWarnings("unchecked")
	private void onFormSubmit(T registration) {
		K user = (K)getUserFacade().newUser();
		user.setEmail(registration.getEmail());
		user.setUsername(registration.getUsername());
		user.setFirstName(user.getUsername());
		onBeforeRegistration(user, registration);
		// save
		String activationKey = getUserFacade().registerUser(user, registration.getPassword(), false, null);
		if (!StringUtils.isEmpty(activationKey)) {
			getSession().info(EMAIL_SENT);
		} else {
			getSession().error(USER_ALREADY_EXSITS);
		}

		// redirect
		setResponsePage(getApplication().getHomePage());
	}

	private Component createAgreementMessageLabel() {
		// Add agreement message
		String agreementMessage = getParent().getString(AGREEMENT_MESSAGE);
		return new Label(AGREEMENT_MESSAGE, new Model<String>(agreementMessage)).setEscapeModelStrings(false);
	}

	private void createAgreementPanel(Form<T> registrationForm) {
		// Add agreement field
		CheckBox agreement = new CheckBox("agreement");
		agreement.add(AgreementValidator.getInstance());
		registrationForm.add(agreement);
		registrationForm.add(new ComponentFeedbackPanel("agreement_fp", agreement));
	}

	private void createRepeatPasswordField(Form<T> registrationForm, PasswordTextField passwordField) {
		// Add repeat password field
		PasswordTextField repeatPasswordField = new PasswordTextField("repeat_password");
		repeatPasswordField.add(new PasswordValidator(passwordField));
		registrationForm.add(repeatPasswordField);
		registrationForm.add(new ComponentFeedbackPanel("repeat_password_fp", repeatPasswordField));
	}

	private PasswordTextField createPasswordField(Form<T> registrationForm) {
		// Add password field
		PasswordTextField passwordTextField = new PasswordTextField("password");
		passwordTextField.add(PasswordStrengthValidator.getInstance());
		registrationForm.add(passwordTextField);
		registrationForm.add(new ComponentFeedbackPanel("password_fp", passwordTextField));
		return passwordTextField;
	}

	private void createEmailField(Form<T> registrationForm) {
		// Add email field
		RequiredTextField<String> emailField = new RequiredTextField<String>("email");
		emailField.add(EmailAddressValidator.getInstance());
		emailField.add(new EmailUsageValidator(false, null));
		registrationForm.add(emailField);
		registrationForm.add(new ComponentFeedbackPanel("email_fp", emailField));
	}

	private void createUsernameField(Form<T> registrationForm) {
		// Add username field
		RequiredTextField<String> usernameField = new RequiredTextField<String>("username");
		usernameField.add(new UsernamePatternValidator());
		usernameField.add(new UsernameValidator());
		registrationForm.add(usernameField);
		registrationForm.add(new ComponentFeedbackPanel("username_fp", usernameField));
	}

	protected UserFacade getUserFacade() {
		return userFacade;
	}

	/**
	 * Additional properties to set before persisting user
	 * 
	 * @param user
	 * @param registration
	 */
	protected void onBeforeRegistration(K user, T registration) {
	}

	/**
	 * Additional fields to form should be added using this method
	 * 
	 * @param registrationForm
	 */
	protected void onFormInitialize(Form<T> registrationForm) {
	}
}
