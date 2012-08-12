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

import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.core.api.security.model.UserDetails;

/**
 * @author vytautas r.
 */
public interface LoginService extends Serializable {
	/**
	 * @param username
	 * @param password
	 * @return true if user was successfully logged in
	 */
	boolean performLogin(String username, String password);

	/**
	 * 
	 * @param username
	 * @param password
	 * @return activation key for further processing
	 */
	String registerNewLogin(String username, String password);

	/**
	 * activate user into system
	 * 
	 * @param activationKey
	 * @param userPassword
	 * @return true if activation was fine
	 */
	boolean activate(String activationKey, String userPassword);

	/**
	 * check if username exists in system
	 * 
	 * @param username
	 * @return true if such username is already exists
	 */
	boolean isUsernameRegistered(String username);

	/**
	 * create new password for selected email
	 * 
	 * @param username
	 * @return new random password for selected username
	 */
	String generateNewPassword(String username);

	/**
	 * check if password for selected username is ok
	 * 
	 * @param username
	 * @param password
	 * @return true if password is fine
	 */
	boolean isValidPassword(String username, String password);

	/**
	 * change password for selected username
	 * 
	 * @param username
	 * @param new_password
	 * @return true if password was changed successfully
	 */
	boolean changePassword(String username, String new_password);

	/**
	 * Register login with active state
	 * 
	 * @param username
	 * @param password
	 * @param active
	 * @param alias
	 * @param aliasValue
	 * @return username
	 */
	String registerNewLogin(String username, String password, boolean active, KeyValue<String, String> alias);

	/**
	 * Add alias to selected username
	 * 
	 * @param username
	 * @param alias
	 */
	void addAlias(String username, KeyValue<String, String> alias);

	/**
	 * Remove alias from current username
	 * 
	 * @param currentUsername
	 * @param alias
	 *            to remove
	 */
	void removeAlias(String currentUsername, KeyValue<String, String> alias);

	/**
	 * @param username
	 * @return user details object
	 */
	UserDetails loadUserDetails(String username);

	/**
	 * Returns direct and indirect permissions for selected username
	 * 
	 * @param username
	 * @return list of direct and indirect {@link Authority}
	 */
	List<Authority> getIndirectAuthoritiesForUsername(String username);

	/**
	 * Returns direct and indirect roles for selected username
	 * 
	 * @param username
	 * @return list of direct and indirect {@link SecurityRole}
	 */
	List<SecurityRole> getIndirectRolesForUsername(String username);

	UserDetails modifyCredentialsNonExpired(String username, Boolean newPropertyValue);

	UserDetails modifyAccountNonLocked(String username, Boolean newPropertyValue);

	UserDetails modifyAccountNonExpired(String username, Boolean newPropertyValue);

	UserDetails modifyAccountEnabled(String username, Boolean newPropertyValue);

	/**
	 * Removes user from the system
	 * 
	 * @param username
	 * @return true if user was successfully removed from security service
	 */
	boolean deleteUser(String username);
}
