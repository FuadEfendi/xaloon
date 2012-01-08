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
package org.xaloon.wicket.plugin.image.galleria;

import org.apache.wicket.request.resource.JavaScriptResourceReference;

import com.google.code.jqwicket.api.AbstractJQOptions;

/**
 * @author vytautas r.
 */
public class GalleriaOptions extends AbstractJQOptions<GalleriaOptions> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final JavaScriptResourceReference GALLERIA_JS_MIN = new JavaScriptResourceReference(GalleriaOptions.class, "galleria-latest.min.js");

	private static final JavaScriptResourceReference GALLERIA_JS_THEME = new JavaScriptResourceReference(GalleriaOptions.class,
		"themes/classic/galleria.classic.min.js");

	/**
	 * Construct.
	 */
	public GalleriaOptions() {
		setJsResourceReferences(GALLERIA_JS_MIN, GALLERIA_JS_THEME);

		/** there is not default width - that means it should be 100% by default */
		width(0).height(500);
	}

	/**
	 * Width of galleria
	 * 
	 * @param width
	 * @return GalleriaOptions instance
	 */
	public GalleriaOptions width(int width) {
		return super.put("width", width);
	}

	/**
	 * Height of galleria
	 * 
	 * @param height
	 * @return GalleriaOptions instance
	 */
	public GalleriaOptions height(int height) {
		return super.put("height", height);
	}
}
