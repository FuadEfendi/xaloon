package org.xaloon.wicket.plugin.blog.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.counting.CounterFacade;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.plugin.comment.Commentable;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.html.MetaTagWebContainer;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.component.tag.TagCloudPanel;
import org.xaloon.wicket.plugin.blog.BlogEntryParameters;
import org.xaloon.wicket.plugin.blog.BlogPlugin;
import org.xaloon.wicket.plugin.blog.BlogPluginBean;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntryTag;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListByTagPage;
import org.xaloon.wicket.plugin.comment.panel.CommentContainerPanel;
import org.xaloon.wicket.plugin.image.galleria.panel.GalleriaImagesPanel;
import org.xaloon.wicket.util.UrlUtils;

/**
 * Required PagePamaters:
 * 
 * BlogEntryPanel.BLOG_AUTHOR - username of author BlogEntryPanel.BLOG_PATH -
 * url of encoded title. this would be BlogEntry.getPath();
 * 
 * 
 */
public class BlogEntryPanel extends AbstractBlogPluginPanel {
	private static final String SEPARATOR = ",";

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(BlogEntryPanel.class);

	@Inject
	@Named("counterFacade")
	private CounterFacade counterFacade;

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
		String url = UrlUtils.generateFullvalue(getBlogEntryListPageClass());
		
		if (getPageRequestParameters().isEmpty()) {
			LOGGER.warn("Page request parameters were not provided!");
			throw new RedirectToUrlException(url);
		}
		BlogEntryParameters parameters = parseBlogEntryParameters();
		if (parameters == null) {
			LOGGER.warn("Page request parameters were not correct type: " + getPageRequestParameters());
			throw new RedirectToUrlException(url);
		}
		final BlogEntry blogEntry = getBlogFacade().findEntryByPath(parameters.getUsername(), parameters.getPath());
		if (blogEntry == null) {
			LOGGER.warn("Blog entry was not found!. Username: " + parameters.getUsername() + "\tPath: " + parameters.getPath());
			throw new RedirectToUrlException(url);
		}

		// Increment view count of blog entry
		counterFacade.increment(BlogPlugin.VIEW_COUNT_BLOG_ENTRY, blogEntry.getTrackingCategoryId(), blogEntry.getId());

		setDefaultModel(new Model<BlogEntry>(blogEntry));
		
		// Add blog entry title
		add(new Label("title", new Model<String>(blogEntry.getTitle())));

		// Comment count
		Long commentCount = commentDao.count(blogEntry);
		add(new Label("comment-count", new Model<Long>(commentCount)));

		// Add blog entry create date
		add(new Label("createDate", new Model<String>(dateService.formatWithLongDate(blogEntry.getCreateDate()))));

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

		// Add comment plugin
		add(new CommentContainerPanel("commenting-plugin", new Model<Commentable>(blogEntry), getPageRequestParameters())
				.setCommentPageClass(getBlogFacade().getBlogEntrylink(blogEntry).getKey()));

		// Add tag cloud panel
		TagCloudPanel<JpaBlogEntryTag> tagCloudPanel = new TagCloudPanel<JpaBlogEntryTag>("tag-cloud-panel", KEY_VALUE_BLOG_TAG) {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<JpaBlogEntryTag> findRandomValues(String key, int maxCount) {
				return keyValueDao.findRandomValues(key, maxCount);
			}

		};
		add(tagCloudPanel);
		List<JpaBlogEntryTag> tags = (List<JpaBlogEntryTag>) blogEntry.getTags();
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
			BlogEntry blogEntry = (BlogEntry) getDefaultModelObject();
			getParent().addOrReplace(new Label(LayoutWebPage.PAGE_TITLE, new Model<String>(blogEntry.getTitle())));
			getParent().addOrReplace(new MetaTagWebContainer(LayoutWebPage.PAGE_DESCRIPTION, blogEntry.getDescription()));
			addOrReplaceKeywords(blogEntry);

			if (!blogEntry.getTags().isEmpty()) {
				Collection<String> keywords = new ArrayList<String>();
				keywords.add(StringUtils.join(blogEntry.getTags(), SEPARATOR));

			}
		}

	}

	private void addOrReplaceKeywords(BlogEntry blogEntry) {
		Collection<Object> keywords = new ArrayList<Object>();

		// Add title

		splitAndFilterTitle(keywords, blogEntry.getTitle());
		// Add category if exists
		if (blogEntry.getCategory() != null) {
			keywords.add(blogEntry.getCategory().getName());
		}

		// Add tags if exist
		if (!blogEntry.getTags().isEmpty()) {
			keywords.addAll(blogEntry.getTags());
		}

		// Add all as single string
		String keysAsString = StringUtils.join(keywords, SEPARATOR);
		getParent().addOrReplace(new MetaTagWebContainer(LayoutWebPage.PAGE_KEYWORDS, keysAsString));
	}

	private void splitAndFilterTitle(Collection<Object> keywords, String title) {
		for (String item : title.split(DelimiterEnum.SPACE.value())) {
			if (item.length() > 3) {
				keywords.add(item);
			}
		}
	}
}
