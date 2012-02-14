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
package org.xaloon.wicket.component.custom.panel;

import javax.inject.Inject;

import org.apache.wicket.Application;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.message.model.Message;
import org.xaloon.core.api.plugin.email.EmailFacade;
import org.xaloon.core.api.plugin.email.EmailPluginBean;
import org.xaloon.core.impl.message.DefaultMessage;
import org.xaloon.core.impl.user.DefaultUser;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.email.EmailPlugin;
import org.xaloon.wicket.plugin.email.template.ContactEmailTemplatePage;
import org.xaloon.wicket.plugin.email.template.EmailContentTemplatePage;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class MessagePanel extends AbstractPluginPanel<EmailPluginBean, EmailPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EmailFacade emailFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public MessagePanel(String id, PageParameters pageParameters) {
		super(id, pageParameters);
	}

	class LeaveMessageForm extends org.xaloon.wicket.component.text.MessageForm<Message> {
		private static final long serialVersionUID = 1L;

		public LeaveMessageForm(String id, IModel<Message> model) {
			super(id, model);
		}

		@Override
		protected void onValidate() {
			if (!isPluginEnabled()) {
				error("Message cannot be sent due to system configuration restrictions!");
			}
		}

		@Override
		protected void onSubmit() {
			Message message = getModelObject();
			if (sendEmail(message)) {
				getSession().info("Message was sent. Thank you!");
				setResponsePage(Application.get().getHomePage());
			} else {
				getSession().error("Message cannot be sent due to system configuration restrictions!");
				throw new RedirectToUrlException(UrlUtils.toAbsolutePath(getPage().getClass(), getPageRequestParameters()).toString());
			}
		}

	}

	private boolean sendEmail(Message message) {
		String fromEmail = message.getFromUser().getEmail();
		String fromName = message.getFromUser().getDisplayName();

		EmailContentTemplatePage contentTemplatePage = new ContactEmailTemplatePage(message.getMessage());
		String emailContent = contentTemplatePage.getSource();
		return emailFacade.sendMailToSystem(emailContent, fromEmail, fromName);
	}

	@Override
	protected void onInitialize(EmailPlugin plugin, EmailPluginBean pluginBean) {
		DefaultMessage message = new DefaultMessage();
		message.setFromUser(new DefaultUser());
		add(new LeaveMessageForm("contact-form", new CompoundPropertyModel<Message>(message)));
	}

	@Override
	protected PageParameters cleanupPageRequestParameters(PageParameters pageRequestParameters2) {
		return new PageParameters();
	}
}
