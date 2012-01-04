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
package org.xaloon.core.impl.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.xaloon.core.api.message.model.Message;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public class DefaultMessage implements Message {
	private static final long serialVersionUID = 1L;

	private Date createDate;

	private User fromUser;

	private String subject;

	private String message;

	private Date updateDate = new Date();

	private List<FileDescriptor> attachments = new ArrayList<FileDescriptor>();

	/**
	 * @see org.xaloon.core.message.Message#getCreateDate()
	 */
	@Override
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @see org.xaloon.core.message.Message#setCreateDate(java.util.Date)
	 */
	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @see org.xaloon.core.message.Message#getFromUser()
	 */
	@Override
	public User getFromUser() {
		return fromUser;
	}

	/**
	 * @see org.xaloon.core.message.Message#setFromUser(org.xaloon.core.api.user.model.User)
	 */
	@Override
	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	/**
	 * @see org.xaloon.core.message.Message#getMessage()
	 */
	@Override
	public String getMessage() {
		return message;
	}

	/**
	 * @see org.xaloon.core.message.Message#setMessage(java.lang.String)
	 */
	@Override
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @see org.xaloon.core.message.Message#getAttachments()
	 */
	@Override
	public List<FileDescriptor> getAttachments() {
		return attachments;
	}

	@Override
	public Long getId() {
		throw new RuntimeException("Not persistable instance!");
	}

	@Override
	public boolean isNew() {
		return false;
	}

	@Override
	public void setId(Long id) {
		throw new RuntimeException("Not persistable instance!");
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
}
