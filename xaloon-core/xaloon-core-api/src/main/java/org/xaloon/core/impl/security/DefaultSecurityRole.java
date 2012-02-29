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
package org.xaloon.core.impl.security;

import java.util.ArrayList;
import java.util.List;

import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.core.api.security.model.UserDetails;

/**
 * @author vytautas r.
 */
public class DefaultSecurityRole extends DefaultSecurityEntity implements SecurityRole {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Authority> authorities = new ArrayList<Authority>();
	private List<UserDetails> users = new ArrayList<UserDetails>();

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public DefaultSecurityRole(String name) {
		super(name);
	}

	/**
	 * Gets users.
	 * 
	 * @return users
	 */
	public List<UserDetails> getUsers() {
		return users;
	}

	/**
	 * Sets users.
	 * 
	 * @param users
	 *            users
	 */
	public void setUsers(List<UserDetails> users) {
		this.users = users;
	}

	/**
	 * Gets authorities.
	 * 
	 * @return authorities
	 */
	public List<Authority> getAuthorities() {
		return authorities;
	}

	/**
	 * Sets authorities.
	 * 
	 * @param authorities
	 *            authorities
	 */
	public void setAuthorities(List<Authority> authorities) {
		this.authorities = authorities;
	}
}
