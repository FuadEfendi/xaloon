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
package org.xaloon.core.api.user.model;

import java.util.List;

import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.Persistable;
import org.xaloon.core.api.storage.FileDescriptor;

/**
 * @author vytautas r.
 */
public interface User extends Persistable {
	/**
	 * Who wrote the comment, empty if anonymous
	 * 
	 * @return username
	 */
	String getUsername();

	/**
	 * @param username
	 */
	void setUsername(String username);

	/**
	 * @return first name
	 */
	String getFirstName();

	/**
	 * @param firstName
	 */
	void setFirstName(String firstName);

	/**
	 * @return last name
	 */
	String getLastName();

	/**
	 * @param lastName
	 */
	void setLastName(String lastName);

	/**
	 * @return email
	 */
	String getEmail();

	/**
	 * @param email
	 */
	void setEmail(String email);

	/**
	 * @return timezone of this user
	 */
	String getTimezone();

	/**
	 * @param timezone
	 */
	void setTimezone(String timezone);

	/**
	 * @return signature
	 */
	String getSignature();

	/**
	 * @param signature
	 */
	void setSignature(String signature);

	/**
	 * @return true if user is from external system
	 */
	boolean isExternal();

	/**
	 * @param external
	 */
	void setExternal(boolean external);

	/**
	 * @return website of user
	 */
	String getWebsite();

	/**
	 * @param website
	 */
	void setWebsite(String website);

	/**
	 * @return photo image
	 */
	FileDescriptor getPhotoThumbnail();

	/**
	 * @param photoThumbnail
	 */
	void setPhotoThumbnail(FileDescriptor photoThumbnail);

	/**
	 * @return display name of user like first name + last name
	 */
	String getDisplayName();

	/**
	 * Gets custom list of user properties
	 * 
	 * @return properties key=value pair of String properties
	 */
	List<? extends KeyValue<String, String>> getProperties();

	/**
	 * Sets cutom list of user properties.
	 * 
	 * @param properties
	 *            list of properties as key=value pairs
	 */
	void setProperties(List<? extends KeyValue<String, String>> properties);

}
