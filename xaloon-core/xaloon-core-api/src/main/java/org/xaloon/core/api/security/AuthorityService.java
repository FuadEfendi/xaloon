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

import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.security.model.Authority;

/**
 * @author vytautas r.
 */
public interface AuthorityService extends Serializable {

	/**
	 * Registers supported permissions by plugin
	 * 
	 * @param plugin
	 */
	void registerPermissions(Plugin plugin);

	/**
	 * Returns existing permission or creates new one if there is no exising
	 * 
	 * @param permission
	 *            string permission to search for
	 * @return authority object found or created
	 */
	Authority findOrCreateAuthority(String permission);

	/**
	 * Returns new instance of {@link Authority}
	 * 
	 * @return new instance of {@link Authority}
	 */
	Authority newAuthority();

	/**
	 * Searches for permission and returns {@link Authority} object if found. null is returned otherwise
	 * 
	 * @param permission
	 *            permission to search
	 * @return instance of {@link Authority}
	 */
	Authority findAuthority(String permission);

	/**
	 * Saves provided authority in storage
	 * 
	 * @param entity
	 *            entity to save
	 */
	void save(Authority entity);
}
