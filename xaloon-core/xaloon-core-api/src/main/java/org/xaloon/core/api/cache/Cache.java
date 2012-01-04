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
package org.xaloon.core.api.cache;

import java.io.Serializable;

/**
 * Simple cache interface for reading, storing and removing items from cache. It may wrap different implementations for custom solutions
 * 
 * @author vytautas r.
 * @version 1.1, 09/30/10
 * @since 1.3
 */

public interface Cache extends Serializable {

	/**
	 * Read object from cache
	 * 
	 * @param <T>
	 * @param key
	 *            to read cached object by
	 * @return cached object. null - if there is no such object by provided key
	 */
	<T> T readFromCache(String key);

	/**
	 * Stores object to cache
	 * 
	 * @param <T>
	 * @param key
	 *            to store object by
	 * @param value
	 *            to store into cache
	 */
	<T> void storeToCache(String key, T value);

	/**
	 * Removes object from cache
	 * 
	 * @param key
	 *            to remove object by
	 */
	void removeFromCache(String key);

	/**
	 * Removes everything from cache
	 */
	void clear();
}
