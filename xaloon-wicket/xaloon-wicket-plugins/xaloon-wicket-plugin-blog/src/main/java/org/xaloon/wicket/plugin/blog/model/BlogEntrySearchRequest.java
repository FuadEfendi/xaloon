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
package org.xaloon.wicket.plugin.blog.model;

import java.io.Serializable;

/**
 * @author vytautas r.
 */
public class BlogEntrySearchRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private String blogEntryPath;

	private String tagPath;

	private String category;

	/**
	 * @return username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return blog entry path
	 */
	public String getBlogEntryPath() {
		return blogEntryPath;
	}

	/**
	 * @param blogEntryPath
	 */
	public void setBlogEntryPath(String blogEntryPath) {
		this.blogEntryPath = blogEntryPath;
	}


	/**
	 * Gets tagPath.
	 * 
	 * @return tagPath
	 */
	public String getTagPath() {
		return tagPath;
	}

	/**
	 * Sets tagPath.
	 * 
	 * @param tagPath
	 *            tagPath
	 */
	public void setTagPath(String tagPath) {
		this.tagPath = tagPath;
	}

	/**
	 * @return category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category;
	}
}
