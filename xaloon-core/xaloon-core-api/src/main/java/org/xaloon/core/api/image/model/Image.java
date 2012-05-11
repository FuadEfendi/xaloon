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
package org.xaloon.core.api.image.model;

import java.util.List;

import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;

/**
 * Simple interface for image definition.
 * 
 * @author vytautas r.
 */
public interface Image extends FileDescriptor {
	/**
	 * Gets thumbnail.
	 * 
	 * @return thumbnail
	 */
	FileDescriptor getThumbnail();

	/**
	 * Sets thumbnail.
	 * 
	 * @param thumbnail
	 *            thumbnail
	 */
	void setThumbnail(FileDescriptor thumbnail);

	/**
	 * Gets title.
	 * 
	 * @return title
	 */
	String getTitle();

	/**
	 * Sets title.
	 * 
	 * @param title
	 *            title
	 */
	void setTitle(String title);

	/**
	 * Gets description.
	 * 
	 * @return description
	 */
	String getDescription();

	/**
	 * Sets description.
	 * 
	 * @param description
	 *            description
	 */
	void setDescription(String description);

	/**
	 * Gets owner.
	 * 
	 * @return owner
	 */
	User getOwner();

	/**
	 * Sets owner.
	 * 
	 * @param owner
	 *            owner
	 */
	void setOwner(User owner);

	/**
	 * Gets tags.
	 * 
	 * @return tags
	 */
	List<? extends KeyValue<String, String>> getTags();

	/**
	 * Sets tags.
	 * 
	 * @param tags
	 *            tags
	 */
	void setTags(List<? extends KeyValue<String, String>> tags);


	/**
	 * Add additional resized file descriptors if necessary
	 * 
	 * @return
	 */
	List<? extends FileDescriptor> getAdditionalSizes();

	void setAdditionalSizes(List<? extends FileDescriptor> items);
}
