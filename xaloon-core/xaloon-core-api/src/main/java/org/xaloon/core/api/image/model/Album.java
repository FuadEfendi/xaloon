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

import org.xaloon.core.api.persistence.Persistable;
import org.xaloon.core.api.plugin.comment.Commentable;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.user.model.User;


/**
 * @author vytautas r.
 */
public interface Album extends Persistable, Commentable {

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
	 * Gets parent.
	 * 
	 * @return parent
	 */
	Album getParent();

	/**
	 * Sets parent.
	 * 
	 * @param parent
	 *            parent
	 */
	void setParent(Album parent);

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
	 * Gets images.
	 * 
	 * @return images
	 */
	<T extends ImageComposition> List<T> getImages();

	/**
	 * Sets images.
	 * 
	 * @param images
	 *            images
	 */
	void setImages(List<? extends ImageComposition> images);

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

}