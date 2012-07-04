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
package org.xaloon.core.api.config;

import org.xaloon.core.api.cache.Cache;
import org.xaloon.core.api.inject.BeanLocatorAdapter;
import org.xaloon.core.api.path.AbsolutePathStrategy;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.PluginBeanSerializer;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.plugin.PluginRegistryListenerCollection;
import org.xaloon.core.api.plugin.resource.PluginResourceRepository;
import org.xaloon.core.api.plugin.resource.ResourceRepositoryListenerCollection;
import org.xaloon.core.api.security.external.OauthSecurityTokenProvider;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.user.UserListenerCollection;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.impl.plugin.DefaultPluginBeanSerializer;

/**
 * Default java configuration class, containing application listeners. Once configured on application startup, this class might be used in software
 * code to get available listeners to process.
 * 
 * @author vytautas r.
 */
public class Configuration {
	private static final Configuration instance = new Configuration();

	private final ResourceRepositoryListenerCollection resourceRepositoryListeners = new ResourceRepositoryListenerCollection();

	private final PluginRegistryListenerCollection pluginRegistryListenerCollection = new PluginRegistryListenerCollection();

	/** cache of {@link AbstractPluginBean} items **/
	private Cache pluginResourceCache;

	private final UserListenerCollection userListenerCollection = new UserListenerCollection();

	private PluginBeanSerializer pluginBeanSerializer = new DefaultPluginBeanSerializer();

	private Cache fileRepositoryCache;

	private Class<? extends User> persistedUserClass;

	private BeanLocatorAdapter beanLocatorAdapter;

	private AbsolutePathStrategy<FileDescriptor> fileDescriptorAbsolutePathStrategy;

	private OauthSecurityTokenProvider oauthSecurityTokenProvider;


	private Configuration() {
	}

	/**
	 * @return singleton instance of Configuration class
	 */
	public static Configuration get() {
		return instance;
	}

	/**
	 * Implementation of {@link PluginResourceRepository} interface is responsible to call this collection
	 * 
	 * @see ResourceRepositoryListenerCollection
	 * @return resource repository listeners to process before saving property and after saving property into JCR
	 */
	public final ResourceRepositoryListenerCollection getResourceRepositoryListeners() {
		return resourceRepositoryListeners;
	}

	/**
	 * {@link PluginRegistry} calls this collection when registering plugins
	 * 
	 * @see PluginRegistryListenerCollection
	 * @return plugin registry listeners to process before registering plugins
	 */
	public final PluginRegistryListenerCollection getPluginRegistryListenerCollection() {
		return pluginRegistryListenerCollection;
	}

	/**
	 * @return plugin bean serializer instance
	 */
	public PluginBeanSerializer getPluginBeanSerializer() {
		return pluginBeanSerializer;
	}

	/**
	 * @param pluginBeanSerializer
	 */
	public void setPluginBeanSerializer(PluginBeanSerializer pluginBeanSerializer) {
		this.pluginBeanSerializer = pluginBeanSerializer;
	}

	/**
	 * Implementation of the cache, which will be used by {@link FileRepositoryFacade}. Caching will not be used if not provided by configuration.
	 * 
	 * @return instance of the file cache
	 */
	public Cache getFileRepositoryCache() {
		return fileRepositoryCache;
	}

	/**
	 * @param fileRepositoryCache
	 *            file cache to us by {@link FileRepositoryFacade}
	 */
	public void setFileRepositoryCache(Cache fileRepositoryCache) {
		this.fileRepositoryCache = fileRepositoryCache;
	}

	/**
	 * @return the persistedUserClass
	 */
	public Class<? extends User> getPersistedUserClass() {
		return persistedUserClass;
	}

	/**
	 * @param persistedUserClass
	 *            the persistedUserClass to set
	 */
	public void setPersistedUserClass(Class<? extends User> persistedUserClass) {
		this.persistedUserClass = persistedUserClass;
	}

	/**
	 * @return the instance of bean locator adapter
	 */
	public BeanLocatorAdapter getBeanLocatorAdapter() {
		if (beanLocatorAdapter == null) {
			throw new IllegalArgumentException("Application is not properly configured! Instance of BeanLocatorAdapter is not provided!");
		}
		return beanLocatorAdapter;
	}

	/**
	 * @param beanLocatorAdapter
	 *            bean locator adapter to set
	 */
	public void setBeanLocatorAdapter(BeanLocatorAdapter beanLocatorAdapter) {
		this.beanLocatorAdapter = beanLocatorAdapter;
	}

	/**
	 * Gets fileDescriptorAbsolutePathStrategy.
	 * 
	 * @return fileDescriptorAbsolutePathStrategy
	 */
	public AbsolutePathStrategy<FileDescriptor> getFileDescriptorAbsolutePathStrategy() {
		if (fileDescriptorAbsolutePathStrategy == null) {
			throw new IllegalArgumentException(
				"Application is not properly configured! Instance of AbsolutePathStrategy<FileDescriptor> is not provided!");
		}
		return fileDescriptorAbsolutePathStrategy;
	}

	/**
	 * Sets fileDescriptorAbsolutePathStrategy.
	 * 
	 * @param fileDescriptorAbsolutePathStrategy
	 *            fileDescriptorAbsolutePathStrategy
	 */
	public void setFileDescriptorAbsolutePathStrategy(AbsolutePathStrategy<FileDescriptor> fileDescriptorAbsolutePathStrategy) {
		this.fileDescriptorAbsolutePathStrategy = fileDescriptorAbsolutePathStrategy;
	}

	/**
	 * @return external authentication security token provider
	 */
	public OauthSecurityTokenProvider getOauthSecurityTokenProvider() {
		if (oauthSecurityTokenProvider == null) {
			throw new IllegalArgumentException("Application is not properly configured! Instance of OauthSecurityTokenProvider is not provided!");
		}
		return oauthSecurityTokenProvider;
	}

	/**
	 * @param oauthSecurityTokenProvider
	 */
	public void setOauthSecurityTokenProvider(OauthSecurityTokenProvider oauthSecurityTokenProvider) {
		this.oauthSecurityTokenProvider = oauthSecurityTokenProvider;
	}

	/**
	 * Gets userListenerCollection.
	 * 
	 * @return userListenerCollection
	 */
	public UserListenerCollection getUserListenerCollection() {
		return userListenerCollection;
	}

	/**
	 * Gets pluginResourceCache.
	 * 
	 * @return pluginResourceCache
	 */
	public Cache getPluginResourceCache() {
		return pluginResourceCache;
	}

	/**
	 * Sets pluginResourceCache for {@link AbstractPluginBean} items. Not cached if there is no cache provided.
	 * 
	 * @param pluginResourceCache
	 *            pluginResourceCache
	 */
	public void setPluginResourceCache(Cache pluginResourceCache) {
		this.pluginResourceCache = pluginResourceCache;
	}
}
