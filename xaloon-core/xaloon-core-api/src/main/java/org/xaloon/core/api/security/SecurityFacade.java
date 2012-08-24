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
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.core.api.user.model.User;

/**
 * Security interface to authenticate user and check for various properties
 * 
 * @author vytautas r.
 * 
 */
public interface SecurityFacade extends Serializable {
	/**
	 * Message key if there is no account for selected username
	 */
	String NO_ACCOUNT_FOR_USERNAME = "NO_ACCOUNT_FOR_USERNAME";

	/**
	 * Message key if username or password is incorrect
	 */
	String INVALID_USERNAME_PASSWORD = "INVALID_USERNAME_PASSWORD";

	/**
	 * Default message key if login failed
	 */
	String LOGIN_FAILED = "LOGIN_FAILED";

	/**
	 * Default message key if account is disabled
	 */
	String ACCOUNT_DISABLED = "ACCOUNT_DISABLED";

	/**
	 * Default message key if account is expired
	 */
	String ACCOUNT_EXPIRED = "ACCOUNT_EXPIRED";

	/**
	 * Default message key if account is locked
	 */
	String ACCOUNT_LOCKED = "ACCOUNT_LOCKED";

	/**
	 * Default message key if credentials expired
	 */
	String CREDENTIALS_EXPIRED = "CREDENTIALS_EXPIRED";

	/**
	 * Authenticate user
	 * 
	 * @param username
	 *            username to validate
	 * @param password
	 *            password to validate
	 * @return authentication token containing error message and authentication status
	 */
	AuthenticationToken authenticate(String username, String password);

	/**
	 * Checks if user has role of administrator
	 * 
	 * @return true if current user has role of administrator
	 */
	boolean isAdministrator();

	/**
	 * @return username of currently logged in user
	 */
	String getCurrentUsername();

	/**
	 * Returns full user display name, for example John Johny
	 * 
	 * @return string representation of user display name
	 */
	String getCurrentUserDisplayName();

	/**
	 * Returns email for currently logged in user
	 * 
	 * @return email of current user
	 */
	String getCurrentUserEmail();

	/**
	 * Checks if current user is the same as provided username
	 * 
	 * @param username
	 *            username to check
	 * @return true if username and currently logged in user are the same
	 */
	boolean isOwnerOfObject(String username);

	/**
	 * Checks if user has any of requested roles
	 * 
	 * @param roles
	 *            roles to check
	 * @return true if user has at least one role
	 */
	boolean hasAny(String... roles);

	/**
	 * @return checks if user is logged in
	 */
	boolean isLoggedIn();

	/**
	 * @return currently logged in user
	 */
	<T extends User> T getCurrentUser();

	/**
	 * Authenticates user from external system
	 * 
	 * @param token
	 * @return token containing error message and authentication status
	 */
	AuthenticationToken authenticate(AuthenticationToken token);

	/**
	 * @return user is logged in from external system, but not registered new account
	 */
	boolean isRegistered();

	/**
	 * @return alias for external user
	 */
	KeyValue<String, String> getAlias();

	/**
	 * Returns all aliases for current user
	 * 
	 * @return empty collection if there are no aliases found
	 */
	List<? extends KeyValue<String, String>> getAliases();

	/**
	 * logout user
	 */
	void logout();

	/**
	 * Removes alias for a currently signed in user from the session
	 * 
	 * @param alias
	 *            to remove
	 */
	void removeAlias(KeyValue<String, String> alias);

	/**
	 * Add temporary alias for a currently signed in user.
	 * 
	 * @param alias
	 *            alias to add
	 */
	void addAlias(KeyValue<String, String> alias);
}
