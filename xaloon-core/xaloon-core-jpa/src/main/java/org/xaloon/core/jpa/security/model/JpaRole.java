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
package org.xaloon.core.jpa.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.xaloon.core.jpa.model.AbstractEntity;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_SECURITY_ROLE", uniqueConstraints = @UniqueConstraint(columnNames = { "ROLE_NAME" }))
public class JpaRole extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ROLE_NAME", nullable = false)
	private String name;

	@OneToMany
	private List<JpaRoleMembers> users = new ArrayList<JpaRoleMembers>();

	/**
	 * @return role name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets users.
	 * 
	 * @return users
	 */
	public List<JpaRoleMembers> getUsers() {
		return users;
	}

	/**
	 * Sets users.
	 * 
	 * @param users
	 *            users
	 */
	public void setUsers(List<JpaRoleMembers> users) {
		this.users = users;
	}
}
