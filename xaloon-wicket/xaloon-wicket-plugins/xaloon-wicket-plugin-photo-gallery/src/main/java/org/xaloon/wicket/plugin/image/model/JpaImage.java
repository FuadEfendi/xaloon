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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;
import org.xaloon.core.jpa.user.model.JpaUser;

/**
 * @author vytautas r.
 */
@Entity
@DiscriminatorValue("IMAGE")
@Table(name = "XAL_IMAGE")
public class JpaImage extends JpaFileDescriptor implements Image {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "THUMBNAIL_ID", referencedColumnName = "ID")
	private JpaFileDescriptor thumbnail;
	
	@Column(name = "CUSTOM_ORDER")
	private int customOrder = 9999;
	
	@Column(name = "TITLE")
	private String title;

	@Column(name = "DESCRIPTION", length=255)
	private String description;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "USER_ID", referencedColumnName = "ID")
	private JpaUser owner;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "image", orphanRemoval = true)
	private List<JpaImageTag> tags = new ArrayList<JpaImageTag>();

	/**
	 * Transient fields
	 */

	/** The image width */
	private transient int width;

	/** The image height */
	private transient int height;

	private transient boolean resize;

	private transient boolean modifyPath;

	private transient boolean generateUuid;

	private transient String pathPrefix;

	/**
	 * @see org.xaloon.core.api.image.model.Image#getThumbnail()
	 */
	@Override
	public JpaFileDescriptor getThumbnail() {
		return thumbnail;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#setThumbnail(FileDescriptor)
	 */
	@Override
	public void setThumbnail(FileDescriptor thumbnail) {
		if (thumbnail != null && !(thumbnail instanceof JpaFileDescriptor)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.thumbnail = (JpaFileDescriptor)thumbnail;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#getTitle()
	 */
	@Override
	public String getTitle() {
		return title;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#setTitle(java.lang.String)
	 */
	@Override
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#getDescription()
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#setDescription(java.lang.String)
	 */
	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#getOwner()
	 */
	@Override
	public JpaUser getOwner() {
		return owner;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#setOwner(org.xaloon.core.jpa.user.model.JpaUser)
	 */
	@Override
	public void setOwner(User owner) {
		if (owner != null && !(owner instanceof JpaUser)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.owner = (JpaUser)owner;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#getTags()
	 */
	@Override
	public List<JpaImageTag> getTags() {
		return tags;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Image#setTags(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setTags(List<? extends KeyValue<String, String>> tags) {
		/**
		 * assume that if at first element failed then all elements should fail. Of cource it is possible to switch values, but why somebody should do
		 * this?
		 */
		if (tags != null && !tags.isEmpty() && !(tags.get(0) instanceof JpaImageTag)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.tags = (List<JpaImageTag>)tags;
	}

	/**
	 * Gets width.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets width.
	 * 
	 * @param width
	 *            width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets height.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets height.
	 * 
	 * @param height
	 *            height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets resize.
	 * 
	 * @return resize
	 */
	public boolean isResize() {
		return resize;
	}

	/**
	 * Sets resize.
	 * 
	 * @param resize
	 *            resize
	 */
	public void setResize(boolean resize) {
		this.resize = resize;
	}

	/**
	 * Gets modifyPath.
	 * 
	 * @return modifyPath
	 */
	public boolean isModifyPath() {
		return modifyPath;
	}

	/**
	 * Sets modifyPath.
	 * 
	 * @param modifyPath
	 *            modifyPath
	 */
	public void setModifyPath(boolean modifyPath) {
		this.modifyPath = modifyPath;
	}

	/**
	 * Gets generateUuid.
	 * 
	 * @return generateUuid
	 */
	public boolean isGenerateUuid() {
		return generateUuid;
	}

	/**
	 * Sets generateUuid.
	 * 
	 * @param generateUuid
	 *            generateUuid
	 */
	public void setGenerateUuid(boolean generateUuid) {
		this.generateUuid = generateUuid;
	}

	/**
	 * Gets pathPrefix.
	 * 
	 * @return pathPrefix
	 */
	public String getPathPrefix() {
		return pathPrefix;
	}

	/**
	 * Sets pathPrefix.
	 * 
	 * @param pathPrefix
	 *            pathPrefix
	 */
	public void setPathPrefix(String pathPrefix) {
		this.pathPrefix = pathPrefix;
	}

	/**
	 * @return the customOrder
	 */
	public int getCustomOrder() {
		return customOrder;
	}

	/**
	 * @param customOrder the customOrder to set
	 */
	public void setCustomOrder(int customOrder) {
		this.customOrder = customOrder;
	}
}
