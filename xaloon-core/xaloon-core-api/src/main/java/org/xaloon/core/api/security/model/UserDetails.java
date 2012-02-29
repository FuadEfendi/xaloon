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
package org.xaloon.core.api.security.model;

import java.util.List;

import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.Persistable;

/**
 * @author vytautas r.
 */
public interface UserDetails extends Persistable {
	/**
	 * @return username
	 */
	String getUsername();

	/**
	 * @return encrypted password
	 */
	String getPassword();

	/**
	 * @return account non expired
	 */
	boolean isAccountNonExpired();

	/**
	 * @param flag
	 *            new flag to set
	 */
	void setAccountNonExpired(boolean flag);

	/**
	 * @return account non locked
	 */
	boolean isAccountNonLocked();

	/**
	 * @param flag
	 *            new flag to set
	 */
	void setAccountNonLocked(boolean flag);

	/**
	 * @return credentials non expired
	 */
	boolean isCredentialsNonExpired();

	/**
	 * @param flag
	 *            new flag to set
	 */
	void setCredentialsNonExpired(boolean flag);

	/**
	 * @return enabled
	 */
	boolean isEnabled();

	/**
	 * @param flag
	 *            new flag to set
	 */
	void setEnabled(boolean flag);

	/**
	 * @return list of available user aliases.
	 */
	List<? extends KeyValue<String, String>> getAliases();

	<T extends SecurityGroup> List<T> getGroups();

	<T extends SecurityRole> List<T> getRoles();

	<T extends Authority> List<T> getAuthorities();
}
