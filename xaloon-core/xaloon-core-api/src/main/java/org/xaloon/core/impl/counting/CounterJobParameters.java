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
package org.xaloon.core.impl.counting;

import org.xaloon.core.api.asynchronous.JobParameters;

/**
 * @author vytautas r.
 */
public class CounterJobParameters implements JobParameters {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String counterGroup;

	private Long entityId;

	private Long categoryId;

	private boolean increment = true;


	/**
	 * Gets increment.
	 * 
	 * @return increment
	 */
	public boolean isIncrement() {
		return increment;
	}

	/**
	 * Sets increment.
	 * 
	 * @param increment
	 *            increment
	 */
	public void setIncrement(boolean increment) {
		this.increment = increment;
	}

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
	public String toString() {
		return String.format("Counter group [%s], Category [%s], Entity Id [%s]", counterGroup, categoryId, entityId);
	}
}
