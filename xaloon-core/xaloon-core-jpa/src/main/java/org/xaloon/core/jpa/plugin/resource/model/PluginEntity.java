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
package org.xaloon.core.jpa.plugin.resource.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.xaloon.core.jpa.model.AbstractEntity;

/**
 * @author vytautas r.
 */
@Cacheable
@Entity
@Table(name = "XAL_PLUGIN_ENTITY", uniqueConstraints = { @UniqueConstraint(columnNames = { "PLUGIN_KEY" }) })
public class PluginEntity extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PLUGIN_ENABLED", nullable = false)
	private boolean enabled;

	@Column(name = "PLUGIN_DATA", nullable = false)
	@Lob
	private String pluginData;

	@Column(name = "PLUGIN_KEY", nullable = false)
	private String pluginKey;

	/**
	 * Gets enabled.
	 * 
	 * @return enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets enabled.
	 * 
	 * @param enabled
	 *            enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return xml representation of plugin bean data
	 */
	public String getPluginData() {
		return pluginData;
	}

	/**
	 * @param pluginData
	 */
	public void setPluginData(String pluginData) {
		this.pluginData = pluginData;
	}

	/**
	 * @return plugin id
	 */
	public String getPluginKey() {
		return pluginKey;
	}

	/**
	 * @param pluginKey
	 */
	public void setPluginKey(String pluginKey) {
		this.pluginKey = pluginKey;
	}

	@Override
	public String toString() {
		return String.format("[%s] pluginKey=%s, enabled=%b", this.getClass().getSimpleName(), getPluginKey(), isEnabled());
	}
}
