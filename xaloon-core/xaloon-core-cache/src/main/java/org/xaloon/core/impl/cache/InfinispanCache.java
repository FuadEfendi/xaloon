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
package org.xaloon.core.impl.cache;

import org.apache.commons.lang.StringUtils;
import org.infinispan.config.Configuration;
import org.infinispan.manager.EmbeddedCacheManager;
import org.xaloon.core.api.cache.Cache;

/**
 * Jboss Inifinispan cache implementation for {@link Cache} interface
 * 
 * @author vytautas r.
 * @version 1.1, 10/06/10
 * @since 1.5
 */

public class InfinispanCache implements Cache {
	private static final long serialVersionUID = 1L;

	/** Cache name to use */
	private String cacheName;

	/** maximum size of the cache */
	private int maxCacheSize = 1000;

	/** Configuration for selected cache to use */
	private Configuration configuration;

	/** Cache container */
	private transient EmbeddedCacheManager cacheContainer;

	/**
	 * @see Cache#readFromCache(String)
	 */
	public <T> T readFromCache(String key) {
		org.infinispan.Cache<String, T> cache = cacheContainer.getCache(cacheName);
		if (cache != null) {
			return cache.get(key);
		}
		return null;
	}

	/**
	 * @see Cache#removeFromCache(String)
	 */
	public void removeFromCache(String key) {
		if (!StringUtils.isEmpty(key)) {
			cacheContainer.getCache(cacheName).remove(key);
		}
	}

	/**
	 * @see Cache#storeToCache(String, Object)
	 */
	public <T> void storeToCache(String key, T value) {
		if (!StringUtils.isEmpty(key) && value != null) {
			org.infinispan.Cache<String, T> cache = cacheContainer.getCache(cacheName);
			if (cache != null) {
				// Clear cache if maximum size is reached
				if (cache.size() > maxCacheSize) {
					cache.clear();
				}
				cache.put(key, value);
			}
		}
	}

	/**
	 * Sets cache container for this cache
	 * 
	 * @param cacheContainer
	 *            to set
	 */
	public void setCacheContainer(EmbeddedCacheManager cacheContainer) {
		this.cacheContainer = cacheContainer;
	}


	/**
	 * Sets maximimum size of this cache
	 * 
	 * @param maxCacheSize
	 *            cache size to set
	 */
	public void setMaxCacheSize(int maxCacheSize) {
		this.maxCacheSize = maxCacheSize;
	}

	/**
	 * Sets cache name for this cache
	 * 
	 * @param cacheName
	 *            to set
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}

	/**
	 * Sets configuration for this cache and initializes it
	 * 
	 * @param configuration
	 *            custom cache configuration instance
	 */
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
		createCache();
	}

	/**
	 * Creates cache with specified parameters
	 */
	private void createCache() {
		cacheContainer.defineConfiguration(cacheName, configuration);
	}

	@Override
	public void clear() {
		cacheContainer.getCache(cacheName).clear();
	}
}