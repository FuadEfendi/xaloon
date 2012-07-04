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
import java.util.List;

import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;

/**
 * Core plugin interface. It defines required methods provided by plugin implementation. Plugin contains property <b>pluginBean</b> which represents
 * configuration parameters of current plugin. This property usually will be modified via administration panel. It also may be configured once on
 * application startup
 * <p>
 * Property <b>administrationFormClass</b> returns administration view for selected plugin. This property later is used for plugin administration
 * page, which takes administration panel to display configuration parameters for administrator.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */

public interface Plugin extends Serializable, Comparable<Plugin> {

	/**
	 * Returns administration form view class for this plugin
	 * 
	 * @return class of administration view panel
	 */
	Class<?> getAdministratorFormClass();

	/**
	 * Registers unique id for this plugin
	 * 
	 * @param id
	 *            string format parameter to register
	 */
	void setId(String id);

	/**
	 * Returns unique id for this plugin
	 * 
	 * @return string format of plugin identification
	 */
	String getId();

	/**
	 * Registers name of this plugin
	 * 
	 * @param name
	 *            string formatted message to register
	 */
	void setName(String name);

	/**
	 * Returns name of this plugin
	 * 
	 * @return string formatted message as plugin name
	 */
	String getName();

	/**
	 * Registers short description of this plugin
	 * 
	 * @param description
	 *            short plain string or html formatted string
	 */
	void setDescription(String description);

	/**
	 * Returns short description of this plugin
	 * 
	 * @return string representation of description
	 */
	String getDescription();

	/**
	 * Registers order of this plugin if required. Might be used to sort plugins
	 * 
	 * @param order
	 *            integer number greater than zero if plugin is sorted.
	 */
	void setOrder(int order);

	/**
	 * Returns order of this plugin
	 * 
	 * @return integer number greater than zero if plugin is sorted. Otherwise it will not be sorted
	 */
	int getOrder();

	/**
	 * Returns category of this plugin. Usually is should be used only to generate menu of categories for administration purposes
	 * 
	 * @return string format of plugin category
	 */
	String getCategory();

	/**
	 * Registers category of this plugin. Category format is "/parentCategoryName/childCategoryName", for example "/administration/statistics"
	 * 
	 * @param categoryToSet
	 *            string format of plugin category
	 */
	void setCategory(String categoryToSet);

	/**
	 * Returns current plugin version
	 * 
	 * @return string representation of plugin version
	 */
	String getVersion();

	/**
	 * Plugin type is used to determine how plugin is registered or processed. Most generic plugin types are {@link PluginType#VISIBLE} and
	 * {@link PluginType#HIDDEN}. Plugins might be interpreted in different ways depending of their type, for example {@link PluginType#HIDDEN}
	 * plugins should not be used while generating menu
	 * 
	 * @see PluginType
	 * 
	 * @param pluginType
	 *            string representation of plugin type
	 */
	void setType(String pluginType);

	/**
	 * Returns current plugin type
	 * 
	 * @return string representation of plugin type
	 */
	String getType();

	/**
	 * Returns the list of roles supported by this plugin
	 * 
	 * @return list of {@link SecurityRole} as a representation of roles
	 */
	List<SecurityRole> getSupportedRoles();

	/**
	 * Returns the list of permissions/authorities supported by this plugin
	 * 
	 * @return list of {@link Authority} as a representation of permissions
	 */
	List<Authority> getSupportedAuthorities();

	/**
	 * Plugin configuration, which cannot be changed on runtime, for example, custom blog page link pages
	 * 
	 * @return plugin implementation of {@link PluginConfigEntry}
	 */
	<T extends PluginConfigEntry> T getTechnicalConfiguration();

	/**
	 * @param technicalConfiguration
	 *            sets custom plugin configuration to be used for the application
	 */
	<T extends PluginConfigEntry> void setTechnicalConfiguration(T technicalConfiguration);
}
