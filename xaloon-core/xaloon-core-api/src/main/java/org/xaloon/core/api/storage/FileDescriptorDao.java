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

import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * @author vytautas r.
 */
public interface FileDescriptorDao extends Serializable {

	/**
	 * @param fileDescriptor
	 * @return reference to saved file descriptor
	 */
	FileDescriptor save(FileDescriptor fileDescriptor);

	/**
	 * @param fileDescriptor
	 * @param fileIdentifier
	 * @return reference to saved file descriptor
	 */
	FileDescriptor save(FileDescriptor fileDescriptor, KeyValue<String, String> fileIdentifier);

	/**
	 * @param path
	 * @return file descriptor if exists
	 */
	FileDescriptor getFileDescriptorByPath(String path);

	/**
	 * @return concrete implementation of file descriptor
	 */
	FileDescriptor newFileDescriptor();

	/**
	 * @param fileDescriptor
	 *            file to delete
	 */
	void delete(FileDescriptor fileDescriptor);

	/**
	 * Loads file descriptor entity by it's unique identifier
	 * 
	 * @param id
	 *            unique identifier to use for loading
	 * @return instance of selected {@link FileDescriptor}
	 */
	FileDescriptor getFileDescriptorById(Long id);
}
