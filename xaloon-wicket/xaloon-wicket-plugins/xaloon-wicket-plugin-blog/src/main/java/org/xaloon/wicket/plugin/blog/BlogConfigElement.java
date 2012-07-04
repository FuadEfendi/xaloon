package org.xaloon.wicket.plugin.blog;

import org.apache.wicket.Page;
import org.xaloon.core.api.plugin.PluginConfigEntry;
import org.xaloon.wicket.plugin.blog.page.BlogEntryNoDatePage;
import org.xaloon.wicket.plugin.blog.page.BlogEntryNoUserPage;
import org.xaloon.wicket.plugin.blog.page.BlogEntryPage;

public class BlogConfigElement implements PluginConfigEntry {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<? extends Page> defaultBlogEntryPage = BlogEntryPage.class;

	private Class<? extends Page> blogEntryNoUserPage = BlogEntryNoUserPage.class;
	
	private Class<? extends Page>  blogEntryNoDatePage = BlogEntryNoDatePage.class;
	
	/**
	 * @return the defaultBlogEntryPage
	 */
	public Class<? extends Page> getDefaultBlogEntryPage() {
		return defaultBlogEntryPage;
	}

	/**
	 * @param defaultBlogEntryPage the defaultBlogEntryPage to set
	 */
	public BlogConfigElement setDefaultBlogEntryPage(Class<? extends Page> defaultBlogEntryPage) {
		this.defaultBlogEntryPage = defaultBlogEntryPage;
		return this;
	}

	/**
	 * @return the blogEntryNoUserPage
	 */
	public Class<? extends Page> getBlogEntryNoUserPage() {
		return blogEntryNoUserPage;
	}

	/**
	 * @param blogEntryNoUserPage the blogEntryNoUserPage to set
	 */
	public void setBlogEntryNoUserPage(Class<? extends Page> blogEntryNoUserPage) {
		this.blogEntryNoUserPage = blogEntryNoUserPage;
	}

	/**
	 * @return the blogEntryNoDatePage
	 */
	public Class<? extends Page> getBlogEntryNoDatePage() {
		return blogEntryNoDatePage;
	}

	/**
	 * @param blogEntryNoDatePage the blogEntryNoDatePage to set
	 */
	public void setBlogEntryNoDatePage(Class<? extends Page> blogEntryNoDatePage) {
		this.blogEntryNoDatePage = blogEntryNoDatePage;
	}
	
	
}
