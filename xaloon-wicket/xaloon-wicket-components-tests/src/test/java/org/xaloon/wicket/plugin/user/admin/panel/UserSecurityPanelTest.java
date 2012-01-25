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

import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.WicketTester;
import org.xaloon.core.api.security.SecurityRoles;
import org.xaloon.wicket.component.test.MockedApplication;

/**
 * @author vytautas r.
 */
public class UserSecurityPanelTest extends TestCase {

	public void testPanelNotAuthorized() throws Exception {
		MockedApplication app = new MockedApplication();

		WicketTester tester = new WicketTester(app);
		try {
			tester.startComponentInPage(new UserSecurityPanel("id", new PageParameters()));
			fail();
		} catch (UnauthorizedInstantiationException e) {
			assertEquals("Not authorized to instantiate class org.xaloon.wicket.plugin.user.admin.panel.UserSecurityPanel", e.getMessage());
		}
	}

	public void testPanelAuthorized() throws Exception {
		MockedApplication app = new MockedApplication();

		when(app.getSecurityFacade().hasAny(SecurityRoles.SYSTEM_ADMINISTRATOR))
				.thenReturn(true);

		WicketTester tester = new WicketTester(app);
		tester.startComponentInPage(new UserSecurityPanel("id", new PageParameters()));
	}
}