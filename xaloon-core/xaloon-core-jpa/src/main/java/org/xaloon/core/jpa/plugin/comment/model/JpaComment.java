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

	@Column(name = "OBJECT_ID", nullable = false)
	private Long objectId;

	@Column(name = "COMPONENT_ID", nullable = false)
	private Long componentId;

	@Column(name = "PATH")
	private String path;

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

	public Long getComponentId() {
		return componentId;
	}

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
