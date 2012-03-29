package org.xaloon.wicket.plugin.blog.panel;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;

public class BlogEntryListTitlesPanel extends AbstractBlogPluginPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected BlogListOptions blogListOptions;
	
	public BlogEntryListTitlesPanel(String id, BlogListOptions blogListOptions) {
		super(id);
		this.blogListOptions = blogListOptions;
	}
	
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		addOrReplace(new ListView<BlogEntry>("blog-items", blogFacade.findAvailableBlogEntryList(0, blogListOptions.getMaxBlogEntriesCount())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<BlogEntry> item) {
				BlogEntry blogEntry = item.getModelObject();
				KeyValue<Class<? extends Page>, PageParameters> blogEntyLink = getBlogFacade().getBlogEntrylink(blogEntry);
				
				// Add link to the blog entry
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link-title", blogEntyLink.getKey(), blogEntyLink.getValue());
				item.add(link);
				
				// Add blog entry title
				String title = blogEntry.getTitle();
				int titleLength = blogListOptions.getTitleLength();
				if (titleLength > 0 && title.length() > titleLength) {
					title = title.substring(0, titleLength) + "...";
				}
				link.add(new Label("title", new Model<String>(title)));
			}
		});
	}
}
