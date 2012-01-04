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
package org.xaloon.core.jpa.audit.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.xaloon.core.api.audit.model.AuditEntity;
import org.xaloon.core.api.audit.model.AuditEntityItem;
import org.xaloon.core.api.audit.model.AuditState;
import org.xaloon.core.jpa.model.BookmarkableEntity;

/**
 * @author vytautas r.
 */
@Cacheable
@Entity
@Table(name = "XAL_AUDIT_ENTITY")
public class JpaAuditEntity extends BookmarkableEntity implements AuditEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@OneToMany(mappedBy = "auditEntity", fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, orphanRemoval = true)
	@OrderBy(value = "id")
	private List<JpaAuditEntityItem> auditEntityItems = new ArrayList<JpaAuditEntityItem>();

	@Column(name = "AUDIT_STATE", nullable = false)
	@Enumerated(EnumType.STRING)
	private AuditState auditState;

	@Column(name = "AUDITABLE_NAME", nullable = false)
	private String auditableName;

	/**
	 * Gets auditEntityItems.
	 * 
	 * @return auditEntityItems
	 */
	public List<? extends AuditEntityItem> getAuditEntityItems() {
		return auditEntityItems;
	}

	/**
	 * Sets auditEntityItems.
	 * 
	 * @param auditEntityItems
	 *            auditEntityItems
	 */
	public void setAuditEntityItems(List<JpaAuditEntityItem> auditEntityItems) {
		this.auditEntityItems = auditEntityItems;
	}

	/**
	 * Gets auditState.
	 * 
	 * @return auditState
	 */
	public AuditState getAuditState() {
		return auditState;
	}

	/**
	 * Sets auditState.
	 * 
	 * @param auditState
	 *            auditState
	 */
	public void setAuditState(AuditState auditState) {
		this.auditState = auditState;
	}

	/**
	 * Gets auditableName.
	 * 
	 * @return auditableName
	 */
	public String getAuditableName() {
		return auditableName;
	}

	/**
	 * Sets auditableName.
	 * 
	 * @param auditableName
	 *            auditableName
	 */
	public void setAuditableName(String auditableName) {
		this.auditableName = auditableName;
	}
}
