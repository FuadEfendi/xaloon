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

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractAjaxBehavior;
import org.odlabs.wiquery.core.javascript.JsQuery;

/**
 * @author vytautas r.
 */
public class GalleriaBehavior extends WiQueryAbstractAjaxBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public GalleriaBehavior() {
		this(new GalleriaOptions());
	}

	/**
	 * Construct.
	 * 
	 * @param options
	 */
	public GalleriaBehavior(GalleriaOptions opt) {
		options = opt.getOptions();

	}

	@Override
	public void renderHead(Component arg0, IHeaderResponse response) {
		super.renderHead(arg0, response);
		response.render(JavaScriptHeaderItem
				.forReference(GalleriaOptions.GALLERIA_JS_MIN));
		response.render(JavaScriptHeaderItem
				.forReference(GalleriaOptions.GALLERIA_JS_THEME));
		response.render(CssHeaderItem
				.forReference(GalleriaOptions.GALLERIA_CSS_THEME));
		response.render(OnDomReadyHeaderItem.forScript(new JsQuery(
				getComponent()).$()
				.chain("galleria", options.getJavaScriptOptions()).render()));
	}
}
