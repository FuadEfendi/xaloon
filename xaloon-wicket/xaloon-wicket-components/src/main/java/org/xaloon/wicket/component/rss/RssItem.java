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
package org.xaloon.wicket.component.rss;

import java.io.Serializable;
import java.util.Date;

/**
 * @author vytautas r.
 */
public class RssItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String title;

	private String link;

	private String description;

	private String author;

	private String category;

	private String commentsLink;

	private String guid;

	private Date pubDate;

	private String source;

	/**
	 * @return The title of the item.
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return The URL of the item.
	 */
	public String getLink() {
		return link;
	}

	/**
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * @return The item synopsis.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Email address of the author of the item.
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return Includes the item in one or more categories.
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

	/**
	 * @return URL of a page for comments relating to the item.
	 */
	public String getCommentsLink() {
		return commentsLink;
	}

	/**
	 * @param commentsLink
	 */
	public void setCommentsLink(String commentsLink) {
		this.commentsLink = commentsLink;
	}

	/**
	 * @return A string that uniquely identifies the item.
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return Indicates when the item was published.
	 */
	public Date getPubDate() {
		return pubDate;
	}

	/**
	 * @param pubDate
	 */
	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	/**
	 * @return The RSS channel that the item came from.
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}
}
