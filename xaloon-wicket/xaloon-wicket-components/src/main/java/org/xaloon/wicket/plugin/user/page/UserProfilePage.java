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
package org.xaloon.wicket.plugin.user.page;

import javax.annotation.security.RolesAllowed;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.plugin.user.panel.UserProfilePanel;

/**
 * @author vytautas r.
 */
@RequireHttps
@MountPage(value = "/profile", order = 10)
@RolesAllowed({ SecurityAuthorities.AUTHENTICATED_USER })
public class UserProfilePage extends PersonalGroupPage {
	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		return new UserProfilePanel<User>(id, pageParameters);
	}

}
