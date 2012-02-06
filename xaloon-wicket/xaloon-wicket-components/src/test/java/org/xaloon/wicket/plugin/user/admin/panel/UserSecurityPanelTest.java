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
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.authorization.UnauthorizedInstantiationException;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.tester.BaseWicketTester;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.user.admin.AbstractUserAdminTestCase;
import org.xaloon.wicket.plugin.user.admin.page.UsersPage;

/**
 * @author vytautas r.
 */
public class UserSecurityPanelTest extends AbstractUserAdminTestCase {

	final List<Authority> availableAuthorities = newSecurityAuthorityListWithItems(3);

	final List<SecurityRole> availableRoles = newSecurityRoleListWithItems(3);

	final List<SecurityGroup> availableGroups = newSecurityGroupListWithItems(3);

	WicketTester tester;

	MockedApplication app;

	/**
	 * 
	 */
	@Before
	public void init() {
		app = createMockedApplication();
		tester = new WicketTester(app);

		Mockito.when(app.getSecurityFacade().hasAny(SecurityAuthorities.SYSTEM_ADMINISTRATOR)).thenReturn(true);
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNoParameters() throws Exception {
		try {
			tester.startComponentInPage(new UserSecurityPanel("id", new PageParameters()));
			fail();
		} catch (RestartResponseException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelEmptyParameter() throws Exception {
		try {
			PageParameters pageParam = new PageParameters();
			pageParam.add(UsersPage.PARAM_USER_ID, "");
			tester.startComponentInPage(new UserSecurityPanel("id", pageParam));
			fail();
		} catch (RestartResponseException e) {
			Assert.assertTrue(true);
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNotAuthorized() throws Exception {
		WicketTester tester = new WicketTester(new MockedApplication());
		try {
			tester.startComponentInPage(new UserSecurityPanel("id", new PageParameters()));
			fail();
		} catch (UnauthorizedInstantiationException e) {
			assertEquals("Not authorized to instantiate class org.xaloon.wicket.plugin.user.admin.panel.UserSecurityPanel", e.getMessage());
		}
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNoUserDetailsFound() throws Exception {
		when(app.getUserFacade().loadUserDetails("test")).thenReturn(null);

		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "test");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(UsersPage.class);
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNoUserFound() throws Exception {
		when(app.getUserFacade().loadUserDetails("test")).thenReturn(details);
		when(app.getUserFacade().getUserByUsername("test")).thenReturn(null);

		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "test");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(UsersPage.class);
	}


	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelAuthoritiesAssign() throws Exception {
		// Add mocked invocations

		// return available authorities
		Mockito.when(roleGroupService.getAuthorityList(0, -1)).thenReturn(availableAuthorities);

		// return given authorities to user
		final List<Authority> givenAuthorities = new ArrayList<Authority>();
		givenAuthorities.add(newAuthority(777L, "fake-authority"));
		Mockito.when(details.getAuthorities()).thenReturn(givenAuthorities);

		Mockito.when(roleGroupService.assignAuthorities((UserDetails)Matchers.anyObject(), Matchers.anyListOf(Authority.class))).thenAnswer(
			new Answer<SecurityGroup>() {

				@SuppressWarnings("unchecked")
				@Override
				public SecurityGroup answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					List<Authority> selections = (List<Authority>)args[1];
					Assert.assertEquals(1, selections.size());
					Assert.assertEquals(availableAuthorities.get(2), selections.get(0));
					return null;
				}
			});

		// Start the test

		// Start the panel before each test
		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		Assert.assertNotNull(tester.getTagByWicketId("authority-admin"));
		Assert.assertNotNull(tester.getTagByWicketId("link-assign-entities"));

		// Open the modal window
		tester.clickLink("id:authority-admin:choice-management:link-assign-entities");
		tester.assertNoErrorMessage();

		ModalWindow modalWindow = (ModalWindow)tester.getComponentFromLastRenderedPage("id:authority-admin:choice-management:modal-assign-entities");
		String modalPath = modalWindow.getPageRelativePath() + ":" + modalWindow.getContentId();
		Assert.assertTrue(modalWindow.isVisible());

		// Submit the form
		String formPath = modalPath + ":form";
		FormTester form = tester.newFormTester(formPath);
		form.selectMultiple("choices", new int[] { 2 });

		// Submit ajax form
		tester.executeAjaxEvent(formPath + ":submit", "onclick");
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelAuthoritiesRevoke() throws Exception {
		// Add mocked invocations

		// return available authorities
		Mockito.when(roleGroupService.getAuthorityList(0, -1)).thenReturn(availableAuthorities);

		// return given authorities to user
		final List<Authority> givenAuthorities = newSecurityAuthorityListWithItems(1);
		Mockito.when(details.getAuthorities()).thenReturn(givenAuthorities);

		Mockito.when(roleGroupService.revokeAuthority((UserDetails)Matchers.anyObject(), (Authority)Matchers.anyObject())).thenAnswer(
			new Answer<SecurityGroup>() {

				@Override
				public SecurityGroup answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					Authority authorityToBeRevoked = (Authority)args[1];
					Assert.assertEquals(givenAuthorities.get(0), authorityToBeRevoked);
					return null;
				}
			});

		// Start the panel before each test
		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		Assert.assertNotNull(tester.getTagByWicketId("authority-admin"));
		Assert.assertNotNull(tester.getTagByWicketId("revoke"));

		// Click revoke link for the first assigned authority
		tester.clickLink("id:authority-admin:current-view:0:revoke");
		tester.assertNoErrorMessage();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelAuthoritiesCloseWindow() throws Exception {
		// return available authorities
		Mockito.when(roleGroupService.getAuthorityList(0, -1)).thenReturn(availableAuthorities);

		// return given authorities to user
		final List<Authority> givenAuthorities = newSecurityAuthorityListWithItems(1);
		Mockito.when(details.getAuthorities()).thenReturn(givenAuthorities);

		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		Assert.assertNotNull(tester.getTagByWicketId("authority-admin"));

		Assert.assertNotNull(tester.getTagByWicketId("link-assign-entities"));
		tester.clickLink("id:authority-admin:choice-management:link-assign-entities");
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		ModalWindow modalWindow = (ModalWindow)tester.getComponentFromLastRenderedPage("id:authority-admin:choice-management:modal-assign-entities");
		Assert.assertTrue(modalWindow.isVisible());
		closeModalWindow(modalWindow, tester);
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelRolesAssign() throws Exception {
		// Add mocked invocations

		// return available roles
		Mockito.when(roleGroupService.getRoleList(0, -1)).thenReturn(availableRoles);

		// return given roles to user
		final List<SecurityRole> givenRoles = new ArrayList<SecurityRole>();
		givenRoles.add(newRole(777L, "fake-role"));
		Mockito.when(details.getRoles()).thenReturn(givenRoles);

		Mockito.when(roleGroupService.assignRoles((UserDetails)Matchers.anyObject(), Matchers.anyListOf(SecurityRole.class))).thenAnswer(
			new Answer<SecurityGroup>() {

				@SuppressWarnings("unchecked")
				@Override
				public SecurityGroup answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					List<SecurityRole> selections = (List<SecurityRole>)args[1];
					Assert.assertEquals(1, selections.size());
					Assert.assertEquals(availableRoles.get(2), selections.get(0));
					return null;
				}
			});

		// Start the test

		// Start the panel before each test
		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		Assert.assertNotNull(tester.getTagByWicketId("role-admin"));
		Assert.assertNotNull(tester.getTagByWicketId("link-assign-entities"));

		// Open the modal window
		tester.clickLink("id:role-admin:choice-management:link-assign-entities");
		tester.assertNoErrorMessage();

		ModalWindow modalWindow = (ModalWindow)tester.getComponentFromLastRenderedPage("id:role-admin:choice-management:modal-assign-entities");
		String modalPath = modalWindow.getPageRelativePath() + ":" + modalWindow.getContentId();
		Assert.assertTrue(modalWindow.isVisible());

		// Submit the form
		String formPath = modalPath + ":form";
		FormTester form = tester.newFormTester(formPath);
		form.selectMultiple("choices", new int[] { 2 });

		// Submit ajax form
		tester.executeAjaxEvent(formPath + ":submit", "onclick");
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelRolesRevoke() throws Exception {
		// Add mocked invocations

		// return available roles
		Mockito.when(roleGroupService.getRoleList(0, -1)).thenReturn(availableRoles);

		// return given roles to user
		final List<SecurityRole> givenRoles = new ArrayList<SecurityRole>();
		givenRoles.add(newRole(777L, "fake-role"));
		Mockito.when(details.getRoles()).thenReturn(givenRoles);

		Mockito.when(roleGroupService.revokeRole((UserDetails)Matchers.anyObject(), (SecurityRole)Matchers.anyObject())).thenAnswer(
			new Answer<SecurityGroup>() {

				@Override
				public SecurityGroup answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					SecurityRole authorityToBeRevoked = (SecurityRole)args[1];
					Assert.assertEquals(givenRoles.get(0), authorityToBeRevoked);
					return null;
				}
			});

		// Start the panel before each test
		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		Assert.assertNotNull(tester.getTagByWicketId("role-admin"));
		Assert.assertNotNull(tester.getTagByWicketId("revoke"));

		// Click revoke link for the first assigned authority
		tester.clickLink("id:role-admin:current-view:0:revoke");
		tester.assertNoErrorMessage();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelRolesCloseWindow() throws Exception {
		// return available roles
		Mockito.when(roleGroupService.getRoleList(0, -1)).thenReturn(availableRoles);

		// return given roles to user
		final List<SecurityRole> givenRoles = new ArrayList<SecurityRole>();
		givenRoles.add(newRole(777L, "fake-role"));
		Mockito.when(details.getRoles()).thenReturn(givenRoles);

		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		Assert.assertNotNull(tester.getTagByWicketId("role-admin"));

		Assert.assertNotNull(tester.getTagByWicketId("link-assign-entities"));
		tester.clickLink("id:role-admin:choice-management:link-assign-entities");
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		ModalWindow modalWindow = (ModalWindow)tester.getComponentFromLastRenderedPage("id:role-admin:choice-management:modal-assign-entities");
		Assert.assertTrue(modalWindow.isVisible());
		closeModalWindow(modalWindow, tester);
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelGroupsAssign() throws Exception {
		// Add mocked invocations

		// return available groups
		Mockito.when(roleGroupService.getGroupList(0, -1)).thenReturn(availableGroups);

		// return given groups to user
		final List<SecurityGroup> givenGroups = new ArrayList<SecurityGroup>();
		givenGroups.add(newGroup(777L, "fake-group"));
		Mockito.when(details.getGroups()).thenReturn(givenGroups);

		Mockito.when(roleGroupService.assignGroups((UserDetails)Matchers.anyObject(), Matchers.anyListOf(SecurityGroup.class))).thenAnswer(
			new Answer<SecurityGroup>() {

				@SuppressWarnings("unchecked")
				@Override
				public SecurityGroup answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					List<SecurityGroup> selections = (List<SecurityGroup>)args[1];
					Assert.assertEquals(1, selections.size());
					Assert.assertEquals(availableGroups.get(2), selections.get(0));
					return null;
				}
			});

		// Start the test

		// Start the panel before each test
		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		Assert.assertNotNull(tester.getTagByWicketId("group-admin"));
		Assert.assertNotNull(tester.getTagByWicketId("link-assign-entities"));

		// Open the modal window
		tester.clickLink("id:group-admin:choice-management:link-assign-entities");
		tester.assertNoErrorMessage();

		ModalWindow modalWindow = (ModalWindow)tester.getComponentFromLastRenderedPage("id:group-admin:choice-management:modal-assign-entities");
		String modalPath = modalWindow.getPageRelativePath() + ":" + modalWindow.getContentId();
		Assert.assertTrue(modalWindow.isVisible());

		// Submit the form
		String formPath = modalPath + ":form";
		FormTester form = tester.newFormTester(formPath);
		form.selectMultiple("choices", new int[] { 2 });

		// Submit ajax form
		tester.executeAjaxEvent(formPath + ":submit", "onclick");
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelGroupsRevoke() throws Exception {
		// Add mocked invocations

		// return available groups
		Mockito.when(roleGroupService.getGroupList(0, -1)).thenReturn(availableGroups);

		// return given groups to user
		final List<SecurityGroup> givenGroups = new ArrayList<SecurityGroup>();
		givenGroups.add(newGroup(777L, "fake-group"));
		Mockito.when(details.getGroups()).thenReturn(givenGroups);

		Mockito.when(roleGroupService.revokeGroup((UserDetails)Matchers.anyObject(), (SecurityGroup)Matchers.anyObject())).thenAnswer(
			new Answer<SecurityGroup>() {

				@Override
				public SecurityGroup answer(InvocationOnMock invocation) throws Throwable {
					Object[] args = invocation.getArguments();
					SecurityGroup authorityToBeRevoked = (SecurityGroup)args[1];
					Assert.assertEquals(givenGroups.get(0), authorityToBeRevoked);
					return null;
				}
			});

		// Start the panel before each test
		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		Assert.assertNotNull(tester.getTagByWicketId("group-admin"));
		Assert.assertNotNull(tester.getTagByWicketId("revoke"));

		// Click revoke link for the first assigned authority
		tester.clickLink("id:group-admin:current-view:0:revoke");
		tester.assertNoErrorMessage();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelGroupsCloseWindow() throws Exception {
		// return available groups
		Mockito.when(roleGroupService.getGroupList(0, -1)).thenReturn(availableGroups);

		// return given groups to user
		final List<SecurityGroup> givenGroups = new ArrayList<SecurityGroup>();
		givenGroups.add(newGroup(777L, "fake-group"));
		Mockito.when(details.getGroups()).thenReturn(givenGroups);

		PageParameters params = new PageParameters();
		params.add(UsersPage.PARAM_USER_ID, "demo");
		tester.startComponentInPage(new UserSecurityPanel("id", params));
		Assert.assertNotNull(tester.getTagByWicketId("group-admin"));

		Assert.assertNotNull(tester.getTagByWicketId("link-assign-entities"));
		tester.clickLink("id:group-admin:choice-management:link-assign-entities");
		tester.assertNoErrorMessage();
		tester.assertRenderedPage(BaseWicketTester.StartComponentInPage.class);

		ModalWindow modalWindow = (ModalWindow)tester.getComponentFromLastRenderedPage("id:group-admin:choice-management:modal-assign-entities");
		Assert.assertTrue(modalWindow.isVisible());
		closeModalWindow(modalWindow, tester);
	}
}
