package org.xaloon.wicket.plugin.user.admin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.behavior.AbstractAjaxBehavior;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.util.tester.WicketTester;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.component.test.MockedApplication;
import org.xaloon.wicket.plugin.system.SystemPlugin;
import org.xaloon.wicket.plugin.system.SystemPluginBean;

public abstract class AbstractUserAdminTestCase {
	protected RoleGroupService roleGroupService = mock(RoleGroupService.class);
	
	protected MockedApplication createMockedApplication() {
		MockedApplication app = new MockedApplication();
		app.getMockedServices().put(RoleGroupService.class.getName(), roleGroupService);
		
		SystemPluginBean systemPluginBean = new SystemPluginBean();
		
		when(app.getPluginRegistry().getPluginBean(SystemPlugin.class)).thenReturn(systemPluginBean);
		when(app.getPluginRegistry().isEnabled(SystemPlugin.class)).thenReturn(true);
		
		UserDetails details = mock(UserDetails.class);
		when(app.getUserFacade().loadUserDetails("demo")).thenReturn(details);
		
		User user = mock(User.class);
		when(app.getUserFacade().getUserByUsername("demo")).thenReturn(user);
		
		return app;
	}
	
	protected void closeModalWindow(ModalWindow modal, WicketTester tester) {
		for (Behavior behavior : modal.getBehaviors()) {
			if (behavior instanceof AbstractDefaultAjaxBehavior) {
					String name = behavior.getClass().getSimpleName();
					if (name.startsWith("WindowClosedBehavior")) {
						tester.executeBehavior((AbstractAjaxBehavior)behavior);
					}
			}
		}
	}
}
