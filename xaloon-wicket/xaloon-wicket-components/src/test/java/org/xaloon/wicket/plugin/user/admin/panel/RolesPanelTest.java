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
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.core.jpa.security.model.JpaRole;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.user.admin.AbstractUserAdminTestCase;

/**
 * @author vytautas r.
 */
public class RolesPanelTest extends AbstractUserAdminTestCase {

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNotAuthorized() throws Exception {
		WicketTester tester = new WicketTester(new MockedApplication());
		try {
			tester.startComponentInPage(new RolesPanel("id", new PageParameters()));
			fail();
		} catch (UnauthorizedInstantiationException e) {
			assertEquals("Not authorized to instantiate class org.xaloon.wicket.plugin.user.admin.panel.RolesPanel", e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelAuthorized() throws Exception {
		MockedApplication app = createMockedApplication();
		WicketTester tester = new WicketTester(app);

		// Set security to True by default
		when(app.getSecurityFacade().hasAny(SecurityAuthorities.SYSTEM_ADMINISTRATOR)).thenReturn(true);

		// Return at least one role
		when(roleService.getCount()).thenReturn(1L);

		SecurityRole role = mock(SecurityRole.class);
		when(role.getPath()).thenReturn("role-name");

		List<SecurityRole> roles = new ArrayList<SecurityRole>();
		roles.add(role);
		when(roleService.getAuthorities(0, 1)).thenReturn(roles);

		SecurityRole securityRole = new JpaRole();
		when(roleService.newAuthority()).thenReturn(securityRole);

		tester.startComponentInPage(new RolesPanel("id", new PageParameters()));
		assertNotNull(tester.getTagByWicketId("container"));
		assertNotNull(tester.getTagByWicketId("security-roles"));
		assertEquals(1, tester.getTagsByWicketId("name").size());

		// Test creating new role
		tester.clickLink("id:add-new-role");
		tester.assertNoErrorMessage();

		// Get the modal window and submit the form
		ModalWindow modal = (ModalWindow)tester.getComponentFromLastRenderedPage("id:modal-new-role");
		tester.isVisible(modal.getPageRelativePath());

		// Close and re-open modal window
		closeModalWindow(modal, tester);
		tester.clickLink("id:add-new-role");
		tester.assertNoErrorMessage();
		modal = (ModalWindow)tester.getComponentFromLastRenderedPage("id:modal-new-role");

		// Take the form
		String modalPath = modal.getPageRelativePath() + ":" + modal.getContentId();
		String formPath = modalPath + ":new-entity";
		FormTester form = tester.newFormTester(formPath);
		form.setValue("name", "testValue");

		// Submit ajax form
		tester.executeAjaxEvent(formPath + ":submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();

		Assert.assertEquals("testValue", securityRole.getName());
	}
}
