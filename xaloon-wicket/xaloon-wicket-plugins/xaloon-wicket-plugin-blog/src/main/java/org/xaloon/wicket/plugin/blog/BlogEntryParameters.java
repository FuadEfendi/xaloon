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

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * Generated link of blog entry: "http[s]://host/[username]/[year/month]/blog-entry-path"
 * 
 * @author vytautas r.
 */
public class BlogEntryParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;

	private int year;

	private int month;

	private int day;

	private String path;

	/**
	 * Construct.
	 */
	public BlogEntryParameters() {
	}

	/**
	 * @return username who created blog entry
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
	 * @return path of blog entry
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * @return year when blog entry was posted
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return month when blog entry was posted
	 */
	public int getMonth() {
		return month;
	}

	/**
	 * @param month
	 */
	public void setMonth(int month) {
		this.month = month;
	}

	/**
	 * @return day when blog was posted
	 */
	public int getDay() {
		return day;
	}

	/**
	 * @param day
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return true if all parameters are empty
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(username) && StringUtils.isEmpty(path) && year < 1900 && month < 1 && day < 1;
	}
}
