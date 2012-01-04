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
package org.xaloon.core.api.persistence;

import java.io.Serializable;
import java.util.Date;

/**
 * @author vytautas r.
 */
public interface Persistable extends Serializable {
	/**
	 * @return unique object identifier
	 */
	Long getId();

	/**
	 * Unique identifier to set
	 * 
	 * @param id
	 */
	void setId(Long id);

	/**
	 * @return true if entity is not stored into database yet
	 */
	boolean isNew();

	/**
	 * @return date when instance was created
	 */
	Date getCreateDate();

	/**
	 * @param createDate
	 */
	void setCreateDate(Date createDate);

	/**
	 * @param updateDate
	 */
	void setUpdateDate(Date updateDate);

	/**
	 * @return date when instance was updated
	 */
	Date getUpdateDate();
}
