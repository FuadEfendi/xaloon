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
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.api.util.KeyFactory;
import org.xaloon.wicket.component.html.TimezoneDropDownChoice;
import org.xaloon.wicket.plugin.user.admin.page.UsersPage;
import org.xaloon.wicket.plugin.user.page.UserRegistrationPage;
import org.xaloon.wicket.plugin.user.validator.AgreementValidator;
import org.xaloon.wicket.plugin.user.validator.EmailUsageValidator;
import org.xaloon.wicket.plugin.user.validator.UsernamePatternValidator;
import org.xaloon.wicket.plugin.user.validator.UsernameValidator;

import com.google.code.jqwicket.ui.ckeditor.CKEditorOptions;
import com.google.code.jqwicket.ui.ckeditor.CKEditorTextArea;

/**
 * user profile panel
 * 
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 * @param <T>
 */
public class UserProfilePanel<T extends User> extends Panel {
	private static final long serialVersionUID = 1L;

	private static final String USER_ALREADY_EXSITS = "USER_ALREADY_EXSITS";
	private static final String PROFILE_SAVED = "PROFILE_SAVED";

	@Inject
	private StringResourceLoader stringResourceLoader;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	private AuthenticationFacade authenticationFacade;

	private Boolean agreement;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param params
	 */
	public UserProfilePanel(String id, PageParameters params) {
		super(id, new Model<PageParameters>(params));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize() {
		super.onInitialize();
		T user = null;

		PageParameters params = (PageParameters)getDefaultModelObject();
		String username = params.get(UsersPage.PARAM_USER_ID).toString();
		if (!StringUtils.isEmpty(username) && securityFacade.isAdministrator()) {
			user = (T)userFacade.getUserByUsername(username);
		} else {
			user = (T)securityFacade.getCurrentUser();
		}
		Form<T> profileForm = new StatelessForm<T>("user-form", new CompoundPropertyModel<T>(user)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				onFormSubmit(getModelObject());
			}
		};
		add(profileForm);

		/** add form feedback panel */
		add(new ComponentFeedbackPanel("feedback-panel", profileForm));

		createUsernameField(profileForm, user);
		createEmailField(profileForm, user);
		profileForm.add(createFirstNameField());
		profileForm.add(createLastNameField());
		profileForm.add(createSignatureField());
		profileForm.add(createTimezoneField());
		createAgreementPanel(profileForm, user);
		profileForm.add(createAgreementMessagePanel());
		profileForm.add(createExternalAuthenticationPanel(profileForm.getModel()));

		onFormInitialize(profileForm);
	}

	private Component createExternalAuthenticationPanel(IModel<T> iModel) {
		// Add external authentication methods
		ExternalAuthenticationPanel externalAuthenticationPanel = new ExternalAuthenticationPanel("external-auth-link", iModel);
		externalAuthenticationPanel.setVisible(externalAuthenticationPanel.isExternalAuthenticationEnabled() && iModel.getObject().getId() != null &&
			!isAdministrationPanel());
		return externalAuthenticationPanel;
	}

	private boolean isAdministrationPanel() {
		return securityFacade.isAdministrator() && !((PageParameters)getDefaultModelObject()).isEmpty();
	}

	private Component createAgreementMessagePanel() {
		// Add agreement message
		return new Label(RegistrationPanel.AGREEMENT_MESSAGE, new Model<String>(getAgreementMessage())).setEscapeModelStrings(false);
	}

	private void createUsernameField(Form<T> profileForm, T user) {
		// Add username field
		TextField<String> usernameField = new RequiredTextField<String>("username");
		usernameField.setVisible(user.getId() == null);
		usernameField.add(new UsernameValidator());
		usernameField.add(new UsernamePatternValidator());

		profileForm.add(usernameField);
		profileForm.add(new ComponentFeedbackPanel("username_fp", usernameField));
	}

	private void createEmailField(Form<T> profileForm, T user) {
		// Add email field
		RequiredTextField<String> emailField = new RequiredTextField<String>("email");
		emailField.add(EmailAddressValidator.getInstance());
		emailField.add(new EmailUsageValidator((user.getId() != null), user.getEmail()));

		profileForm.add(emailField);
		profileForm.add(new ComponentFeedbackPanel("email_fp", emailField));
	}

	private Component createFirstNameField() {
		// Add first name field
		return new TextField<String>("firstName");
	}

	private Component createLastNameField() {
		// Add last name field
		return new TextField<String>("lastName");
	}

	private Component createSignatureField() {
		// Add signature editor
		return new CKEditorTextArea<String>("signature", new CKEditorOptions().toolbar(new CharSequence[][] {
				{ "Bold", "Italic", "-", "NumberedList", "BulletedList", "-", "Link", "Unlink" }, { "UIColor" } }));
	}

	private Component createTimezoneField() {
		return new TimezoneDropDownChoice("timezone");
	}

	private void createAgreementPanel(Form<T> profileForm, T user) {
		// Add agreement field
		CheckBox agreement = new CheckBox("agreement", new PropertyModel<Boolean>(UserProfilePanel.this, "agreement"));
		agreement.add(AgreementValidator.getInstance());
		agreement.setVisible((user.getId() == null) && Boolean.TRUE.equals(user.isExternal()));

		profileForm.add(agreement);
		profileForm.add(new ComponentFeedbackPanel("agreement_fp", agreement));
	}

	protected void onFormSubmit(T user) {
		if ((user.getId() == null) && Boolean.TRUE.equals(user.isExternal())) {
			KeyValue<String, String> alias = securityFacade.getAlias();
			String activationKey = userFacade.registerUser(user, KeyFactory.generateKey(), true, alias);
			if (StringUtils.isEmpty(activationKey)) {
				getSession().error(USER_ALREADY_EXSITS);
			}
		} else {
			userFacade.save(user);
		}
		getSession().info(getString(PROFILE_SAVED));
		setResponsePage(WebApplication.get().getHomePage());
	}

	/**
	 * @return true if user did agree with rules
	 */
	public Boolean getAgreement() {
		return agreement;
	}

	/**
	 * @param agreement
	 */
	public void setAgreement(Boolean agreement) {
		this.agreement = agreement;
	}

	protected UserFacade getUserFacade() {
		return userFacade;
	}


	/**
	 * Default agreement message is taken from root page property xml file or UserProfilePanel.xml file may be overwritten.
	 * 
	 * @return agreement message
	 */
	public String getAgreementMessage() {
		return stringResourceLoader.getString(UserRegistrationPage.class, RegistrationPanel.AGREEMENT_MESSAGE);
	}

	protected void onFormInitialize(Form<T> profileForm) {
	}
}
