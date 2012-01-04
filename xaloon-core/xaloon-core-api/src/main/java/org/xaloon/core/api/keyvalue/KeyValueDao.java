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
package org.xaloon.core.api.keyvalue;

import java.io.Serializable;
import java.util.List;

import javax.inject.Named;

import org.xaloon.core.api.exception.CreateClassInstanceException;

/**
 * @author vytautas r.
 * @param <K>
 * @param <V>
 * @param <R>
 */
@Named
public interface KeyValueDao<K, V, R extends KeyValue<K, V>> extends Serializable {
	/**
	 * @param key
	 * @param parsedValue
	 * @return created instance of KeyValue
	 * @throws CreateClassInstanceException
	 */
	R newKeyValue(K key, V parsedValue) throws CreateClassInstanceException;

	/**
	 * @param key
	 * @param parsedValue
	 * @return instance of KeyValue if found in storage. null is returned otherwise
	 */
	R findInStorage(K key, V parsedValue);

	/**
	 * 
	 * @param key
	 * 
	 * @param randomLinkCountToSelect
	 *            how many values to select - maximum available
	 * @return list of KeyValue
	 */
	List<R> findRandomValues(String key, int randomLinkCountToSelect);
}
