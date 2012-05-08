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
package org.xaloon.core.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xaloon.core.api.persistence.CategoryPrimaryKey;


@Embeddable
public class JpaCategoryPrimaryKey implements CategoryPrimaryKey {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ENTITY_ID", nullable = false)
	private Long entityId;

	@Column(name = "CATEGORY_ID", nullable = false)
	private Long categoryId;

	/**
	 * Construct.
	 */
	public JpaCategoryPrimaryKey() {

	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param trackingCategoryId
	 */
	public JpaCategoryPrimaryKey(Long id, Long trackingCategoryId) {
		entityId = id;
		categoryId = trackingCategoryId;
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
		if (obj == null || !(obj instanceof JpaCategoryPrimaryKey)) {
			return false;
		}
		JpaCategoryPrimaryKey id = (JpaCategoryPrimaryKey)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getEntityId(), id.getEntityId());
		equalsBuilder.append(getCategoryId(), id.getCategoryId());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getEntityId());
		hashCodeBuilder.append(getCategoryId());
		return hashCodeBuilder.hashCode();
	}
}
