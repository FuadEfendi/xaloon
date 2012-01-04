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
package org.xaloon.core.impl.plugin;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.plugin.PluginNotFoundException;
import org.xaloon.core.api.plugin.PluginRegistry;

/**
 * Memory based plugin registry - default implementation to store all plugins
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */
@Named("pluginRegistry")
public class DefaultMemoryPluginRegistry extends PluginRegistry implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMemoryPluginRegistry.class);
	/**
	 * The same type may have several plugins
	 */
	private static final Map<Class<?>, Plugin> registeredPlugins = new LinkedHashMap<Class<?>, Plugin>();

	/**
	 * @see PluginRegistry#lookup(Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T lookup(Class<?> pluginClassToLookup) {
		if (pluginClassToLookup == null) {
			throw new IllegalArgumentException("Plugin class to search was not provided.");
		}
		if (registeredPlugins.containsKey(pluginClassToLookup)) {
			return (T)registeredPlugins.get(pluginClassToLookup);
		}
		throw new PluginNotFoundException("Plugin was not found: " + pluginClassToLookup.getName());
	}

	/**
	 * @see PluginRegistry#onUnregister(Plugin)
	 */
	@Override
	protected void onUnregister(Plugin plugin) {
		if (registeredPlugins.containsKey(plugin.getClass())) {
			registeredPlugins.remove(plugin.getClass());
		}
	}

	/**
	 * @see PluginRegistry#onRegister(Plugin)
	 */
	@Override
	protected Plugin onRegister(Plugin pluginToRegister) {
		if (!registeredPlugins.containsKey(pluginToRegister.getClass())) {
			registeredPlugins.put(pluginToRegister.getClass(), pluginToRegister);
		}
		return pluginToRegister;
	}

	/**
	 * @see PluginRegistry#getAllPlugins()
	 */
	@Override
	public Collection<Plugin> getAllPlugins() {
		return registeredPlugins.values();
	}
}
