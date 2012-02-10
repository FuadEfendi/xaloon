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
package org.xaloon.core.jpa.storage.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.HtmlElementEnum;
import org.xaloon.core.jpa.model.BookmarkableEntity;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_FILE_DESCRIPTOR")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DESCRIPTOR_TYPE", discriminatorType = DiscriminatorType.STRING)
public class JpaFileDescriptor extends BookmarkableEntity implements FileDescriptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** File name */
	@Column(name = "FILE_NAME", nullable = false)
	private String name;

	/** Type of file */
	@Column(name = "MIME_TYPE")
	private String mimeType;

	/** File size */
	@Column(name = "FILE_SIZE")
	private Long size;

	/** File physical location identifier */
	@Column(name = "FILE_IDENTIFIER")
	private String identifier;

	/** Saved location where file is stored */
	@Column(name = "FILE_LOCATION")
	private String location;


	/** File storage service provider bean name */
	@Column(name = "STORAGE_SERVICE_PROVIDER")
	private String fileStorageServiceProvider;

	/** The image input stream if available */
	private transient InputStreamContainer imageInputStreamContainer;

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

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Override
	public boolean isExternal() {
		return !StringUtils.isEmpty(getPath()) && getPath().startsWith(HtmlElementEnum.PROTOCOL_HTTP.value());
	}
}
