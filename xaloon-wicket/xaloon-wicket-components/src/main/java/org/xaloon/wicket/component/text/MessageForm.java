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
package org.xaloon.wicket.component.text;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.xaloon.core.api.message.model.Message;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.captcha.RecaptchaPanel;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class MessageForm<T extends Message> extends StatelessForm<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean emailVisible = true;

	private boolean captchaVisible = true;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public MessageForm(String id, IModel<T> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		T message = getModelObject();
		User fromUser;
		if (securityFacade.isLoggedIn()) {
			if (securityFacade.isRegistered()) {
				fromUser = securityFacade.getCurrentUser();
			} else {
				fromUser = userFacade.newAnonymousUser(securityFacade.getCurrentUser());
			}
		} else {
			fromUser = userFacade.newAnonymousUser();
		}
		message.setFromUser(fromUser);

		// Add feedback panel
		add(new FeedbackPanel("panel-feedback"));

		// Add name field
		boolean displayNameEnabled = fromUser == null || StringUtils.isEmpty(fromUser.getDisplayName());
		add(new RequiredTextField<String>("fromUser.displayName").setEnabled(displayNameEnabled));

		// Add email field
		RequiredTextField<String> emailTextField = new RequiredTextField<String>("fromUser.email");
		emailTextField.add(EmailAddressValidator.getInstance());
		emailTextField.setVisible(emailVisible);
		emailTextField.setEnabled(fromUser == null || StringUtils.isEmpty(fromUser.getEmail()));
		add(emailTextField);

		// Add text message field
		add(new TextArea<String>("message"));

		// Add captcha panel
		RecaptchaPanel recaptchaPanel = new RecaptchaPanel("recaptcha-panel", this);
		recaptchaPanel.setVisible(recaptchaPanel.isVisible() && captchaVisible);
		add(recaptchaPanel);
	}

	protected void setEmailVisible(boolean emailVisible) {
		this.emailVisible = emailVisible;
	}

	protected void setCaptchaVisible(boolean captchaVisible) {
		this.captchaVisible = captchaVisible;
	}
}
