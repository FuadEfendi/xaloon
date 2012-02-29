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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "XAL_COUNTER")
public class JpaCounterEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private JpaCounterId counterId;

	@Column(name = "VALUE_COUNT", nullable = false)
	private Long count;

	/**
	 * Gets count.
	 * 
	 * @return count
	 */
	public Long getCount() {
		return count;
	}


	/**
	 * Sets count.
	 * 
	 * @param count
	 *            count
	 */
	public void setCount(Long count) {
		this.count = count;
	}


	/**
	 * Gets counterId.
	 * 
	 * @return counterId
	 */
	public JpaCounterId getCounterId() {
		return counterId;
	}


	/**
	 * Sets counterId.
	 * 
	 * @param counterId
	 *            counterId
	 */
	public void setCounterId(JpaCounterId counterId) {
		this.counterId = counterId;
	}
}
