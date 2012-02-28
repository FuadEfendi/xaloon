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

/**
 * @author vytautas r.
 */
public interface BlogPageConstants extends Serializable {
	/**
	 * The username of the blogger
	 */
	String BLOG_USERNAME = "blogger";

	/**
	 * The blog entry encoded path
	 */
	String BLOG_PATH = "blog_path";

	/**
	 * The blog entry year
	 */
	String BLOG_YEAR = "blog_year";

	/**
	 * The blog entry month
	 */
	String BLOG_MONTH = "blog_month";

	/**
	 * The blog entry day
	 */
	String BLOG_DAY = "blog_day";
	
	/** the category code as a parameter */
	String CATEGORY_CODE = "CATEGORY_CODE";
}
