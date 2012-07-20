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
package org.xaloon.wicket.plugin.blog.dao;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest;

/**
 * Interface contains {@link BlogEntry} data object related methods, like storing, retrieving, deleting blog entries, finding them by several
 * parameters.
 * 
 * @author vytautas r.
 */
public interface BlogDao extends Serializable {

	/**
	 * Returns blog entry by it's username and path
	 * 
	 * @param username
	 *            string representation of username
	 * @param blogEntryPath
	 *            string representation of blog entry path
	 * @return null if blog entry is not found, otherwise - blog entry instance
	 */
	BlogEntry findEntryByPath(String username, String blogEntryPath);

	/**
	 * Returns list of blog entries, limited by provided max count parameter
	 * 
	 * @param blogEntrySearchRequest
	 *            query parameters
	 * 
	 * @param first
	 *            from which blog entry to start. default start position - 0
	 * @param maxCount
	 *            maximum blog entries to return. -1 - returns all blog entries
	 * @return list of blog entries, ordered by sticky flag first and then by update date and then by create date
	 */
	List<BlogEntry> findAvailableBlogEntryList(BlogEntrySearchRequest blogEntrySearchRequest, long first, long maxCount);

	/**
	 * Creates new blog entry into blog repository
	 * 
	 * @param blogEntry
	 *            blog entry object to store
	 */
	BlogEntry save(BlogEntry blogEntry);

	/**
	 * Deletes existing blog entry from repository
	 * 
	 * @param blogEntry
	 *            blog entry object to delete
	 * 
	 * @return true - if delete was successful, false - otherwise
	 */
	boolean deleteBlogEntry(BlogEntry blogEntry);

	/**
	 * Returns total count of blog entries
	 * 
	 * @return count of blog entries in database
	 */
	Long getCount();

	/**
	 * Returns count of blog entries by query parameters
	 * 
	 * @param blogEntrySearchRequest
	 *            query parameters
	 * 
	 * @return count of blog entries by tag value
	 */
	Long getCount(BlogEntrySearchRequest blogEntrySearchRequest);

	/**
	 * @param first
	 *            from which blog entry to start. default start position - 0
	 * @param count
	 *            maximum blog entries to return. -1 - returns all blog entries
	 * @return list of blog entries
	 */
	List<BlogEntry> findAvailableBlogEntryList(long first, long count);

	/**
	 * Deletes all blogs created by this user
	 * 
	 * @param userToBeDeleted
	 */
	void deleteBlogsByUsername(User userToBeDeleted);
}
