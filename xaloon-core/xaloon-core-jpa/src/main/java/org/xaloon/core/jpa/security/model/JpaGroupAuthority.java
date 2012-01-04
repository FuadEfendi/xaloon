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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.xaloon.core.jpa.model.AbstractEntity;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_SECURITY_GROUP_AUTHORITY")
public class JpaGroupAuthority extends AbstractEntity {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "GROUP_ID", referencedColumnName = "ID")
	private JpaGroup group;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")
	private JpaAuthority authority;

	/**
	 * @return group
	 */
	public JpaGroup getGroup() {
		return group;
	}

	/**
	 * @param group
	 */
	public void setGroup(JpaGroup group) {
		this.group = group;
	}

	/**
	 * @return authority
	 */
	public JpaAuthority getAuthority() {
		return authority;
	}

	/**
	 * @param authority
	 */
	public void setAuthority(JpaAuthority authority) {
		this.authority = authority;
	}
}
