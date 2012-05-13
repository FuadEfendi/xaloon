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

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.xaloon.core.api.audit.model.AuditEntity;
import org.xaloon.core.api.audit.model.AuditEntityItem;
import org.xaloon.core.jpa.model.AbstractEntity;

/**
 * @author vytautas r.
 */
@Cacheable
@Entity
@Table(name = "XAL_AUDIT_ENTITY_ITEM")
public class JpaAuditEntityItem extends AbstractEntity implements AuditEntityItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "AUDIT_ENTITY_ID", referencedColumnName = "ID")
	private JpaAuditEntity auditEntity;

	@Column(name = "FIELD_NAME", length = 255)
	private String name;

	@Column(name = "FIELD_VALUE", length = 2000)
	private String value;

	private transient boolean key;

	/**
	 * Gets auditEntity.
	 * 
	 * @return auditEntity
	 */
	public JpaAuditEntity getAuditEntity() {
		return auditEntity;
	}

	/**
	 * Sets auditEntity.
	 * 
	 * @param auditEntity
	 *            auditEntity
	 */
	public void setAuditEntity(AuditEntity auditEntity) {
		this.auditEntity = (JpaAuditEntity)auditEntity;
	}

	/**
	 * Gets name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets value.
	 * 
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets value.
	 * 
	 * @param value
	 *            value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets key.
	 * 
	 * @return key
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * Sets key.
	 * 
	 * @param key
	 *            key
	 */
	public void setKey(boolean key) {
		this.key = key;
	}
}
