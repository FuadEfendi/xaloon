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
package org.xaloon.core.jpa.plugin.comment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.xaloon.core.api.plugin.comment.Comment;
import org.xaloon.core.jpa.message.model.JpaMessage;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_COMMENT")
public class JpaComment extends JpaMessage implements Comment {
	private static final long serialVersionUID = 1L;

	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;

	@Column(name = "ENTITY_ID", nullable = false)
	private Long entityId;

	@Column(name = "CATEGORY_ID", nullable = false)
	private Long categoryId;

	@Column(name = "PATH")
	private String path;

	@Column(name = "INAPPROPRIATE")
	private boolean inappropriate;

	/**
	 * Gets inappropriate.
	 * 
	 * @return inappropriate
	 */
	public boolean isInappropriate() {
		return inappropriate;
	}

	/**
	 * Sets inappropriate.
	 * 
	 * @param inappropriate
	 *            inappropriate
	 */
	public void setInappropriate(boolean inappropriate) {
		this.inappropriate = inappropriate;
	}

	/**
	 * @return path where comment was posted
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
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

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
