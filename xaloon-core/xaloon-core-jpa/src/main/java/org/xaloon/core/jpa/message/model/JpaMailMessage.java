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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.xaloon.core.api.message.model.MailMessage;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.user.model.JpaUser;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_MAIL_MESSAGE")
@DiscriminatorValue("MAIL_MESSAGE")
public class JpaMailMessage extends JpaTextMessage implements MailMessage {
	private static final long serialVersionUID = 1L;

	@Column(name = "IS_READ", nullable = false)
	private boolean read;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "TO_USER_ID", referencedColumnName = "ID")
	private JpaUser toUser;

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public User getToUser() {
		return toUser;
	}

	/**
	 * @param toUser
	 */
	public void setToUser(JpaUser toUser) {
		this.toUser = toUser;
	}

	@Override
	public void setToUser(User toUser) {
		this.toUser = (JpaUser)toUser;
	}
}
