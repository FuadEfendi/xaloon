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
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.message.model.TextMessage;
import org.xaloon.core.jpa.classifier.model.JpaClassifierItem;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_TEXT_MESSAGE")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "MESSAGE_TYPE", discriminatorType = DiscriminatorType.STRING)
public class JpaTextMessage extends JpaMessage implements TextMessage {
	private static final long serialVersionUID = 1L;


	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CLASSIFIER_ITEM_FOLDER_ID", referencedColumnName = "ID")
	private JpaClassifierItem folder;

	@Column(name = "SUBJECT")
	private String subject;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ClassifierItem getFolder() {
		return folder;
	}

	/**
	 * @param folder
	 */
	public void setFolder(JpaClassifierItem folder) {
		this.folder = folder;
	}

	@Override
	public void setFolder(ClassifierItem messageFolder) {
		folder = (JpaClassifierItem)messageFolder;
	}
}
