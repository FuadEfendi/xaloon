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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.WebSession;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.component.custom.ConfirmationAjaxLink;
import org.xaloon.wicket.component.security.page.SignInNoParamsPage;
import org.xaloon.wicket.component.security.panel.SignInPanel;
import org.xaloon.wicket.plugin.user.page.UserProfilePage;
import org.xaloon.wicket.util.UrlUtils;


/**
 * @author vytautas r.
 */
public class ExternalAuthenticationPanel extends Panel {
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	private AuthenticationFacade authenticationFacade;

	private String selectedLoginType;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public ExternalAuthenticationPanel(String id, IModel<? extends User> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// Add used aliases
		addAliases();

		// Add available providers
		addAvailableProviders();
	}

	private void addAliases() {
		List<? extends KeyValue<String, String>> aliases = securityFacade.getAliases();
		add(new ListView<KeyValue<String, String>>("linked-account", aliases) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<KeyValue<String, String>> item) {
				KeyValue<String, String> keyValue = item.getModelObject();
				item.add(new Label("key", new Model<String>(keyValue.getKey())));

				// Add delete link
				AjaxLink<KeyValue<String, String>> link_delete = new ConfirmationAjaxLink<KeyValue<String, String>>("delete", item.getModel()) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						KeyValue<String, String> keyValue = getModelObject();
						userFacade.removeAlias(securityFacade.getCurrentUsername(), keyValue);
						securityFacade.removeAlias(keyValue);
						setResponsePage(UserProfilePage.class);
					}
				};
				item.add(link_delete);
			}
		});
	}

	/**
	 * @return
	 */
	public boolean isExternalAuthenticationEnabled() {
		try {
			return authenticationFacade.isPluginEnabled();
		} catch (NullPointerException e) {
			// If WELD is used then authenticationFacade is always not null, but exception is thrown when trying to access the method
			return false;
		}
	}

	private void addAvailableProviders() {
		List<? extends KeyValue<String, String>> aliases = securityFacade.getAliases();
		List<String> allProviders = new ArrayList<String>();
		if (isExternalAuthenticationEnabled()) {
			allProviders = authenticationFacade.getAvailableProviderSet();
		}
		List<String> availableProviderCollection = getAvailableProviderSet(aliases, allProviders);

		DropDownChoice<String> availableProviderChoice = new DropDownChoice<String>("available-provider-list", new PropertyModel<String>(this,
			"selectedLoginType"), availableProviderCollection);

		add(new IndicatingAjaxButton("linkNewAccount") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (!StringUtils.isEmpty(selectedLoginType)) {
					String referrerURL = UrlUtils.toAbsolutePath(UserProfilePage.class, null);
					String absoluteURL = UrlUtils.toAbsolutePath(SignInNoParamsPage.class, null);

					WebSession.get().setMetaData(SignInPanel.METADATAKEY_REFERER, referrerURL);
					WebSession.get().setMetaData(SignInPanel.METADATAKEY_LOGIN_TYPE, selectedLoginType);
					authenticationFacade.beginConsumption(selectedLoginType, absoluteURL);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});

		add(availableProviderChoice);
	}

	private List<String> getAvailableProviderSet(List<? extends KeyValue<String, String>> aliases, List<String> allProviders) {
		List<String> availableProviderCollection = new ArrayList<String>(allProviders);
		for (KeyValue<String, String> keyValue : aliases) {
			if (availableProviderCollection.contains(keyValue.getKey())) {
				availableProviderCollection.remove(keyValue.getKey());
			}
		}
		return availableProviderCollection;
	}

	/**
	 * @return selected login type from drop down choice list
	 */
	public String getSelectedLoginType() {
		return selectedLoginType;
	}

	/**
	 * @param selectedLoginType
	 */
	public void setSelectedLoginType(String selectedLoginType) {
		this.selectedLoginType = selectedLoginType;
	}
}
