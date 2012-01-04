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
package org.xaloon.core.api.plugin.comment;

import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.message.model.Message;

/**
 * @author vytautas r.
 */
public interface Comment extends Message, Bookmarkable {
	/**
	 * This is object id which comment was written on
	 * 
	 * @return object id
	 */
	Long getObjectId();

	/**
	 * @param objectId
	 */
	void setObjectId(Long objectId);

	/**
	 * Several systems may have the same object id's. This is to identify unique system
	 * 
	 * @return component enumeration
	 */
	Long getComponentId();

	/**
	 * @param componentId
	 */
	void setComponentId(Long componentId);


	/**
	 * Check if comment is enabled or disabled due to spam
	 * 
	 * @return true if comment is enabled and visible to end users
	 */
	boolean isEnabled();

	/**
	 * @param enabled
	 */
	void setEnabled(boolean enabled);
}
