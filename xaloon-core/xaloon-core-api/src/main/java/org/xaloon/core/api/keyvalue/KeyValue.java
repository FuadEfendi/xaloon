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

import org.xaloon.core.api.bookmark.Bookmarkable;


/**
 * Simple interface used to represent key-value pair
 * 
 * @author vytautas r.
 * @param <T>
 *            key representation type
 * @param <K>
 *            value representation type
 */
public interface KeyValue<T, K> extends Bookmarkable {
	/**
	 * Returns key pair element
	 * 
	 * @return object of type T
	 */
	T getKey();

	/**
	 * Sets property key
	 * 
	 * @param key
	 */
	void setKey(T key);

	/**
	 * Returns value pair element
	 * 
	 * @return object of type K
	 */
	K getValue();

	/**
	 * Set key value
	 * 
	 * @param value
	 */
	void setValue(K value);

	/**
	 * Checks if key and value elements are empty
	 * 
	 * @return true if both key and value elements are null or empty
	 */
	boolean isEmpty();
}
