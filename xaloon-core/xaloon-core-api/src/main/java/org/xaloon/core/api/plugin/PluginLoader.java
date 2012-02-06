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
package org.xaloon.core.api.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.security.AuthorityFacade;

/**
 * @author vytautas r.
 */
@Named
public class PluginLoader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PluginLoader.class);

	@Inject
	@Named("pluginRegistry")
	private PluginRegistry pluginRegistry;

	@Inject
	private AuthorityFacade authorityFacade;

	private List<Plugin> loadedPlugins;

	private final List<Plugin> userPlugins = new ArrayList<Plugin>();

	private final List<Plugin> hiddenPlugins = new ArrayList<Plugin>();

	/**
	 * Loads system plugins and should be used as a first invocation
	 */
	public void loadSystemPlugins() {
		if (loadedPlugins == null) {
			loadPlugins();
		}
		registerPlugins(hiddenPlugins);
	}

	private void registerPlugins(List<Plugin> pluginsToRegister) {
		for (Plugin plugin : pluginsToRegister) {
			try {
				pluginRegistry.lookup(plugin.getClass());
				LOGGER.warn(String.format("Plugin '%s' is already registered. Ignoring ...", plugin.getClass().getName()));
			} catch (PluginNotFoundException e) {
				pluginRegistry.register(plugin);
				authorityFacade.registerRoles(plugin);
			}
		}
	}

	/**
	 * Loads user defined plugins
	 */
	public void loadUserPlugins() {
		if (loadedPlugins == null) {
			loadPlugins();
		}
		registerPlugins(userPlugins);
	}

	private void loadPlugins() {
		loadedPlugins = ServiceLocator.get().getInstances(Plugin.class);
		for (Plugin plugin : loadedPlugins) {
			if (PluginType.HIDDEN.equals(plugin.getType())) {
				hiddenPlugins.add(plugin);
			} else {
				userPlugins.add(plugin);
			}
		}
	}
}
