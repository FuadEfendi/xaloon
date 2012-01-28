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

import static org.mockito.Mockito.when;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.xaloon.core.api.bookmark.Bookmarkable;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.api.security.SecurityRoles;
import org.xaloon.core.jpa.security.model.JpaRole;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.user.admin.AbstractUserAdminTestCase;

/**
 * @author vytautas r.
 */
public class RoleDetailPageTest extends AbstractUserAdminTestCase {

	/**
	 * @throws Exception
	 */
	@Test
	public void testPage() throws Exception {
		MockedApplication app = createMockedApplication();

		when(app.getSecurityFacade().hasAny(SecurityRoles.SYSTEM_ADMINISTRATOR)).thenReturn(true);

		WicketTester tester = new WicketTester(app);
		tester.startPage(RoleDetailPage.class);
		tester.assertRenderedPage(RolesPage.class);
	}


	/**
	 * @throws Exception
	 */
	@Test
	public void testPageWithParams() throws Exception {
		MockedApplication app = createMockedApplication();

		when(app.getSecurityFacade().hasAny(SecurityRoles.SYSTEM_ADMINISTRATOR)).thenReturn(true);

		SecurityRole role = new JpaRole();
		role.setName("name");
		when(roleGroupService.getRoleByPath("test")).thenReturn(role);
		WicketTester tester = new WicketTester(app);

		PageParameters params = new PageParameters();
		params.add(Bookmarkable.PARAM_PATH, "test");
		tester.startPage(RoleDetailPage.class, params);
		tester.assertRenderedPage(RoleDetailPage.class);
	}
}
