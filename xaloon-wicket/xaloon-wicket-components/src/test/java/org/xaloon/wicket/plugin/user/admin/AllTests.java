package org.xaloon.wicket.plugin.user.admin;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.xaloon.wicket.plugin.user.admin.renderer.RendererTests;

/**
 * @author vytautas r.
 */
@RunWith(Suite.class)
@SuiteClasses({ org.xaloon.wicket.plugin.user.admin.page.AllTests.class, org.xaloon.wicket.plugin.user.admin.panel.AllTests.class,
		RendererTests.class })
public class AllTests {

}
