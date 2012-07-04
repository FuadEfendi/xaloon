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

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.xaloon.core.api.audit.annotation.Auditable;
import org.xaloon.core.api.bookmark.Bookmarkable;

/**
 * Contains bookmarkable path of entity.
 * 
 * @author vytautas r.
 * @since 1.5
 */
@MappedSuperclass
public abstract class BookmarkableEntity extends AbstractEntity implements Bookmarkable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "PATH", nullable = false)
	@Auditable
	private String path;

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return bookmarkable path of the entity
	 */
	public String getPath() {
		return path;
	}
}
