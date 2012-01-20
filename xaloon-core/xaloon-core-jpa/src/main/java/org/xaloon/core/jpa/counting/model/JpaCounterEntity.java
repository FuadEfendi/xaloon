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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.xaloon.core.api.counting.CounterEntity;
import org.xaloon.core.jpa.model.AbstractEntity;

@Entity
@Table(name = "XAL_COUNTER")
public class JpaCounterEntity extends AbstractEntity implements CounterEntity {

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

	@Column(name = "VALUE_COUNT", nullable = false)
	private Long count;


	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public String getCounterGroup() {
		return counterGroup;
	}

	public void setCounterGroup(String counterGroup) {
		this.counterGroup = counterGroup;
	}
}
