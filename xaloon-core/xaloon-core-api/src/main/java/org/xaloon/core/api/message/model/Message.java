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
package org.xaloon.core.api.message.model;

import java.util.Date;
import java.util.List;

import org.xaloon.core.api.persistence.Persistable;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public interface Message extends Persistable {

	/**
	 * @return message creating date
	 */
	Date getCreateDate();

	/**
	 * @param messageCreateDate
	 */
	void setCreateDate(Date messageCreateDate);

	/**
	 * @return user who sent the message
	 */
	User getFromUser();

	/**
	 * @param fromUser
	 */
	void setFromUser(User fromUser);

	/**
	 * @return message body
	 */
	String getMessage();

	/**
	 * @param messageBody
	 */
	void setMessage(String messageBody);

	/**
	 * @return list of attachments for this message
	 */
	List<? extends FileDescriptor> getAttachments();
}
