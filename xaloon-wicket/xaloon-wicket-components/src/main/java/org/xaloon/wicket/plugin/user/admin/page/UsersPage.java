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
package org.xaloon.wicket.plugin.user.admin.page;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.plugin.user.admin.panel.UsersPanel;

/**
 * @author vytautas r.
 */
@MountPage(value = "/users", order = 5)
public class UsersPage extends SecurityGroupPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Parameter to pass user id to details page
	 */
	public static final String PARAM_USER_ID = "userId";

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		return new UsersPanel(id, pageParameters);
	}
}
