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

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.util.UrlUtil;

/**
 * @author vytautas r.
 */
@Cacheable
@Entity
@Table(name = "XAL_ALBUM", uniqueConstraints = { @UniqueConstraint(columnNames = { "PATH" }) })
public class JpaAlbum extends AbstractAlbum {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "XAL_ALBUM_IMAGES", joinColumns = { @JoinColumn(name = "ALBUM_ID") }, inverseJoinColumns = { @JoinColumn(name = "IMAGE_ID") })
	private List<JpaImage> images = new ArrayList<JpaImage>();

	/**
	 * @see org.xaloon.core.api.image.model.Album#getImages()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<? extends Image> getImages() {
		return images;
	}

	/**
	 * @see org.xaloon.core.api.image.model.Album#setImages(java.util.List)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void setImages(List<? extends Image> images) {
		/**
		 * assume that if at first element failed then all elements should fail. Of cource it is possible to switch values, but why somebody should do
		 * this?
		 */
		if (images != null && !images.isEmpty() && !(images.get(0) instanceof JpaImage)) {
			throw new IllegalArgumentException("Wrong type for provided argument!");
		}
		this.images = (List<JpaImage>)images;
	}

	@Override
	protected void beforeCreate() {
		super.beforeCreate();
		setPath(UrlUtil.encode(getTitle()));
	}
}
