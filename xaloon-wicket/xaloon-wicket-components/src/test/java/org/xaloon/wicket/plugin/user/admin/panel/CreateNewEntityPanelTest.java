package org.xaloon.wicket.plugin.user.admin.panel;

import static org.mockito.Mockito.when;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.model.SecurityGroup;
import org.xaloon.core.jpa.security.model.JpaGroup;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.user.admin.AbstractUserAdminTestCase;

/**
 * @author vytautas r.
 */
public class CreateNewEntityPanelTest extends AbstractUserAdminTestCase {
	/**
	 * @throws Exception
	 */
	@Test
	public void testPanel() throws Exception {
		final StringBuilder invocationResult = new StringBuilder();
		WicketTester tester = createLocalTester(invocationResult);

		// Get and fill form
		FormTester form = tester.newFormTester("id:new-entity");
		form.setValue("name", "testValue");

		// Submit ajax form
		tester.executeAjaxEvent("id:new-entity:submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();
		Assert.assertEquals("testValue", invocationResult.toString());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelError() throws Exception {
		final StringBuilder invocationResult = new StringBuilder();
		WicketTester tester = createLocalTester(invocationResult);

		// Submit ajax form without filling required field
		tester.executeAjaxEvent("id:new-entity:submit", "onclick");

		// Validate result
		tester.assertErrorMessages("Field 'name' is required.");
		Assert.assertEquals("", invocationResult.toString());
	}

	private WicketTester createLocalTester(final StringBuilder invocationResult) {
		MockedApplication app = createMockedApplication();
		WicketTester tester = new WicketTester(app);

		// Set security to True by default
		when(app.getSecurityFacade().hasAny(SecurityAuthorities.SYSTEM_ADMINISTRATOR)).thenReturn(true);

		// Start panel
		SecurityGroup group = new JpaGroup();
		tester.startComponentInPage(new CreateNewEntityPanel<SecurityGroup>("id", new Model<SecurityGroup>(group)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onNewEntitySubmit(AjaxRequestTarget target, SecurityGroup entity) {
				invocationResult.append(entity.getName());
			}
		});
		return tester;
	}
}
