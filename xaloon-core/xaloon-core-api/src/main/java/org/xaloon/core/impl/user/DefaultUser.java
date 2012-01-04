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
package org.xaloon.core.impl.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public class DefaultUser implements User {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String firstName;

	private String lastName;

	private String email;

	private String timezone;

	private String signature;

	private boolean external;

	private String website;

	private FileDescriptor photoThumbnail;

	private List<? extends KeyValue<String, String>> properties = new ArrayList<KeyValue<String, String>>();

	private transient String displayName;

	private Date createDate = new Date();

	private Date updateDate = new Date();


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
	 * @see org.xaloon.core.user.User#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * @see org.xaloon.core.user.User#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @see org.xaloon.core.user.User#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @see org.xaloon.core.user.User#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @see org.xaloon.core.user.User#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}

	/**
	 * @see org.xaloon.core.user.User#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @see org.xaloon.core.user.User#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}

	/**
	 * @see org.xaloon.core.user.User#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @see org.xaloon.core.user.User#getTimezone()
	 */
	@Override
	public String getTimezone() {
		return timezone;
	}

	/**
	 * @see org.xaloon.core.user.User#setTimezone(java.lang.String)
	 */
	@Override
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	/**
	 * @see org.xaloon.core.user.User#getSignature()
	 */
	@Override
	public String getSignature() {
		return signature;
	}

	/**
	 * @see org.xaloon.core.user.User#setSignature(java.lang.String)
	 */
	@Override
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @see org.xaloon.core.user.User#isExternal()
	 */
	@Override
	public boolean isExternal() {
		return external;
	}

	/**
	 * @see org.xaloon.core.user.User#setExternal(boolean)
	 */
	@Override
	public void setExternal(boolean external) {
		this.external = external;
	}

	/**
	 * @see org.xaloon.core.user.User#getWebsite()
	 */
	@Override
	public String getWebsite() {
		return website;
	}

	/**
	 * @see org.xaloon.core.user.User#setWebsite(java.lang.String)
	 */
	@Override
	public void setWebsite(String website) {
		this.website = website;
	}

	/**
	 * @see org.xaloon.core.user.User#getPhotoThumbnail()
	 */
	@Override
	public FileDescriptor getPhotoThumbnail() {
		return photoThumbnail;
	}

	/**
	 * @see org.xaloon.core.user.User#setPhotoThumbnail(org.xaloon.core.api.storage.FileDescriptor)
	 */
	@Override
	public void setPhotoThumbnail(FileDescriptor photoThumbnail) {
		this.photoThumbnail = photoThumbnail;
	}

	/**
	 * @see org.xaloon.core.user.User#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		if (!StringUtils.isEmpty(displayName)) {
			return displayName;
		}
		StringBuilder result = new StringBuilder();
		if (!StringUtils.isEmpty(firstName)) {
			result.append(firstName);
		}
		if (!StringUtils.isEmpty(lastName)) {
			result.append(DelimiterEnum.SPACE);
			result.append(lastName);
		}
		if (result.length() > 1) {
			displayName = result.toString().trim();
			return displayName;
		} else {
			return username;
		}
	}

	@Override
	public Long getId() {
		throw new RuntimeException("Not persistable instance!");
	}

	@Override
	public boolean isNew() {
		return true;
	}

	/**
	 * Gets properties.
	 * 
	 * @return properties
	 */
	public List<? extends KeyValue<String, String>> getProperties() {
		return properties;
	}

	@Override
	public void setProperties(List<? extends KeyValue<String, String>> properties) {
		this.properties = properties;
	}

	@Override
	public void setId(Long id) {
		throw new RuntimeException("Not persistable instance!");
	}
}
