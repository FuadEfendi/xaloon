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

import java.io.Serializable;

import org.apache.wicket.model.Model;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.odlabs.wiquery.core.options.Options;

/**
 * @author vytautas r.
 */
public class GalleriaOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final JavaScriptResourceReference GALLERIA_JS_MIN = new JavaScriptResourceReference(
			GalleriaOptions.class, "galleria-1.2.7.min.js");

	public static final JavaScriptResourceReference GALLERIA_JS_THEME = new JavaScriptResourceReference(
			GalleriaOptions.class, "themes/classic/galleria.classic.min.js");

	public static final CssResourceReference GALLERIA_CSS_THEME = new CssResourceReference(
			GalleriaOptions.class, "themes/classic/galleria.classic.css");

	org.odlabs.wiquery.core.options.Options options;

	public GalleriaOptions() {
		options = new org.odlabs.wiquery.core.options.Options();
		extended();		
	}

	public GalleriaOptions extended() {
		options.put("extend", "function() {this.attachKeyboard({left: this.prev,right: this.next}); }");
		return this;
	}

	/**
	 * Width of galleria
	 * 
	 * @param width
	 * @return GalleriaOptions instance
	 */
	public GalleriaOptions width(int width) {
		options.putInteger("width", new Model<Integer>(width));
		return this;
	}

	/**
	 * Height of galleria
	 * 
	 * @param height
	 * @return GalleriaOptions instance
	 */
	public GalleriaOptions height(int height) {
		options.putInteger("height", new Model<Integer>(height));
		return this;
	}

	public Options getOptions() {
		return options;
	}
}
