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
package org.xaloon.wicket.plugin.google.panel;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.xaloon.wicket.plugin.google.PlusOneSize;

/**
 * @author vytautas r.
 */
public class GooglePlusOnePanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String PLUSONE_JAVA_SCRIPT = "<script type=\"text/javascript\">";
	private static final String PLUSONE_JAVA_SCRIPT_END = "(function() {"
		+ " var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;"
		+ "  po.src = 'https://apis.google.com/js/plusone.js';"
		+ "  var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);" + "})();" + "</script>";

	private PlusOneSize size;

	private String locale;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public GooglePlusOnePanel(String id) {
		super(id);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param size
	 * @param locale
	 *            for example, en-GB
	 */
	public GooglePlusOnePanel(String id, PlusOneSize size, String locale) {
		super(id);
		this.size = size;
		this.locale = locale;
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		Response response = RequestCycle.get().getResponse();
		response.write(getJavaScript());
	}

	/**
	 * Returns javascript of addthis component with provided username
	 * 
	 * @return string format of formatted javascript
	 */

	private CharSequence getJavaScript() {
		StringBuilder sb = new StringBuilder(PLUSONE_JAVA_SCRIPT);
		if (!StringUtils.isEmpty(locale)) {
			sb.append(getLocaleProperties());
		}
		sb.append(PLUSONE_JAVA_SCRIPT_END);
		return sb.toString();
	}

	protected String getLocaleProperties() {
		return "window.___gcfg = {lang: '" + locale + "'};";
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		add(new WebMarkupContainer("plusone") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (size != null) {
					tag.put("size", size.getSize());
				}
			}
		});
	}
}
