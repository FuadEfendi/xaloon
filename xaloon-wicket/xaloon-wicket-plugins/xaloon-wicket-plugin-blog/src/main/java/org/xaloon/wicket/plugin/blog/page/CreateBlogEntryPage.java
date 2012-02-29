package org.xaloon.wicket.plugin.blog.page;

import javax.annotation.security.RolesAllowed;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.component.mount.annotation.MountPageGroup;
import org.xaloon.wicket.plugin.blog.BlogPlugin;
import org.xaloon.wicket.plugin.blog.BlogSecurityAuthorities;
import org.xaloon.wicket.plugin.blog.panel.CreateBlogEntryPanel;

/**
 * @author vytautas r.
 */
@MountPageGroup(value = "/personal", plugin = BlogPlugin.class, order = 120)
@MountPage(value = "/new-blog-entry", order = 100)
@RolesAllowed({ BlogSecurityAuthorities.BLOG_CREATOR })
public class CreateBlogEntryPage extends LayoutWebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		return new CreateBlogEntryPanel(id, pageParameters);
	}

}
