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
package org.xaloon.core.jpa.message.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import org.xaloon.core.api.message.model.Message;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.model.AbstractEntity;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;
import org.xaloon.core.jpa.user.model.AbstractUser;

/**
 * @author vytautas r.
 */
@MappedSuperclass
public abstract class JpaMessage extends AbstractEntity implements Message {
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "FROM_USER_ID", referencedColumnName = "ID")
	private AbstractUser fromUser;

	@Column(name = "MESSAGE", nullable = false)
	private String message;

	@ManyToMany
	@JoinTable(name = "XAL_MESSAGE_ATTACHMENTS", joinColumns = @JoinColumn(name = "MESSAGE_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "FILE_DESCRIPTOR_ID", referencedColumnName = "ID"))
	private List<JpaFileDescriptor> attachments = new ArrayList<JpaFileDescriptor>();

	public AbstractUser getFromUser() {
		return fromUser;
	}

	/**
	 * @param fromUser
	 */
	public void setFromUser(AbstractUser fromUser) {
		this.fromUser = fromUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<JpaFileDescriptor> getAttachments() {
		return attachments;
	}

	/**
	 * @param attachments
	 */
	public void setAttachments(List<JpaFileDescriptor> attachments) {
		this.attachments = attachments;
	}

	@Override
	public void setFromUser(User fromUser) {
		this.fromUser = (AbstractUser)fromUser;
	}
}
