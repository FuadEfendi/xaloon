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
package org.xaloon.wicket.plugin;

import javax.inject.Inject;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.util.ClassUtil;

/**
 * Abstract plugin class might be used as core class for plugin implementations.
 * <p>
 * It provides such methods for subclasses as plugin registry class, plugin bean and other methods which might be helpful for development.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @param <K>
 *            plugin bean object, which stores plugin properties
 * @param <T>
 *            plugin class
 * @since 1.5
 */

public abstract class AbstractPluginPanel<K extends AbstractPluginBean, T extends Plugin> extends Panel {
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(AbstractPluginPanel.class);

	/**
	 * Error message saying that plugin bean was not found
	 */
	private static final String PLUGIN_BEAN_UNRESOLVED = "PLUGIN_BEAN_UNRESOLVED";

	/**
	 * Warning message saying that plugin is disabled
	 */
	private static final String PLUGIN_NOT_ENABLED = "PLUGIN_NOT_ENABLED";

	/**
	 * Provided key for all plugins to get default message while deleting entry
	 */
	public static final String DELETE_CONFIRMATION = "DELETE_CONFIRMATION";


	@Inject
	private PluginRegistry registry;

	@Inject
	protected SecurityFacade securityFacade;

	/**
	 * plugin instance
	 */
	private T plugin;

	/**
	 * plugin bean properties instance
	 */
	private K pluginBean;

	/**
	 * plugin class taken from generics
	 */
	private Class<?> pluginClass;

	/**
	 * passed page parameters
	 */
	private PageParameters pageRequestParameters;

	/**
	 * Construct.
	 * 
	 * @param id
	 *            wicket id
	 */
	public AbstractPluginPanel(String id) {
		super(id);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            wicket id
	 * @param pageRequestParameters
	 *            page parameters got from page
	 */
	public AbstractPluginPanel(String id, PageParameters pageRequestParameters) {
		super(id);
		this.pageRequestParameters = cleanupPageRequestParameters(pageRequestParameters);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 *            wicket id
	 * @param model
	 * @param pageRequestParameters
	 *            page parameters got from page
	 */
	public AbstractPluginPanel(String id, IModel<?> model, PageParameters pageRequestParameters) {
		super(id, model);
		this.pageRequestParameters = cleanupPageRequestParameters(pageRequestParameters);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public AbstractPluginPanel(String id, IModel<?> model) {
		super(id, model);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		onInitialize(getPlugin(), getPluginBean());
	}

	@Override
	protected void onConfigure() {
		boolean isPluginEnabled = isPluginEnabled();
		setVisible(isVisible() && isPluginEnabled && isPluginValid());
		if (!isPluginEnabled && logger.isWarnEnabled()) {
			logger.warn("[" + getClass().getName() + "] " + getString(PLUGIN_NOT_ENABLED));
		}
		super.onConfigure();
	}

	/**
	 * This method should be used in order to get full support of plugins
	 * 
	 * @param plugin
	 *            plugin instance
	 * @param pluginBean
	 *            plugin properties instance
	 */
	protected void onInitialize(T plugin, K pluginBean) {
	}

	/**
	 * Each panel is responsible to clean up page request parameters before passing them further.
	 * <p>
	 * Use case: Some components may contain stateless forms. These parameters are added to {@link PageParameters} when submitting the form. Later
	 * absolute url may be incorrectly generated with these provided parameters.
	 * <p>
	 * Usually clean up should be made by the parent component, before passing page parameters to child panel.
	 * 
	 * @param pageRequestParameters
	 *            page request parameters to clean up
	 * @return {@link PageParameters} instance with removed parameters
	 */
	protected PageParameters cleanupPageRequestParameters(PageParameters pageRequestParameters) {
		return pageRequestParameters;
	}

	/**
	 * Returns plugin object of extended class
	 * 
	 * @return plugin instance to return. {@link RuntimeException} is thrown if plugin is not found
	 */
	@SuppressWarnings("unchecked")
	protected T getPlugin() {
		if (plugin == null) {
			Class<?> pluginClass = getPluginClass();
			plugin = (T)getPluginRegistry().lookup(pluginClass);
		}
		return plugin;
	}

	/**
	 * Returns actual class of {@link Plugin} implementation
	 * 
	 * @return plugin instance
	 */
	private Class<?> getPluginClass() {
		if (pluginClass == null) {
			pluginClass = ClassUtil.getClassGenericType(this.getClass(), 1, Plugin.class);
		}
		return pluginClass;
	}

	/**
	 * Returns plugin bean which is registered by required plugin
	 * 
	 * @return plugin instance. {@link RuntimeException} is thrown if plugin bean is not found in plugin registry
	 */
	@SuppressWarnings("unchecked")
	protected K getPluginBean() {
		if (pluginBean == null) {
			pluginBean = (K)getPluginRegistry().getPluginBean(getPluginClass());
			if (pluginBean == null) {
				throw new RuntimeException(getString(PLUGIN_BEAN_UNRESOLVED));
			}
		}
		return pluginBean;
	}

	/**
	 * Checks if plugin is enabled
	 * 
	 * @return true if plugin is enabled, otherwise - false. {@link RuntimeException} is thrown if plugin is not found.
	 */
	protected boolean isPluginEnabled() {
		return getPluginRegistry().isEnabled(getPluginClass());
	}

	protected boolean isPluginValid() {
		if (getPluginBean() != null) {
			return getPluginBean().isValid();
		}
		return true;
	}

	/**
	 * 
	 * @return plugin registry instance
	 */
	protected PluginRegistry getPluginRegistry() {
		return registry;
	}

	/**
	 * Returns security facade to use
	 * 
	 * @return security facade instance
	 */
	protected SecurityFacade getSecurityFacade() {
		return securityFacade;
	}

	protected Class<? extends IRequestablePage> getParentPageClass() {
		Page parentPage = getParent().getPage();
		return parentPage.getClass();
	}

	/**
	 * Gets pageRequestParameters.
	 * 
	 * @return pageRequestParameters
	 */
	public PageParameters getPageRequestParameters() {
		if (pageRequestParameters == null) {
			return getPage().getPageParameters();
		}
		return pageRequestParameters;
	}
}
