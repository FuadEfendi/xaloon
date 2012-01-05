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
package org.xaloon.wicket.plugin.comment.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.util.TextUtil;
import org.xaloon.wicket.plugin.email.template.EmailContentTemplatePage;

/**
 * @author vytautas r.
 */
public class CommentEmailTemplatePage extends EmailContentTemplatePage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param originalUrl
	 * @param displayName
	 * @param messageText
	 */
	public CommentEmailTemplatePage(String originalUrl, String displayName, String messageText) {
		ExternalLink link = new ExternalLink("url", new Model<String>(originalUrl));
		add(link);
		link.add(new Label("url", new Model<String>(originalUrl)));
		add(new Label("display-name", new Model<String>(displayName)));
		add(new Label("text", new Model<String>(TextUtil.prepareStringForHTML(messageText))).setEscapeModelStrings(false));
	}
}
