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
package org.xaloon.core.jpa.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.xaloon.core.api.persistence.Persistable;

/**
 * Base entity for JPA
 * 
 * @author vytautas r.
 */
@MappedSuperclass
public abstract class AbstractEntity implements Persistable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID", nullable = false)
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Column(name = "CREATE_DATE", nullable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;

	@Column(name = "UPDATE_DATE", updatable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;

	/**
	 * @return unique identifier of entity
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return date when instance was created
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @param updateDate
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return date when instance was updated
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @return true if entity is not persisted yet
	 */
	public boolean isNew() {
		return (id == null);
	}

	@PreUpdate
	protected void beforeUpdate() {
		setUpdateDate(new Date());
	}

	@PrePersist
	protected void beforeCreate() {
		if (getCreateDate() == null) {
			Date cd = new Date();
			setCreateDate(cd);
			setUpdateDate(cd);
		}
	}
}
