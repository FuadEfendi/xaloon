package org.xaloon.wicket.plugin.blog.panel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.util.Link;

/**
 * Default usage: new BlogEntryListPanel("blog-list"); You might want to use custom blog page, then you should override method
 * <p>
 * getBlogEntryDetailsPage
 * </p>
 * and add BlogEntryPanel into your page with url parameters:
 * 
 * add(new BlogEntryPanel("content", params));
 * 
 * where params contain these parameters: BlogEntryPanel.BLOG_AUTHOR - username of author BlogEntryPanel.BLOG_PATH - url of encoded title. this would
 * be BlogEntry.getPath();
 * 
 * @author vytautas r.
 * 
 */
public class BlogEntryListPanel extends AbstractBlogPluginPanel {

	private static final long serialVersionUID = 1L;

	private BlogListOptions blogListOptions;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public BlogEntryListPanel(String id, PageParameters pageParameters) {
		super(id, pageParameters);
		blogListOptions = new BlogListOptions();
	}


	/**
	 * Construct.
	 * 
	 * @param id
	 * @param blogListOptions
	 */
	public BlogEntryListPanel(String id, BlogListOptions blogListOptions) {
		super(id);
		this.blogListOptions = blogListOptions;
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		// Add paging navigation container with navigation toolbar
		final DecoratedPagingNavigatorContainer<BlogEntry> dataContainer = new DecoratedPagingNavigatorContainer<BlogEntry>("container",
			getCurrentRedirectLink());
		addOrReplace(dataContainer);
		// Create date formatter
		final DateFormat dateFormat = new SimpleDateFormat(getPluginBean().getDateFormat());

		// Add blog list data view
		final DataView<BlogEntry> blogEntryDataView = new DataView<BlogEntry>("blog-entry-list", getBlogEntryDataProvider()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<BlogEntry> item) {
				BlogEntry blogEntry = item.getModelObject();

				// Add blog entry link
				// PageParameters pageParameters = new PageParameters();
				// BlogUtils.setPageParameters(pageParameters, blogEntry);

				KeyValue<Class<? extends Page>, PageParameters> blogEntyLink = getBlogFacade().getBlogEntrylink(blogEntry);
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link-title", blogEntyLink.getKey(), blogEntyLink.getValue());
				item.add(link);

				// Add blog entry title
				String title = blogEntry.getTitle();
				int titleLength = blogListOptions.getTitleLength();
				if (titleLength > 0 && title.length() > titleLength) {
					title = title.substring(0, titleLength) + "...";
				}
				link.add(new Label("title", new Model<String>(title)));

				//Comment count
				Long commentCount = commentDao.count(blogEntry);
				item.add(new Label("comment-count", new Model<Long>(commentCount)));
				
				// Add description
				String description = blogEntry.getDescription();
				int descriptionLength = blogListOptions.getDescriptionLength();
				if (descriptionLength > 10 && description.length() > descriptionLength) {
					description = description.substring(0, descriptionLength) + "...";
				}
				final Label descriptionLabel = new Label("description", new Model<String>(description));
				descriptionLabel.setEscapeModelStrings(false);
				item.add(descriptionLabel);

				// Add image link
				BookmarkablePageLink<Void> link_image = new BookmarkablePageLink<Void>("link-image", blogEntyLink.getKey(), blogEntyLink.getValue());
				item.add(link_image);


				FileDescriptor thumbnail = getBlogEntryThumbnail(blogEntry);

				// Add image to link
				if (thumbnail != null) {
					ImageLink imageLink = new ImageLink("image", thumbnail.getPath());
					imageLink.setWidth(getPluginBean().getBlogImageWidth());
					imageLink.setHeight(getPluginBean().getBlogImageHeight());
					imageLink.setTitle(blogEntry.getTitle());

					link_image.add(imageLink);
				} else {
					link_image.setVisible(false);

					// Change style for description
					if (!StringUtils.isEmpty(blogListOptions.getDescriptionStyle())) {
						descriptionLabel.add(AttributeModifier.replace("style", blogListOptions.getDescriptionStyle()));
					}
				}
				// Add create date
				item.add(new Label("createDate", new Model<String>(dateFormat.format(blogEntry.getCreateDate()))));

				// Add category link
				BookmarkablePageLink<Void> categoryLink = createBlogCategoryLink(blogEntry);
				item.add(categoryLink);

				// Add author link
				BookmarkablePageLink<Void> authorLink = createBlogAuthorLink(blogEntry);
				item.add(authorLink);

				// Add read more link
				item.add(new BookmarkablePageLink<Void>("link-read-more", blogEntyLink.getKey(), blogEntyLink.getValue()));

				// Add edit link
				BookmarkablePageLink<Void> link_edit = new BookmarkablePageLink<Void>("link-edit", getCreateBlogEntryPageClass(),
					blogEntyLink.getValue());
				// Check security of link
				link_edit.setVisible(getSecurityFacade().isAdministrator() || getSecurityFacade().isOwnerOfObject(blogEntry.getOwner().getUsername()));
				item.add(link_edit);

				// Add delete link
				WebMarkupContainer link_delete = new AjaxLink<BlogEntry>("link-delete", item.getModel()) {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					protected IAjaxCallDecorator getAjaxCallDecorator() {
						return new AjaxCallDecorator() {
							private static final long serialVersionUID = 1L;

							@Override
							public CharSequence decorateScript(Component c, CharSequence script) {
								return "if(!confirm('" + BlogEntryListPanel.this.getString(DELETE_CONFIRMATION) + "')) return false;" + script;
							}
						};
					}

					@Override
					public void onClick(AjaxRequestTarget target) {
						BlogEntry blogEntry = getModelObject();
						getBlogFacade().deleteBlogEntryByPath(blogEntry.getOwner().getUsername(), blogEntry.getPath());
						setResponsePage(getBlogEntryListPageClass());
					}
				};
				boolean isDeleteLinkVisible = (getSecurityFacade().isAdministrator() || getSecurityFacade().isOwnerOfObject(
					blogEntry.getOwner().getUsername()));

				
				// Check security of link
				link_delete.setVisible(isDeleteLinkVisible);
				item.add(link_delete);
			}
		};
		dataContainer.addAbstractPageableView(blogEntryDataView, !(blogListOptions.getMaxBlogEntriesCount() > 0));
	}

	protected FileDescriptor getBlogEntryThumbnail(BlogEntry blogEntry) {
		if (blogEntry.getThumbnail() != null) {
			return blogEntry.getThumbnail();
		}
		if (!blogEntry.getImages().isEmpty()) {
			return blogEntry.getImages().get(0).getThumbnail();
		}
		return null;
	}

	protected IDataProvider<BlogEntry> getBlogEntryDataProvider() {
		return new JpaBlogEntryDataProvider();
	}

	protected Link getCurrentRedirectLink() {
		return new Link(getBlogEntryListPageClass(), getPageRequestParameters());
	}

	private class JpaBlogEntryDataProvider implements IDataProvider<BlogEntry> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
		}

		@Override
		public Iterator<? extends BlogEntry> iterator(int first, int count) {
			return getBlogFacade().findAvailableBlogEntryList(first, count).iterator();
		}

		@Override
		public int size() {
			int totalCount = getBlogFacade().getCount().intValue();
			int maxBlogEntries = blogListOptions.getMaxBlogEntriesCount();
			if (maxBlogEntries > 0 && maxBlogEntries < totalCount) {
				return maxBlogEntries;
			}
			return totalCount;
		}

		@Override
		public IModel<BlogEntry> model(BlogEntry object) {
			return new Model<BlogEntry>(object);
		}
	}
}
