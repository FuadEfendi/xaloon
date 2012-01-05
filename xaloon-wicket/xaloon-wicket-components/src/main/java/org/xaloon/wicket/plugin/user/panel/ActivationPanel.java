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

import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.value.ValueMap;
import org.xaloon.core.api.user.UserFacade;

/**
 * @author vytautas r.
 */
public class ActivationPanel extends Panel {
	private static final long serialVersionUID = 1L;


	/** Activation key as parameter */
	public static final String ACTIVATION_KEY = "activationKey";

	/** account was activated. success message */
	public static final String MESSAGE_INFO_ACTIVATED = "message.info.activated";

	/** account was not activated. error message */
	public static final String MESSAGE_ERROR_ACTIVATION = "message.error.activation";

	private static final String MESSAGE_ERROR_KEY_NOT_VALID = "message.error.key.not.valid";

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param params
	 */
	public ActivationPanel(String id, PageParameters params) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new FeedbackPanel("feedback-panel"));
		add(new ActivationForm("activation-form"));
	}

	class ActivationForm extends StatelessForm<Void> {
		private static final long serialVersionUID = 1L;

		private final ValueMap properties = new ValueMap();

		public ActivationForm(String id) {
			super(id);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();

			// Add activation key field
			RequiredTextField<String> activationField = new RequiredTextField<String>(ACTIVATION_KEY, new PropertyModel<String>(properties,
				ACTIVATION_KEY));
			add(activationField);
		}

		public String getActivationKey() {
			return properties.getString(ACTIVATION_KEY);
		}

		@Override
		protected void onSubmit() {
			if (userFacade.activate(getActivationKey())) {
				getSession().info(getString(MESSAGE_INFO_ACTIVATED));
			} else {
				getSession().warn(getString(MESSAGE_ERROR_ACTIVATION));
			}
			setResponsePage(getPage());
		}
	}
}
