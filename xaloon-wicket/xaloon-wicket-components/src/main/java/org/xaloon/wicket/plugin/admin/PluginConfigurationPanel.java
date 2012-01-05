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
package org.xaloon.wicket.plugin.admin;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.exception.CreateClassInstanceException;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.plugin.PluginRegistry;

/**
 * @author vytautas r.
 */
public class PluginConfigurationPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PluginRegistry pluginRegistry;

	private ModalWindow pluginConfigModalWindow;

	/**
	 * Construct.
	 * 
	 * @param pluginConfigModalWindow
	 * 
	 * @param pluginModel
	 */
	public PluginConfigurationPanel(ModalWindow pluginConfigModalWindow, IModel<Plugin> pluginModel) {
		super(pluginConfigModalWindow.getContentId(), pluginModel);
		this.pluginConfigModalWindow = pluginConfigModalWindow;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		Plugin plugin = (Plugin)getDefaultModelObject();
		try {
			AbstractPluginAdministrationPanel<AbstractPluginBean, Plugin> adminPanel = (AbstractPluginAdministrationPanel<AbstractPluginBean, Plugin>)pluginRegistry.createAdministrationForm(
				plugin, "plugin-item");
			if (adminPanel == null) {
				throw new NullPointerException();
			}
			PluginAdministrationForm pluginAdministrationForm = new PluginAdministrationForm("admin-form", plugin);
			adminPanel.initModel(pluginAdministrationForm.getModel());
			pluginAdministrationForm.add(adminPanel);
			add(pluginAdministrationForm);
		} catch (CreateClassInstanceException e) {
			e.printStackTrace();
		}
	}


	class PluginAdministrationForm extends Form<AbstractPluginBean> {
		private static final String PLUGIN_ENABLED = "PLUGIN_ENABLED";

		private static final String PLUGIN_DISABLED = "PLUGIN_DISABLED";

		private static final long serialVersionUID = 1L;

		private Plugin plugin;

		public PluginAdministrationForm(String id, final Plugin plugin) {
			super(id);
			this.plugin = plugin;
			setOutputMarkupId(true);
			AbstractPluginBean pluginBean = pluginRegistry.getPluginBean(plugin);
			if (pluginBean == null) {
				pluginBean = new AbstractPluginBean();
			}
			setModel(new Model<AbstractPluginBean>(pluginBean));

			add(new FeedbackPanel("feedback-panel"));
			add(new Label("plugin-name", new Model<String>(plugin.getName())));
		}

		private String getPluginLabel(Boolean pluginEnabled) {
			return (pluginEnabled) ? PLUGIN_ENABLED : PLUGIN_DISABLED;
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();
			add(new AjaxButton("submit-button", this) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(PluginAdministrationForm.this);
				}

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					AbstractPluginBean modelObject = (AbstractPluginBean)form.getModelObject();
					pluginRegistry.setPluginBean(plugin, modelObject);
					pluginConfigModalWindow.close(target);
				}
			});
		}
	}
}
