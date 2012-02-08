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
package org.xaloon.wicket.plugin.user.admin.panel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.user.UserSearchResult;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.user.admin.AbstractUserAdminTestCase;

/**
 * @author vytautas r.
 */
public class UsersPanelTest extends AbstractUserAdminTestCase {
	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNotAuthorized() throws Exception {
		WicketTester tester = new WicketTester(new MockedApplication());
		try {
			tester.startComponentInPage(new UsersPanel("id", new PageParameters()));
			fail();
		} catch (UnauthorizedInstantiationException e) {
			assertEquals("Not authorized to instantiate class org.xaloon.wicket.plugin.user.admin.panel.UsersPanel", e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelAuthorized() throws Exception {
		MockedApplication app = createMockedApplication();
		WicketTester tester = new WicketTester(app);
		when(app.getSecurityFacade().hasAny(SecurityAuthorities.SYSTEM_ADMINISTRATOR)).thenReturn(true);

		when(app.getUserFacade().count(null)).thenReturn(1);

		List<UserSearchResult> users = new ArrayList<UserSearchResult>();

		UserSearchResult user = mock(UserSearchResult.class);
		users.add(user);
		when(user.getUsername()).thenReturn("test");
		when(app.getUserFacade().findCombinedUsers(null, 0, 1)).thenReturn(users);

		tester.startComponentInPage(new UsersPanel("id", new PageParameters()));
		assertNotNull(tester.getTagByWicketId("container"));
		assertNotNull(tester.getTagByWicketId("security-users"));
		assertEquals(1, tester.getTagsByWicketId("details-link").size());
	}
}
