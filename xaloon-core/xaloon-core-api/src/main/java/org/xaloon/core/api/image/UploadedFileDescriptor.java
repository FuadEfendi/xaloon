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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.HtmlElementEnum;

/**
 * @author vytautas r.
 */
public class UploadedFileDescriptor implements FileDescriptor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** File name */
	private String name;

	private String path;

	private Date createDate;

	private Date updateDate;


	/** Type of file */
	private String mimeType;

	/** File size */
	private Long size;

	/** File physical location identifier */
	private String identifier;

	/** Saved location where file is stored */
	private String location;


	/** File storage service provider bean name */
	private String fileStorageServiceProvider;

	/** The image input stream if available */
	private InputStreamContainer imageInputStreamContainer;

	/**
	 * Gets path.
	 * 
	 * @return path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets path.
	 * 
	 * @param path
	 *            path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets createDate.
	 * 
	 * @return createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * Sets createDate.
	 * 
	 * @param createDate
	 *            createDate
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * Gets updateDate.
	 * 
	 * @return updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * Sets updateDate.
	 * 
	 * @param updateDate
	 *            updateDate
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Gets name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets mimeType.
	 * 
	 * @return mimeType
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * Sets mimeType.
	 * 
	 * @param mimeType
	 *            mimeType
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * Gets size.
	 * 
	 * @return size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * Sets size.
	 * 
	 * @param size
	 *            size
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * Gets identifier.
	 * 
	 * @return identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Sets identifier.
	 * 
	 * @param identifier
	 *            identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Gets location.
	 * 
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets location.
	 * 
	 * @param location
	 *            location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * Gets fileStorageServiceProvider.
	 * 
	 * @return fileStorageServiceProvider
	 */
	public String getFileStorageServiceProvider() {
		return fileStorageServiceProvider;
	}

	/**
	 * Sets fileStorageServiceProvider.
	 * 
	 * @param fileStorageServiceProvider
	 *            fileStorageServiceProvider
	 */
	public void setFileStorageServiceProvider(String fileStorageServiceProvider) {
		this.fileStorageServiceProvider = fileStorageServiceProvider;
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

	public URL getExternalImage() throws MalformedURLException {
		if (isExternal()) {
			return new URL(getPath());
		}
		return null;
	}

	@Override
	public Long getId() {
		return null;
	}

	@Override
	public void setId(Long id) {
	}

	@Override
	public boolean isNew() {
		return true;
	}

	@Override
	public boolean isExternal() {
		return !StringUtils.isEmpty(getPath()) && getPath().startsWith(HtmlElementEnum.PROTOCOL_HTTP.value());
	}
}
