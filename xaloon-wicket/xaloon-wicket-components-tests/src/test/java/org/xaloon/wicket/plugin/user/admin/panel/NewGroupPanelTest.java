package org.xaloon.wicket.plugin.user.admin.panel;

import static org.mockito.Mockito.when;

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Test;
import org.xaloon.core.api.security.SecurityRoles;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.user.admin.AbstractUserAdminTestCase;

public class NewGroupPanelTest extends AbstractUserAdminTestCase {
	@Test
	public void testPanel() throws Exception {
		MockedApplication app = createMockedApplication();
		WicketTester tester = new WicketTester(app);

		// Set security to True by default
		when(app.getSecurityFacade().hasAny(SecurityRoles.SYSTEM_ADMINISTRATOR)).thenReturn(true);

		tester.startComponentInPage(new NewGroupPanel("id"));
	}

}
