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
package org.xaloon.core.api.image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.storage.InputStreamContainer;

public class ImageOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ImageSize imageSize;

	private List<ImageSize> additionalImageSizes = new ArrayList<ImageSize>();

	private boolean modifyPath;

	private boolean generateUuid;

	private String pathPrefix;

	private InputStreamContainer imageInputStreamContainer;

	private Long albumId;

	private Class<? extends Album> albumEntityClass;


	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * Construct.
	 * 
	 * @param imageInputStreamContainer
	 * @param imageSize
	 */
	public ImageOptions(InputStreamContainer imageInputStreamContainer, ImageSize imageSize) {
		this.imageInputStreamContainer = imageInputStreamContainer;
		this.imageSize = imageSize;
	}

	/**
	 * Gets albumId.
	 * 
	 * @return albumId
	 */
	public Long getAlbumId() {
		return albumId;
	}

	/**
	 * Sets albumId.
	 * 
	 * @param albumId
	 *            albumId
	 */
	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	/**
	 * Gets albumEntityClass.
	 * 
	 * @return albumEntityClass
	 */
	public Class<? extends Album> getAlbumEntityClass() {
		return albumEntityClass;
	}

	/**
	 * Sets albumEntityClass.
	 * 
	 * @param albumEntityClass
	 *            albumEntityClass
	 */
	public void setAlbumEntityClass(Class<? extends Album> albumEntityClass) {
		this.albumEntityClass = albumEntityClass;
	}

	/**
	 * Gets imageInputStreamContainer.
	 * 
	 * @return imageInputStreamContainer
	 */
	public InputStreamContainer getImageInputStreamContainer() {
		return imageInputStreamContainer;
	}

	/**
	 * Sets imageInputStreamContainer.
	 * 
	 * @param imageInputStreamContainer
	 *            imageInputStreamContainer
	 */
	public void setImageInputStreamContainer(InputStreamContainer imageInputStreamContainer) {
		this.imageInputStreamContainer = imageInputStreamContainer;
	}


	/**
	 * Gets additionalImageSizes.
	 * 
	 * @return additionalImageSizes
	 */
	public List<ImageSize> getAdditionalImageSizes() {
		return additionalImageSizes;
	}

	/**
	 * Gets imageSize.
	 * 
	 * @return imageSize
	 */
	public ImageSize getImageSize() {
		return imageSize;
	}

	/**
	 * Sets imageSize.
	 * 
	 * @param imageSize
	 *            imageSize
	 */
	public void setImageSize(ImageSize imageSize) {
		this.imageSize = imageSize;
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

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}


}
