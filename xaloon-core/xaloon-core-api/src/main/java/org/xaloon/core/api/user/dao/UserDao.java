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
package org.xaloon.core.api.user.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public interface UserDao extends Serializable {
	/**
	 * @param user
	 * @return persisted instance
	 */
	<T extends User> T save(T user);

	/**
	 * @param username
	 * @return user instance found in database
	 */
	<T extends User> T getUserByUsername(String username);

	/**
	 * @param email
	 * @return user instance found in database
	 */
	<T extends User> T getUserByEmail(String email);

	/**
	 * @return new instance of user
	 */
	<T extends User> T newUser();

	/**
	 * Creates new anonymous user entity
	 * 
	 * @return new instance of anonymous user
	 */
	<T extends User> T newAnonymousUser();

	/**
	 * Creates new anonymous user entity from provided details
	 * 
	 * @param currentUser
	 * @return new instance of anonymous user
	 */
	<T extends User> T newAnonymousUser(T currentUser);

	/**
	 * Concatenates first and last name into single string and returns it as a full user name
	 * 
	 * @param username
	 *            username to lookup for a full name
	 * @return concatenated first and last name for provided username
	 */
	String getFullNameForUser(String username);

	String formatFullName(String firstName, String lastName);

	/**
	 * Returns sublist of system users
	 * 
	 * @param filter
	 *            filter properties to search
	 * @param first
	 *            starting position
	 * @param count
	 *            how many users to fetch
	 * @return sublist of users starting first and max count
	 */
	List<User> findUsers(Map<String, String> filter, long first, long count);

	/**
	 * Returns total count of users in the system if no additional filter added
	 * 
	 * @param filter
	 *            additional properties to search
	 * @return integer value for total existing users in system
	 */
	Long count(Map<String, String> filter);

	/**
	 * Removes user from the system
	 * 
	 * @param username
	 * @return true if user was successfully removed from {@link User} table
	 */
	boolean deleteUser(String username);
}
