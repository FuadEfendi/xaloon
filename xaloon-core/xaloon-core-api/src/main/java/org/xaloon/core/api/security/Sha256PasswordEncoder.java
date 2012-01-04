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

import javax.inject.Named;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author vytautas r.
 */
@Named("sha256PasswordEncoder")
public class Sha256PasswordEncoder extends PasswordEncoder {

	@Override
	public String encode(String username, String password) {
		return DigestUtils.sha256Hex(username + password);
	}

	@Override
	public String encode(String username, String password, String salt) {
		return DigestUtils.sha256Hex(encode(username, password) + salt);
	}
}
