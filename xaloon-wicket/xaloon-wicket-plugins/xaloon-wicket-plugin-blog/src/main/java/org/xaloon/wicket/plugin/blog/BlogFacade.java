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
package org.xaloon.wicket.plugin.blog;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.wicket.plugin.blog.dao.BlogDao;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest;


/**
 * @author vytautas r.
 */
public interface BlogFacade extends Serializable {
	/**
	 * Default location where to store blog images
	 */
	String BLOG_IMAGES = "blog-images";

	/**
	 * @param entry
	 * @param thumbnailToAdd
	 * @param deleteThumbnail
	 * @param pluginBean
	 * @param imagesToDelete
	 * @param imagesToAdd
	 * @throws IOException
	 */
	void storeBlogEntry(BlogEntry entry, ImageComposition thumbnailToAdd, boolean deleteThumbnail, BlogPluginBean pluginBean, List<ImageComposition> imagesToDelete,
		List<ImageComposition> imagesToAdd) throws IOException;

	/**
	 * @return list of blog categories
	 */
	List<ClassifierItem> getBlogCategories();

	/**
	 * @param entry
	 */
	BlogEntry deleteThumbnailFromBlogEntry(BlogEntry entry);

	/**
	 * @param username
	 * @param blogEntryPath
	 */
	void deleteBlogEntryByPath(String username, String blogEntryPath);

	/**
	 * @param first
	 * @param count
	 * @return list of available blog entries
	 */
	List<BlogEntry> findAvailableBlogEntryList(int first, int count);

	/**
	 * 
	 * @return count of total available blog entries
	 * @see BlogDao#getCount()
	 */
	Long getCount();

	/**
	 * @param blogEntrySearchRequest
	 * @param first
	 * @param count
	 * @return list of blog entries found by selected criteria
	 */
	List<BlogEntry> findAvailableBlogEntryList(BlogEntrySearchRequest blogEntrySearchRequest, int first, int count);

	/**
	 * @param blogEntrySearchRequest
	 * @return count of blog entries found by selected criteria
	 */
	Long getCount(BlogEntrySearchRequest blogEntrySearchRequest);

	/**
	 * @param username
	 * @param blogEntryPath
	 * @return blog entry found by unique path and username
	 */
	BlogEntry findEntryByPath(String username, String blogEntryPath);

	/**
	 * @param blogEntry
	 * @return collection of blog entry page class and parameters
	 */
	KeyValue<Class<? extends Page>, PageParameters> getBlogEntrylink(BlogEntry blogEntry);

	/**
	 * @param blogEntry
	 * @return absolute path of blog entry
	 */
	String resolveLink(BlogEntry blogEntry);

	/**
	 * Creates new blog entry entity, but does not persist it.
	 * 
	 * @return new instance of @see {@link BlogEntry} implementation class
	 */
	BlogEntry newBlogEntry();

	/**
	 * @return new Image instance
	 */
	Image newImage();

	ImageComposition newComposition();
}