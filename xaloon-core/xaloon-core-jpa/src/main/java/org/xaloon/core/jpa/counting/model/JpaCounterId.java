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
package org.xaloon.core.jpa.counting.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author vytautas r.
 */
@Embeddable
public class JpaCounterId implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "COUNTER_GROUP_ID", nullable = false)
	private String counterGroup;

	@Column(name = "ENTITY_ID", nullable = false)
	private Long entityId;

	@Column(name = "CATEGORY_ID", nullable = false)
	private Long categoryId;

	/**
	 * Gets counterGroup.
	 * 
	 * @return counterGroup
	 */
	public String getCounterGroup() {
		return counterGroup;
	}

	/**
	 * Sets counterGroup.
	 * 
	 * @param counterGroup
	 *            counterGroup
	 */
	public void setCounterGroup(String counterGroup) {
		this.counterGroup = counterGroup;
	}

	/**
	 * Gets entityId.
	 * 
	 * @return entityId
	 */
	public Long getEntityId() {
		return entityId;
	}

	/**
	 * Sets entityId.
	 * 
	 * @param entityId
	 *            entityId
	 */
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	/**
	 * Gets categoryId.
	 * 
	 * @return categoryId
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * Sets categoryId.
	 * 
	 * @param categoryId
	 *            categoryId
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JpaCounterId)) {
			return false;
		}
		JpaCounterId entity = (JpaCounterId)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getCounterGroup(), entity.getCounterGroup());
		equalsBuilder.append(getCategoryId(), entity.getCategoryId());
		equalsBuilder.append(getEntityId(), entity.getEntityId());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getCounterGroup());
		hashCodeBuilder.append(getCategoryId());
		hashCodeBuilder.append(getEntityId());
		return hashCodeBuilder.hashCode();
	}
}
