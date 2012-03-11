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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xaloon.core.api.audit.annotation.Auditable;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.model.BookmarkableEntity;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 * @since 1.3
 */
@Cacheable
@Entity
@Auditable
@Table(name = "XAL_CLASSIFIER_ITEM")
public class JpaClassifierItem extends BookmarkableEntity implements ClassifierItem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CLASSIFIER_ID", referencedColumnName = "ID")
	private JpaClassifier classifier;

	@Auditable
	@Column(name = "CODE", nullable = false)
	private String code;

	@Column(name = "NAME", nullable = false)
	private String name;


	@ManyToOne
	@JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
	private JpaClassifierItem parent;

	/**
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return name of classifier item
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param classifier
	 */
	public void setClassifier(JpaClassifier classifier) {
		this.classifier = classifier;
	}

	/**
	 * @return classifier instance, associated to the classifier item
	 */
	public JpaClassifier getClassifier() {
		return classifier;
	}

	/**
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return code of the classifier item
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param parent
	 */
	public void setParent(JpaClassifierItem parent) {
		this.parent = parent;
	}

	/**
	 * @return parent classifier item
	 */
	public JpaClassifierItem getParent() {
		return parent;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof JpaClassifierItem)) {
			return false;
		}
		JpaClassifierItem item = (JpaClassifierItem)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getCode(), item.getCode());
		equalsBuilder.append(getName(), item.getName());
		equalsBuilder.append(getClassifier().getType(), item.getClassifier().getType());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getCode());
		hashCodeBuilder.append(getName());
		hashCodeBuilder.append(getClassifier().getType());
		return hashCodeBuilder.hashCode();
	}

	@Override
	public String toString() {
		return String.format("[%s] code=%s, name=%s", this.getClass().getSimpleName(), getCode(), getName());
	}

	@Override
	@PrePersist
	protected void beforeCreate() {
		super.beforeCreate();
		setCode(getCode().toUpperCase());
		String path = UrlUtil.encode(getName());
		if (getParent() != null) {
			path = UrlUtil.mergeIntoUrl(getParent().getPath(), path);
		}
		setPath(path);
	}
}
