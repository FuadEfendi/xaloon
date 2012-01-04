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

import org.xaloon.core.api.inject.ServiceLocator;

/**
 * @author vytautas r.
 */
public abstract class PasswordEncoder {
	private static PasswordEncoder passwordEncoder;

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public abstract String encode(String username, String password);

	/**
	 * @param username
	 * @param password
	 * @param salt
	 * @return
	 */
	public abstract String encode(String username, String password, String salt);

	/**
	 * @return
	 */
	public static PasswordEncoder get() {
		if (passwordEncoder == null) {
			passwordEncoder = ServiceLocator.get().getInstance(PasswordEncoder.class);
		}
		return passwordEncoder;
	}
}
