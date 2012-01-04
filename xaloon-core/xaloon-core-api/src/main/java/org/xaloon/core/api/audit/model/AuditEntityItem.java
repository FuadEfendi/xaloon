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
package org.xaloon.core.api.audit.model;

import org.xaloon.core.api.persistence.Persistable;

/**
 * @author vytautas r.
 */
public interface AuditEntityItem extends Persistable {
	/**
	 * Gets auditEntity.
	 * 
	 * @return auditEntity
	 */
	AuditEntity getAuditEntity();

	/**
	 * Sets auditEntity.
	 * 
	 * @param auditEntity
	 *            auditEntity
	 */
	void setAuditEntity(AuditEntity auditEntity);

	/**
	 * Gets name.
	 * 
	 * @return name
	 */
	String getName();

	/**
	 * Sets name.
	 * 
	 * @param name
	 *            name
	 */
	void setName(String name);

	/**
	 * Gets value.
	 * 
	 * @return value
	 */
	String getValue();

	/**
	 * Sets value.
	 * 
	 * @param value
	 *            value
	 */
	void setValue(String value);

	/**
	 * Gets key.
	 * 
	 * @return key
	 */
	boolean isKey();

	/**
	 * Sets key.
	 * 
	 * @param key
	 *            key
	 */
	void setKey(boolean key);
}
