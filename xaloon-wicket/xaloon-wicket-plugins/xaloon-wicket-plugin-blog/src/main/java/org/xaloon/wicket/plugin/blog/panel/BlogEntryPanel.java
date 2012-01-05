package org.xaloon.wicket.plugin.blog.panel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.plugin.comment.Commentable;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.component.tag.TagCloudPanel;
import org.xaloon.wicket.plugin.addthis.panel.AddThisPanel;
import org.xaloon.wicket.plugin.blog.BlogEntryParameters;
import org.xaloon.wicket.plugin.blog.BlogPlugin;
import org.xaloon.wicket.plugin.blog.BlogPluginBean;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntryTag;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListByTagPage;
import org.xaloon.wicket.plugin.comment.panel.CommentContainerPanel;
import org.xaloon.wicket.plugin.image.galleria.panel.GalleriaImagesPanel;

/**
 * Required PagePamaters:
 * 
 * BlogEntryPanel.BLOG_AUTHOR - username of author BlogEntryPanel.BLOG_PATH - url of encoded title. this would be BlogEntry.getPath();
 * 
 * 
 */
public class BlogEntryPanel extends AbstractBlogPluginPanel {
	private static final String SEPARATOR = ",";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogEntryPanel.class);

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param params
	 */
	public BlogEntryPanel(String id, PageParameters params) {
		super(id, params);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize(BlogPlugin plugin, BlogPluginBean pluginBean) {
		if (getPageRequestParameters().isEmpty()) {
			LOGGER.warn("Page request parameters were not provided!");
			setVisible(false);
			setResponsePage(getBlogEntryListPageClass());
			return;
		}
		BlogEntryParameters parameters = parseBlogEntryParameters();
		if (parameters == null) {
			LOGGER.warn("Page request parameters were not correct type: " + getPageRequestParameters());
			setVisible(false);
			setResponsePage(getBlogEntryListPageClass());
			return;
		}
		final BlogEntry blogEntry = getBlogFacade().findEntryByPath(parameters.getUsername(), parameters.getPath());
		if (blogEntry == null) {
			LOGGER.warn("Blog entry was not found!. Username: " + parameters.getUsername() + "\tPath: " + parameters.getPath());
			setVisible(false);
			setResponsePage(getBlogEntryListPageClass());
			return;
		}

		setDefaultModel(new Model<BlogEntry>(blogEntry));
		DateFormat dateFormat = new SimpleDateFormat(getPluginBean().getDateFormat());

		// Add blog entry title
		add(new Label("title", new Model<String>(blogEntry.getTitle())));

		// Add blog entry create date
		add(new Label("createDate", new Model<String>(dateFormat.format(blogEntry.getCreateDate()))));

		// Add image
		String imageLinkPath = (blogEntry.getThumbnail() != null) ? blogEntry.getThumbnail().getPath() : null;

		final boolean imageVisible = !StringUtils.isEmpty(imageLinkPath) && blogEntry.getImages().isEmpty();
		ImageLink imageLink = new ImageLink("image", imageLinkPath) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isVisible() {
				return imageVisible;
			}
		};
		imageLink.setWidth(getPluginBean().getBlogImageWidth());
		imageLink.setHeight(getPluginBean().getBlogImageHeight());
		imageLink.setTitle(blogEntry.getTitle());
		add(imageLink);

		// Add category link
		BookmarkablePageLink<Void> categoryLink = createBlogCategoryLink(blogEntry);
		add(categoryLink);

		// Add author name
		BookmarkablePageLink<Void> authorLink = createBlogAuthorLink(blogEntry);
		add(authorLink);

		// Add content
		add(new Label("content", new Model<String>(blogEntry.getContent())).setEscapeModelStrings(false));

		// Add add-this panel
		add(new AddThisPanel("add-this-panel"));

		// Add comment plugin
		add(new CommentContainerPanel("commenting-plugin", new Model<Commentable>(blogEntry), getPageRequestParameters()).setCommentPageClass(getBlogFacade().getBlogEntrylink(
			blogEntry)
			.getKey()));

		// Add tag cloud panel
		TagCloudPanel<JpaBlogEntryTag> tagCloudPanel = new TagCloudPanel<JpaBlogEntryTag>("tag-cloud-panel", KEY_VALUE_BLOG_TAG) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<JpaBlogEntryTag> findRandomValues(String key, int maxCount) {
				return keyValueDao.findRandomValues(key, maxCount);
			}

		};
		add(tagCloudPanel);
		List<JpaBlogEntryTag> tags = (List<JpaBlogEntryTag>)blogEntry.getTags();
		tagCloudPanel.setHighlightTagCloudList(tags);
		tagCloudPanel.setPageClass(BlogEntryListByTagPage.class);

		GalleriaImagesPanel imagesPanel = new GalleriaImagesPanel("blog-entry-images", blogEntry);
		imagesPanel.setVisible(!blogEntry.getImages().isEmpty());
		add(imagesPanel);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (getParent() instanceof LayoutWebPage) {
			BlogEntry blogEntry = (BlogEntry)getDefaultModelObject();
			getParent().addOrReplace(new Label(LayoutWebPage.PAGE_TITLE, new Model<String>(blogEntry.getTitle())));
			getParent().addOrReplace(new Label(LayoutWebPage.PAGE_DESCRIPTION, new Model<String>(blogEntry.getDescriptionClean())));
			if (!blogEntry.getTags().isEmpty()) {
				String keysAsString = StringUtils.join(blogEntry.getTags(), SEPARATOR);
				getParent().addOrReplace(new Label(LayoutWebPage.PAGE_KEYWORDS, new Model<String>(keysAsString)));
			}
		}
	}
}
