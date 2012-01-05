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
package org.xaloon.wicket.plugin.user.template;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.xaloon.wicket.plugin.email.template.EmailContentTemplatePage;


/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class ExternalRegistrationEmailTemplatePage extends EmailContentTemplatePage {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param username
	 * @param password
	 */
	public ExternalRegistrationEmailTemplatePage(String username, String password) {
		add(new Label("username", new Model<String>(username)));
		add(new Label("username2", new Model<String>(username)));
		add(new Label("password", new Model<String>(password)));
	}
}
