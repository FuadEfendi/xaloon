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
package org.xaloon.wicket.plugin.image.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.model.BookmarkableEntity;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;
import org.xaloon.core.jpa.user.model.JpaUser;

/**
 * @author vytautas r.
 */
@MappedSuperclass
public abstract class AbstractAlbum extends BookmarkableEntity implements Album {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DESCRIPTION", length = 4000)
	private String description;

	@ManyToOne
	@JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
	private JpaAlbum parent;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "THUMBNAIL_ID", referencedColumnName = "ID")
	private JpaFileDescriptor thumbnail;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	private JpaUser owner;

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}


	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public JpaFileDescriptor getThumbnail() {
		return thumbnail;
	}


	/**
	 * @see org.xaloon.core.api.image.model.Album#setThumbnail(org.xaloon.core.jpa.storage.model.MappableFileDescriptor)
	 */
	@Override
	public void setThumbnail(FileDescriptor thumbnail) {
		if (thumbnail != null && !(thumbnail instanceof JpaFileDescriptor)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.thumbnail = (JpaFileDescriptor)thumbnail;
	}


	public JpaAlbum getParent() {
		return parent;
	}


	/**
	 * @see org.xaloon.core.api.image.model.Album#setParent(org.xaloon.core.api.image.model.Album)
	 */
	@Override
	public void setParent(Album parent) {
		if (parent != null && !(parent instanceof JpaAlbum)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.parent = (JpaAlbum)parent;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Album#getOwner()
	 */
	@Override
	public JpaUser getOwner() {
		return owner;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Album#setOwner(org.xaloon.core.jpa.user.model.JpaUser)
	 */
	@Override
	public void setOwner(User owner) {
		if (owner != null && !(owner instanceof JpaUser)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.owner = (JpaUser)owner;
	}
}
