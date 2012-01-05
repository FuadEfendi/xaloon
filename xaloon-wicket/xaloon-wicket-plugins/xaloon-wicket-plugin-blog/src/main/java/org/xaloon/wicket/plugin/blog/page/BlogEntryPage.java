package org.xaloon.wicket.plugin.blog.page;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.plugin.blog.BlogPageConstants;
import org.xaloon.wicket.plugin.blog.panel.BlogEntryPanel;

/**
 * This is default example of Blog entry page.
 * 
 * Usually you should use your own page, just injecting the panel. Otherwise you may override BlogEntryPage.html file and customize your own template.
 * 
 * Also this page is useful when using VirtualPageFactory.
 * 
 * @author vytautas r.
 * 
 */
@MountPage(value = "/blog/${" + BlogPageConstants.BLOG_USERNAME + "}/${" + BlogPageConstants.BLOG_YEAR + "}/${" + BlogPageConstants.BLOG_MONTH +
	"}/${" + BlogPageConstants.BLOG_DAY + "}/${" + BlogPageConstants.BLOG_PATH + "}", visible = false)
public class BlogEntryPage extends LayoutWebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		return new BlogEntryPanel(id, pageParameters);
	}

}
