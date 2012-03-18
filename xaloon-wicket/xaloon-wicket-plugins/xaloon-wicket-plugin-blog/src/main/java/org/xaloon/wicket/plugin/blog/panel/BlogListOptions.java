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
package org.xaloon.wicket.plugin.blog.panel;

import java.io.Serializable;

import org.xaloon.core.api.image.ImageSize;

/**
 * @author vytautas r.
 */
public class BlogListOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxBlogEntriesCount = -1;

	private int titleLength = -1;

	private int descriptionLength = -1;

	private String descriptionStyle;
	
	private ImageSize imageSize;

	/**
	 * Gets maxBlogEntriesCount.
	 * 
	 * @return maxBlogEntriesCount
	 */
	public int getMaxBlogEntriesCount() {
		return maxBlogEntriesCount;
	}

	/**
	 * Sets maxBlogEntriesCount.
	 * 
	 * @param maxBlogEntriesCount
	 *            maxBlogEntriesCount
	 * @return current instance
	 */
	public BlogListOptions setMaxBlogEntriesCount(int maxBlogEntriesCount) {
		this.maxBlogEntriesCount = maxBlogEntriesCount;
		return this;
	}

	/**
	 * Gets titleLength.
	 * 
	 * @return titleLength
	 */
	public int getTitleLength() {
		return titleLength;
	}

	/**
	 * Sets titleLength.
	 * 
	 * @param titleLength
	 *            titleLength
	 * @return current instance
	 */
	public BlogListOptions setTitleLength(int titleLength) {
		this.titleLength = titleLength;
		return this;
	}

	/**
	 * Gets descriptionLength.
	 * 
	 * @return descriptionLength
	 */
	public int getDescriptionLength() {
		return descriptionLength;
	}

	/**
	 * Sets descriptionLength.
	 * 
	 * @param descriptionLength
	 *            descriptionLength
	 * @return current instance
	 */
	public BlogListOptions setDescriptionLength(int descriptionLength) {
		this.descriptionLength = descriptionLength;
		return this;
	}

	/**
	 * Gets descriptionStyle.
	 * 
	 * @return descriptionStyle
	 */
	public String getDescriptionStyle() {
		return descriptionStyle;
	}

	/**
	 * Sets descriptionStyle.
	 * 
	 * @param descriptionStyle
	 *            descriptionStyle
	 * @return current instance
	 */
	public BlogListOptions setDescriptionStyle(String descriptionStyle) {
		this.descriptionStyle = descriptionStyle;
		return this;
	}

	/**
	 * @return the imageSize
	 */
	public ImageSize getImageSize() {
		return imageSize;
	}

	/**
	 * @param imageSize the imageSize to set
	 * @return the instance
	 */
	public BlogListOptions setImageSize(ImageSize imageSize) {
		this.imageSize = imageSize;
		return this;
	}
}
