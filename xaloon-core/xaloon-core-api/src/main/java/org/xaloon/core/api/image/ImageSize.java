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
package org.xaloon.core.api.image;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * @author vytautas r.
 */
public class ImageSize implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int width;

	private int height;

	private String title;

	private String location;

	/**
	 * Construct.
	 * 
	 * @param width
	 * @param height
	 */
	public ImageSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Construct.
	 * 
	 * @param width
	 */
	public ImageSize(int width) {
		this.width = width;
	}

	/**
	 * Gets title.
	 * 
	 * @return title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets width.
	 * 
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets width.
	 * 
	 * @param width
	 *            width
	 * @return the instance
	 */
	public ImageSize setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * Gets height.
	 * 
	 * @return height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets height.
	 * 
	 * @param height
	 *            height
	 * @return the instance
	 */
	public ImageSize setHeight(int height) {
		this.height = height;
		return this;
	}

	public ImageSize location(String location) {
		this.location = location;
		return this;
	}

	/**
	 * Gets location.
	 * 
	 * @return location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Constructs visual description of image size
	 * 
	 * @return
	 */
	public String getFullTitle() {
		StringBuilder fullTitle = new StringBuilder();
		fullTitle.append('(').append(width).append(" px)");
		if (!StringUtils.isEmpty(title)) {
			fullTitle.append(' ').append(title);
		}
		return fullTitle.toString();
	}

	/**
	 * Sets title.
	 * 
	 * @param title
	 *            title
	 * @return
	 */
	public ImageSize title(String title) {
		this.title = title;
		return this;
	}
}
