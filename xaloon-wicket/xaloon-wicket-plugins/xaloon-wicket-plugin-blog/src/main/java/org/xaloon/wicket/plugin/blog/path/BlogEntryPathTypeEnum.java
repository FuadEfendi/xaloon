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
package org.xaloon.wicket.plugin.blog.path;

import java.io.Serializable;

/**
 * @author vytautas r.
 */
public enum BlogEntryPathTypeEnum implements Serializable {

	/**
	 * Author username, year, month, date and the entry path will be included in the blog link
	 */
	USERNAME_DATE_TITLE,

	/**
	 * Author username and the entry path will be included in blog link
	 */
	USERNAME_TITLE,

	/**
	 * Only entry path will be included in blog link
	 */
	TITLE,

	/**
	 * The custom blog entry path
	 */
	CUSTOM
}
