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
package org.xaloon.core.api.storage;

import java.util.Date;

import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.persistence.Persistable;


/**
 * @author vytautas r.
 */
public interface FileDescriptor extends Bookmarkable, Persistable {
	/**
	 * Returns the name of file, located in repository
	 * 
	 * @return string form of this file name
	 */
	String getName();

	/**
	 * Registers the name of selected file. It represents file name in repository.
	 * 
	 * @param name
	 *            form of this file name
	 */
	void setName(String name);

	/**
	 * Returns the date when file was modified last time
	 * 
	 * @return date format of file modification
	 */
	Date getUpdateDate();

	/**
	 * Registers date, when the file was modified last time.
	 * 
	 * @param lastModified
	 *            date format of last modification of this component
	 */
	void setUpdateDate(Date lastModified);

	/**
	 * Returns the file type
	 * 
	 * @return string form of file type
	 */
	String getMimeType();

	/**
	 * Registers the file type
	 * 
	 * @param mimeType
	 *            string form of file type stored in this component
	 */
	void setMimeType(String mimeType);

	/**
	 * @return file location if was provided
	 */
	String getLocation();

	/**
	 * @param location
	 *            where the file is or should be stored
	 */
	void setLocation(String location);

	/**
	 * Registers file size
	 * 
	 * @param size
	 *            in bytes
	 */
	void setSize(Long size);

	/**
	 * Returns the size of this file
	 * 
	 * @return file size in bytes
	 */
	Long getSize();

	/**
	 * Returns unique identifier of physical file storage location
	 * 
	 * @return unique identifier how easily file can be found
	 */
	String getIdentifier();

	/**
	 * Sets unique identifier of physical file storage location
	 * 
	 * @param identifier
	 *            string representation of physical storage location
	 */
	void setIdentifier(String identifier);

	/**
	 * Checks if file path starts with 'http'
	 * 
	 * @return true if file is loaded from external link
	 */
	boolean isExternal();

	/**
	 * Returns file storage service provider bean name. It might be used if mixes are used to store files
	 * 
	 * @return string representation of service provider bean name
	 */
	String getFileStorageServiceProvider();

	/**
	 * 
	 * @param fileStorageServiceProvider
	 */
	void setFileStorageServiceProvider(String fileStorageServiceProvider);

	/**
	 * Transient fields
	 */

	/**
	 * @return temporary image holder
	 */
	InputStreamContainer getImageInputStreamContainer();

	/**
	 * @param imageInputStreamContainer
	 */
	void setImageInputStreamContainer(InputStreamContainer imageInputStreamContainer);
}