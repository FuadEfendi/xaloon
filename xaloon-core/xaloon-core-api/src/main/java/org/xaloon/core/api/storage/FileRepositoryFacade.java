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

import java.io.Serializable;

/**
 * <p>
 * File repository related actions, such as storing file into repository, retrieve file from repository, delete file from repository.
 * </p>
 * <p>
 * Repository implementation may vary from case to case. Default implementation uses Apache Jackrabbit to store files into repository.
 * </p>
 * <p>
 * File repository may store images, documents and other types of file
 * </p>
 * 
 * @author vytautas r.
 * @version 1.1, 09/19/10
 * @since 1.3
 */

public interface FileRepositoryFacade extends Serializable {
	/**
	 * 
	 */
	String CLASSIFIER_FILE_STORAGE_CATEGORY = "FILE_STORAGE_CATEGORY";

	/**
	 * Stores specified input stream into repository
	 * 
	 * @param fileDescriptor
	 *            file information
	 * @param inputStreamContainer
	 *            stream actual file
	 * @return unique identifier of stored file in repository
	 */
	FileDescriptor storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer);


	/**
	 * Returns actual file as input stream from repository by provided name
	 * 
	 * @param path
	 *            full path to the specified file in repository
	 * @return input stream of specified file. null - if there is no result.
	 */
	byte[] getFileByPath(String path);

	/**
	 * Returns file input stream by unique identifier. Unique identifier is provided by specific file repository implementation.
	 * 
	 * @param path
	 *            full path to the specified file in repository
	 * @return input stream of specified file. null - if there is no result.
	 */
	FileDescriptor getFileDescriptorByPath(String path);

	/**
	 * Delete file descriptor and actual file stream from database and file repository
	 * 
	 * @param fileDescriptor
	 *            file to delete
	 */
	void delete(FileDescriptor fileDescriptor);

	/**
	 * Delete actual file stream from file repository by it's descriptor
	 * 
	 * @param fileDescriptor
	 *            file to delete
	 */
	void deleteFile(FileDescriptor fileDescriptor);

	/**
	 * Delete file/folder by it's path
	 * 
	 * @param path
	 *            path to the file
	 * @return true if file was deleted successfully
	 */
	boolean deleteByPath(String path);

	/**
	 * Check if file exists in repository
	 * 
	 * @param path
	 *            path to of the file descriptor
	 * @return true if file is found in repository, otherwise - false
	 */
	boolean existsFile(String path);


	/**
	 * Creates new instance of {@link FileDescriptor} implementation
	 * 
	 * @return new instance of file descriptor
	 */
	FileDescriptor newFileDescriptor();
}
