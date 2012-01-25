package org.xaloon.wicket.plugin.user.admin;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;
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
		
		//Return at least one group
		when(roleGroupService.getGroupCount()).thenReturn(1);
		
		SecurityGroup group = mock(SecurityGroup.class);
		List<SecurityGroup> groups = new ArrayList<SecurityGroup>();
		groups.add(group);
		when(roleGroupService.getGroupList(0, 1)).thenReturn(groups);
		
		return app;
	}
}
