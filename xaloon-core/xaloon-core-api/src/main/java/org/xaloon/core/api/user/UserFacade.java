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
package org.xaloon.core.api.user;

import java.util.List;
import java.util.Map;

import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.security.LoginService;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public interface UserFacade extends UserDao, LoginService {
	/**
	 * 
	 */
	String EMAIL_VALIDATION_ERROR = "EMAIL_VALIDATION_ERROR";

	/**
	 * @param user
	 * @param password
	 * @param active
	 * @param alias
	 * @return activation key
	 */
	<T extends User> String registerUser(T user, String password, boolean active, KeyValue<String, String> alias);

	/**
	 * Generates and sends new password to requested email if found
	 * 
	 * @param email
	 * @return null if operation completed successfully, otherwise error code, which might be localized
	 */
	String sendNewPassword(String email);

	List<UserSearchResult> findCombinedUsers(Map<String, String> filter, long first, long count);
}
