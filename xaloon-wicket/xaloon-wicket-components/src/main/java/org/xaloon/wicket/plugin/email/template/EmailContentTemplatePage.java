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
package org.xaloon.wicket.plugin.email.template;

import org.apache.wicket.core.request.handler.IPageProvider;
import org.apache.wicket.core.request.handler.PageProvider;
import org.apache.wicket.core.request.handler.RenderPageRequestHandler;
import org.apache.wicket.markup.html.WebPage;
import org.xaloon.wicket.component.render.StringWebPageRenderer;

/**
 * @author vytautas r.
 */
public class EmailContentTemplatePage extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SUBJECT = "SUBJECT";

	/**
	 * @return subject of email
	 */
	public String getSubject() {
		return getString(SUBJECT);
	}

	/**
	 * @return rendered message body
	 */
	public String getSource() {
		IPageProvider provider = new PageProvider(this);
		StringWebPageRenderer pageRenderer = new StringWebPageRenderer(new RenderPageRequestHandler(provider));
		return pageRenderer.renderToString(getRequestCycle());
	}
}
