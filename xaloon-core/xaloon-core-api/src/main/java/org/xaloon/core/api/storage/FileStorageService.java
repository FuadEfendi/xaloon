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
import java.util.Map;

import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * @author vytautas r.
 */
public interface FileStorageService extends Serializable {
	/**
	 * user email as a parameter
	 */
	String PARAMETER_USER_EMAIL = "userEmail";

	/**
	 * external token for picasa services usage
	 */
	String PARAMETER_USER_TOKEN = "userToken";

	/**
	 * JPA file storage service implementation facade name
	 */
	String FILE_STORAGE_SERVICE_JPA = "jpaFileStorageService";

	/**
	 * Stores actual input stream into storage
	 * 
	 * @param fileDescriptor
	 * @param inputStreamContainer
	 * @return unique identifier where file is stored. key - path, value - identifier. usually they are the same, but depending on implementation key
	 *         might be http link to the external image
	 */
	KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer);

	/**
	 * @param fileDescriptor
	 * @param inputStreamContainer
	 * @param additionalProperties
	 * @return unique identifier where file is stored. key - path, value - identifier. usually they are the same, but depending on implementation key
	 *         might be http link to the external image
	 */
	KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer,
		Map<String, Object> additionalProperties);

	/**
	 * Returns file from storage as byte array
	 * 
	 * @param identifier
	 * @return actual byte array of file
	 */
	byte[] getByteArrayByIdentifier(String identifier);

	/**
	 * Deletes from storage by unique identifier
	 * 
	 * @param uniqueIdentifier
	 * @return true if file was deleted
	 */
	boolean delete(String uniqueIdentifier);

	/**
	 * Returns the name of implementation class, the same as in @Named() attribute
	 * 
	 * @return
	 */
	String getName();

}
