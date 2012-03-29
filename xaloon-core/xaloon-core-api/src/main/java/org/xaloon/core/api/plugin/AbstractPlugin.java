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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;


/**
 * Abstract implementation for {@link Plugin} Will be used for further concrete implementations
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @param <T>
 *            plugin property bean to use
 * @since 1.5
 * 
 */

public abstract class AbstractPlugin<T extends AbstractPluginBean> implements Plugin {
	private static final String DEFAULT_VERSION_NUMBER = "1.0";

	private static final long serialVersionUID = 1L;

	/**
	 * Stored unique identifier of this plugin
	 */
	private String id;

	/**
	 * Stored name representation of this plugin. Used in plugin administration console
	 */
	private String name;

	/**
	 * Stored short description of this plugin. Used in plugin administration console. May contain plain string or html formatted string
	 */
	private String description;

	/**
	 * Contains sort property of plugin if required
	 */
	private int order;

	/**
	 * Category of this plugin
	 */
	private String category;

	/**
	 * Type of this plugin
	 */
	private String type;

	private PluginConfigEntry technicalConfiguration;

	/**
	 * Construct.
	 */
	public AbstractPlugin() {
	}

	/**
	 * @see Plugin#setId(String)
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @see Plugin#getId()
	 */
	public String getId() {
		return id;
	}

	/**
	 * @see Plugin#setName(String)
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see Plugin#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see Plugin#setOrder(int)
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @see Plugin#getOrder()
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @see Plugin#setCategory(String)
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @see Plugin#getCategory()
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @see Plugin#setDescription(String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see Plugin#getDescription()
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Default compare implementation. Should be overridden by subclass
	 * 
	 * @param o
	 *            plugin instance to compare to
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	public int compareTo(Plugin o) {
		return 0;
	}

	/**
	 * There is no administration panel by default
	 */
	public Class<?> getAdministratorFormClass() {
		return null;
	}

	/**
	 * Returns default version number
	 * 
	 * @see org.xaloon.core.api.plugin.Plugin#getVersion()
	 */
	public String getVersion() {
		return DEFAULT_VERSION_NUMBER;
	}

	/**
	 * 
	 * @see org.xaloon.core.api.plugin.Plugin#getType()
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @see org.xaloon.core.api.plugin.Plugin#setType(java.lang.String)
	 */
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public List<SecurityRole> getSupportedRoles() {
		return new ArrayList<SecurityRole>();
	}

	@Override
	public List<Authority> getSupportedAuthorities() {
		return new ArrayList<Authority>();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <Z extends PluginConfigEntry> Z getTechnicalConfiguration() {
		return (Z)technicalConfiguration;
	}

	public <U extends PluginConfigEntry> void setTechnicalConfiguration(U technicalConfiguration) {
		this.technicalConfiguration = technicalConfiguration;
	};

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Id: ").append(this.getClass().getName()).append('\n');
		if (!StringUtils.isEmpty(getType())) {
			builder.append("Type: ").append(getType()).append('\n');
		}
		if (!StringUtils.isEmpty(getName())) {
			builder.append("Name: ").append(getName()).append('\n');
		}

		return builder.toString();
	}
}
