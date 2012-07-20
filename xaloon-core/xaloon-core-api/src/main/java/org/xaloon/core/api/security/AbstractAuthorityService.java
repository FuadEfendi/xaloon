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
package org.xaloon.core.api.security;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.api.security.model.SecurityEntity;
import org.xaloon.core.api.security.model.UserDetails;

/**
 * @author vytautas r.
 * @param <T>
 * @param <K>
 */
public interface AbstractAuthorityService<T extends SecurityEntity, K extends SecurityEntity> extends Serializable {
	/**
	 * Returns pageable list of existing security items
	 * 
	 * @param first
	 *            first security item to fetch
	 * @param count
	 *            max count of security items to fetch
	 * @return list of found entities
	 */
	List<T> getAuthorities(long first, long count);

	Long getCount();

	/**
	 * Returns new instance of security item implementation
	 * 
	 * @return new instance of security item implementation
	 */
	T newAuthority();

	void delete(T authority);

	/**
	 * Saves provided security item in storage
	 * 
	 * @param authority
	 *            security item to save
	 * @return
	 */
	T save(T authority);

	/**
	 * Returns existing security item or creates new one if there is no exising
	 * 
	 * @param authorityName
	 *            string security item anme to search for
	 * @return security item object found or created
	 */
	T findOrCreateAuthority(String authorityName);

	List<T> getAuthoritiesByUsername(String username);

	UserDetails revoke(UserDetails userDetails, T authority);

	/**
	 * Searches for a security item and returns the object if found. null is returned otherwise
	 * 
	 * @param name
	 *            security item name to search
	 * @return found security item object
	 */
	T getAuthorityByName(String name);

	T getAuthorityByPath(String path);

	UserDetails assignAuthoritiesByName(UserDetails userDetails, List<String> selections);

	UserDetails assignAuthorities(UserDetails userDetails, List<T> selections);

	T assignChildren(T parent, List<K> selections);

	T revokeChild(T parent, K authority);
}
