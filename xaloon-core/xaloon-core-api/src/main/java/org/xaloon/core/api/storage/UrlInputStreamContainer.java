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
package org.xaloon.core.api.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;

/**
 * @author vytautas r.
 */
public class UrlInputStreamContainer extends AbstractInputStreamContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String WWW_YOUTUBE_COM = "www.youtube.com";

	private String url;

	/**
	 * Construct.
	 * 
	 * @param url
	 */
	public UrlInputStreamContainer(String url) {
		this(url, new InputStreamContainerOptions());
	}

	/**
	 * Construct.
	 * 
	 * @param url
	 * @param options
	 */
	public UrlInputStreamContainer(String url, InputStreamContainerOptions options) {
		super(options);
		this.url = validateAndFix(url);
	}

	private String validateAndFix(String url2) {
		if (url2.contains(WWW_YOUTUBE_COM)) {
			String youtubeId = url2.substring(url2.indexOf("=") + 1);
			return "http://img.youtube.com/vi/" + youtubeId + "/1.jpg";
		}
		return url2;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		try {
			return new URL(url).openStream();
		} catch (MalformedURLException e) {
			throw new RuntimeException("Could not create url", e);
		}
	}

	/**
	 * Gets url.
	 * 
	 * @return url
	 */
	public String getUrl() {
		return url;
	}

	@Override
	public void close() {
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isEmpty(url);
	}
}
