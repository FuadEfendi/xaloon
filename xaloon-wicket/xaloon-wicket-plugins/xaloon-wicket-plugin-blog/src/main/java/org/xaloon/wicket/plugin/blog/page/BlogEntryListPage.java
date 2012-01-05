package org.xaloon.wicket.plugin.blog.page;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.component.mount.annotation.MountPageGroup;
import org.xaloon.wicket.plugin.blog.BlogPlugin;
import org.xaloon.wicket.plugin.blog.panel.BlogEntryListPanel;

/**
 * Default usage of blog entry list page. Useful when combining with VirtualPageFactory
 * 
 * @author vytautas r.
 * 
 */
@MountPageGroup(value = "/blog", plugin = BlogPlugin.class, order = 100)
@MountPage(value = "/list", visible = true, order = 100)
public class BlogEntryListPage extends LayoutWebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		return new BlogEntryListPanel(id, pageParameters);
	}

}
