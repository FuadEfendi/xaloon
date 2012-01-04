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
package org.xaloon.core.jpa.classifier.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.xaloon.core.api.classifier.Classifier;
import org.xaloon.core.jpa.model.AbstractEntity;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 * @since 1.3
 */
@Cacheable
@Entity
@Table(name = "XAL_CLASSIFIER", uniqueConstraints = @UniqueConstraint(columnNames = { "CL_TYPE" }))
public class JpaClassifier extends AbstractEntity implements Classifier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "CL_TYPE", nullable = false)
	private String type;

	@Column(name = "CL_NAME", nullable = false)
	private String name;

	/**
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @see ClassifierType
	 * @return type of classifier
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return name of classifier
	 */
	public String getName() {
		return name;
	}

	@Override
	@PrePersist
	public void beforeCreate() {
		super.beforeCreate();
		setType(getType().toUpperCase());
	}

	@Override
	public String toString() {
		return String.format("[%s] type=%s, name=%s", this.getClass().getSimpleName(), getType(), getName());
	}
}
