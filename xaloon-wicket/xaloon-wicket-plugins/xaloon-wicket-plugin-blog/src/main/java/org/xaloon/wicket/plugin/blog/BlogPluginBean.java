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

import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * Default blog configuration properties
 * 
 * @author vytautas r.
 */
public class BlogPluginBean extends AbstractPluginBean {

	private static final long serialVersionUID = 1L;
	
	/** blog header image width to use **/
	private int blogImageWidth = 158;

	/** blog header image height to use **/
	private int blogImageHeight = 82;

	/** how long description will be in sentences **/
	private int blogDescriptionLengthInSentences = 5;

	/**
	 * Gets count of sentences to be displayed as a description of blog entry
	 * 
	 * @return blogDescriptionLengthInSentences
	 */
	public int getBlogDescriptionLengthInSentences() {
		return blogDescriptionLengthInSentences;
	}

	/**
	 * Sets blogDescriptionLengthInSentences.
	 * 
	 * @param blogDescriptionLengthInSentences
	 *            blogDescriptionLengthInSentences
	 */
	public void setBlogDescriptionLengthInSentences(int blogDescriptionLengthInSentences) {
		this.blogDescriptionLengthInSentences = blogDescriptionLengthInSentences;
	}

	/**
	 * 
	 * @return default image width if there is no provided any
	 */
	public int getBlogImageWidth() {
		return blogImageWidth;
	}

	/**
	 * 
	 * @param blogImageWidth
	 */
	public void setBlogImageWidth(int blogImageWidth) {
		this.blogImageWidth = blogImageWidth;
	}

	/**
	 * 
	 * @return default image height if there is no provided any
	 */
	public int getBlogImageHeight() {
		return blogImageHeight;
	}

	/**
	 * 
	 * @param blogImageHeight
	 */
	public void setBlogImageHeight(int blogImageHeight) {
		this.blogImageHeight = blogImageHeight;
	}
}
