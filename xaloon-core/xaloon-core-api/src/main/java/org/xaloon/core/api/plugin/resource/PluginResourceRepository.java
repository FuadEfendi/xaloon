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
package org.xaloon.core.api.plugin.resource;

import java.io.Serializable;

import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.Plugin;


/**
 * Plugin configuration storage interface for plugin enabling/disabling feature, changing configuration parameters.
 * 
 * @author vytautas r.
 * @version 1.1, 10/06/10
 * @since 1.3
 */
public interface PluginResourceRepository extends Serializable {
	/**
	 * Deletes plugin resource information from storage
	 * 
	 * @param plugin
	 *            parameter which configuration should be deleted
	 */
	void delete(Plugin plugin);

	/**
	 * Returns plugin configuration bean for specified plugin
	 * 
	 * @param <T>
	 *            concrete implementation of configuration bean, which extends {@link AbstractPluginBean}
	 * @param plugin
	 *            plugin object to lookup
	 * @return concrete configuration plugin bean
	 */
	<T extends AbstractPluginBean> T getPluginBean(Plugin plugin);

	/**
	 * Sets new or updates current plugin configuration for specified plugin
	 * 
	 * @param <T>
	 *            concrete implementation of configuration bean, which extends {@link AbstractPluginBean}
	 * @param plugin
	 *            plugin, which contains updatable configuration
	 * @param pluginBean
	 *            configuration, which should be stored into resource repository
	 */
	<T extends AbstractPluginBean> void setPluginBean(Plugin plugin, T pluginBean);

	/**
	 * Changes visibility of plugin.
	 * 
	 * @param plugin
	 *            plugin object to check
	 * @param enabled
	 *            true - to enable plugin, false - to disable plugin
	 */
	void setEnabled(Plugin plugin, boolean enabled);

	/**
	 * Checks if plugin is enabled/disabled
	 * 
	 * @param plugin
	 *            plugin object to lookup
	 * @return true if plugin is enabled, false - otherwise
	 */
	boolean isEnabled(Plugin plugin);
}
