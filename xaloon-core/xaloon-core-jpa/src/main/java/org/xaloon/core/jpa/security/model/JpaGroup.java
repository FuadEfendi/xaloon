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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xaloon.core.api.security.model.SecurityGroup;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.model.BookmarkableEntity;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_SECURITY_GROUP", uniqueConstraints = @UniqueConstraint(columnNames = { "GROUP_NAME" }))
public class JpaGroup extends BookmarkableEntity implements SecurityGroup {
	private static final long serialVersionUID = 1L;

	@Column(name = "GROUP_NAME", nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "XAL_SECURITY_GROUP_ROLES", joinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID"))
	private List<JpaRole> roles = new ArrayList<JpaRole>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "XAL_SECURITY_USER_GROUPS", joinColumns = @JoinColumn(name = "GROUP_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "ID"))
	private List<JpaUserDetails> users = new ArrayList<JpaUserDetails>();

	/**
	 * Gets users.
	 * 
	 * @return users
	 */
	public List<JpaUserDetails> getUsers() {
		return users;
	}

	/**
	 * Sets users.
	 * 
	 * @param users
	 *            users
	 */
	public void setUsers(List<JpaUserDetails> users) {
		this.users = users;
	}

	/**
	 * Gets roles.
	 * 
	 * @return roles
	 */
	public List<JpaRole> getRoles() {
		return roles;
	}

	/**
	 * Sets roles.
	 * 
	 * @param roles
	 *            roles
	 */
	public void setRoles(List<JpaRole> roles) {
		this.roles = roles;
	}

	/**
	 * @return group name
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

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JpaGroup)) {
			return false;
		}
		JpaGroup entity = (JpaGroup)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getId(), entity.getId());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getId());
		return hashCodeBuilder.hashCode();
	}

	@Override
	@PrePersist
	protected void beforeCreate() {
		setPath(UrlUtil.encode(getName()));
	}
}
