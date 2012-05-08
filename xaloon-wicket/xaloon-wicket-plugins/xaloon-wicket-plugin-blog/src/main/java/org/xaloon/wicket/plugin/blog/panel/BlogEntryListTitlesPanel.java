package org.xaloon.wicket.plugin.blog.panel;

import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.wicket.component.resource.ImageLink;
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
		addOrReplace(new ListView<BlogEntry>("blog-items", blogFacade.findAvailableBlogEntryList(blogListOptions.getFirtBlogEntry(), blogListOptions.getMaxBlogEntriesCount())) {
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
				
				// Add image link
				BookmarkablePageLink<Void> link_image = new BookmarkablePageLink<Void>("link-image", blogEntyLink.getKey(), blogEntyLink.getValue());
				item.add(link_image);
				
				
				


				FileDescriptor thumbnail = getBlogEntryThumbnail(blogEntry);

				// Add image to link
				if (thumbnail != null && blogListOptions.getImageSize() != null) {
					ImageLink imageLink = new ImageLink("image", thumbnail.getPath());
					int imageWidth = getPluginBean().getBlogImageWidth();
					int imageHeight = getPluginBean().getBlogImageHeight();
					if (blogListOptions.getImageSize() != null) {
						imageWidth = blogListOptions.getImageSize().getWidth();
						imageHeight = blogListOptions.getImageSize().getHeight();
					}
					imageLink.setWidth(imageWidth);
					imageLink.setHeight(imageHeight);
					imageLink.setTitle(blogEntry.getTitle());

					link_image.add(imageLink);
				} else {
					link_image.setVisible(false);
				}
			}
		});
		
		
	}
	
	protected FileDescriptor getBlogEntryThumbnail(BlogEntry blogEntry) {
		if (blogEntry.getThumbnail() != null) {
			return blogEntry.getThumbnail();
		}
		List<Image> albumImages = albumFacade.getImagesByAlbum(blogEntry);
		if (!albumImages.isEmpty()) {
			return albumImages.get(0).getThumbnail();
		}
		return null;
	}
}
