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
package org.xaloon.core.jpa.plugin.resource;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.cache.Cache;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.plugin.resource.PluginResourceRepository;
import org.xaloon.core.jpa.plugin.resource.model.PluginEntity;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaPluginResourceRepository implements PluginResourceRepository {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaPluginResourceRepository.class);

	/** Plugin bean data property name */
	private static final String PLUGIN_DATA = "DATA";

	/** Plugin enabled/disabled property name */
	private static final String PLUGIN_ENABLED = "ENABLED";

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	private Cache pluginResourceCache;

	@Override
	public void delete(Plugin plugin) {

	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbstractPluginBean> T getPluginBean(Plugin plugin) {
		T data = getPluginDataFromCache(plugin);
		if (data == null) {
			PluginEntity pluginEntity = findPluginEntity(plugin);
			if (pluginEntity != null) {
				data = (T)Configuration.get().getPluginBeanSerializer().deserialize(pluginEntity.getPluginData());
				putPluginEntityToCache(plugin.getId(), data);
			}
		}
		return data;
	}

	private <T extends AbstractPluginBean> T getPluginDataFromCache(Plugin plugin) {
		Cache cache = getPluginResourceCache();
		if (cache != null) {
			return cache.readFromCache(plugin.getId());
		}
		return null;
	}

	@Override
	public <T extends AbstractPluginBean> void setPluginBean(Plugin plugin, T pluginBean) {
		String pluginBeanValue = Configuration.get().getPluginBeanSerializer().serialize(pluginBean);
		PluginEntity pluginEntity = findPluginEntity(plugin);
		Configuration.get().getResourceRepositoryListeners().onBeforeSaveProperty(plugin, PLUGIN_DATA, pluginBeanValue);
		if (pluginEntity == null) {
			pluginEntity = createNewEntity(plugin, pluginBeanValue);
		} else {
			pluginEntity.setPluginData(pluginBeanValue);
			persistenceServices.createOrEdit(pluginEntity);
		}
		Configuration.get().getResourceRepositoryListeners().onAfterSaveProperty(plugin, PLUGIN_DATA);
		putPluginEntityToCache(pluginEntity.getPluginKey(), pluginBean);
	}

	private PluginEntity createNewEntity(Plugin plugin, String pluginBeanValue) {
		try {
			PluginEntity pluginEntity = new PluginEntity();
			pluginEntity.setPluginKey(plugin.getId());
			pluginEntity.setEnabled(Boolean.TRUE);
			pluginEntity.setPluginData(pluginBeanValue);
			return persistenceServices.createOrEdit(pluginEntity);
		} catch (Exception e) {
			LOGGER.error("Could not create plugin entity. Is it already existing?", e);
			PluginEntity found = findPluginEntity(plugin);
			if (found != null) {
				LOGGER.info(String.format("Plugin entity already registered: %s", plugin.getId()));
			}
			return found;
		}
	}

	private <T extends AbstractPluginBean> void putPluginEntityToCache(String id, T data) {
		if (data == null) {
			return;
		}
		Cache cache = getPluginResourceCache();
		if (cache != null) {
			cache.storeToCache(id, data);
		}
	}

	private PluginEntity findPluginEntity(Plugin plugin) {
		QueryBuilder query = new QueryBuilder("select pl from " + PluginEntity.class.getSimpleName() + " pl");
		query.addParameter("pl.pluginKey", "PLUGIN_KEY", plugin.getId());
		return persistenceServices.executeQuerySingle(query);
	}

	@Override
	public void setEnabled(Plugin plugin, boolean enabled) {
		PluginEntity pluginEntity = findPluginEntity(plugin);
		Configuration.get().getResourceRepositoryListeners().onBeforeSaveProperty(plugin, PLUGIN_ENABLED, enabled);
		pluginEntity.setEnabled(enabled);
		persistenceServices.createOrEdit(pluginEntity);
	}

	@Override
	public boolean isEnabled(Plugin plugin) {
		PluginEntity pluginEntity = findPluginEntity(plugin);
		if (pluginEntity == null) {
			throw new RuntimeException("Plugin entity not found: " + plugin.getId());
		}
		return pluginEntity.isEnabled();
	}

	private Cache getPluginResourceCache() {
		if (pluginResourceCache == null) {
			pluginResourceCache = Configuration.get().getPluginResourceCache();
		}
		return pluginResourceCache;
	}
}
