/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xaloon.wicket.plugin.blog.panel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.date.DateService;
import org.xaloon.core.api.keyvalue.KeyValueDao;
import org.xaloon.core.api.plugin.comment.CommentDao;
import org.xaloon.wicket.component.classifier.panel.ClassifierConstants;
import org.xaloon.wicket.component.navigation.BookmarkablePagingNavigator;
import org.xaloon.wicket.component.tag.TagCloudPanel;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.blog.BlogEntryParameters;
import org.xaloon.wicket.plugin.blog.BlogFacade;
import org.xaloon.wicket.plugin.blog.BlogPageConstants;
import org.xaloon.wicket.plugin.blog.BlogPlugin;
import org.xaloon.wicket.plugin.blog.BlogPluginBean;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntryTag;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListByBloggerPage;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListByCategoryPage;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListByTagPage;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListPage;
import org.xaloon.wicket.plugin.blog.page.CreateBlogEntryPage;

/**
 * Generated link of blog entry: "http[s]://host/[username]/[year/month]/blog-entry-path"
 * 
 * @author vytautas r.
 */
public abstract class AbstractBlogPluginPanel extends AbstractPluginPanel<BlogPluginBean, BlogPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final String KEY_VALUE_BLOG_TAG = "BLOG_ENTRY_TAG";

	private final static List<String> AVAILABLE_PARAMETERS = new ArrayList<String>();

	static {
		AVAILABLE_PARAMETERS.add(BlogPageConstants.BLOG_USERNAME);
		AVAILABLE_PARAMETERS.add(BlogPageConstants.BLOG_YEAR);
		AVAILABLE_PARAMETERS.add(BlogPageConstants.BLOG_MONTH);
		AVAILABLE_PARAMETERS.add(BlogPageConstants.BLOG_DAY);
		AVAILABLE_PARAMETERS.add(BlogPageConstants.BLOG_PATH);
		AVAILABLE_PARAMETERS.add(ClassifierConstants.PARENT_ITEM);
		AVAILABLE_PARAMETERS.add(TagCloudPanel.QUERY_BY_TAG);
		AVAILABLE_PARAMETERS.add(BookmarkablePagingNavigator.PAGE_QUERY_ID);
	}
	
	@Inject
	protected BlogFacade blogFacade;

	@Inject
	protected CommentDao commentDao;
	
	@Inject
	@Named("blogEntryKeyValueDao")
	protected KeyValueDao<String, String, JpaBlogEntryTag> keyValueDao;

	@Inject
	protected DateService dateService;
	
	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public AbstractBlogPluginPanel(String id) {
		super(id);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public AbstractBlogPluginPanel(String id, PageParameters pageParameters) {
		super(id, pageParameters);
	}

	protected Class<? extends Page> getCreateBlogEntryPageClass() {
		return CreateBlogEntryPage.class;
	}

	protected Class<? extends Page> getBlogCategoryPageClass() {
		return BlogEntryListByCategoryPage.class;
	}

	protected Class<? extends Page> getBlogBloggerPageClass() {
		return BlogEntryListByBloggerPage.class;
	}

	protected Class<? extends Page> getBlogEntryListPageClass() {
		return BlogEntryListPage.class;
	}

	protected Class<? extends Page> getBlogEntryListByTagPageClass() {
		return BlogEntryListByTagPage.class;
	}

	protected BlogFacade getBlogFacade() {
		return blogFacade;
	}

	/**
	 * Blog parameter management
	 * 
	 * @return parsed parameters
	 */
	protected BlogEntryParameters parseBlogEntryParameters() {
		PageParameters requestParameters = getPageRequestParameters();

		BlogEntryParameters result = new BlogEntryParameters();
		if (!requestParameters.get(BlogPageConstants.BLOG_USERNAME).isEmpty()) {
			result.setUsername(requestParameters.get(BlogPageConstants.BLOG_USERNAME).toString());
		}
		if (!requestParameters.get(BlogPageConstants.BLOG_YEAR).isEmpty()) {
			result.setYear(requestParameters.get(BlogPageConstants.BLOG_YEAR).toInt());
		}
		if (!requestParameters.get(BlogPageConstants.BLOG_MONTH).isEmpty()) {
			result.setMonth(requestParameters.get(BlogPageConstants.BLOG_MONTH).toInt());
		}
		if (!requestParameters.get(BlogPageConstants.BLOG_DAY).isEmpty()) {
			result.setDay(requestParameters.get(BlogPageConstants.BLOG_DAY).toInt());
		}
		if (!requestParameters.get(BlogPageConstants.BLOG_PATH).isEmpty()) {
			result.setPath(requestParameters.get(BlogPageConstants.BLOG_PATH).toString());
		}
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}
	
	@Override
	protected PageParameters cleanupPageRequestParameters(PageParameters pageRequestParameters) {
		PageParameters result = new PageParameters();
		
		for (String key : AVAILABLE_PARAMETERS) {
			if (!pageRequestParameters.get(key).isEmpty()) {
				result.add(key, pageRequestParameters.get(key));
			}
		}
		return pageRequestParameters.overwriteWith(result);
	}

	protected BookmarkablePageLink<Void> createBlogCategoryLink(final BlogEntry blogEntry) {
		PageParameters categoryPageParameters = new PageParameters();
		BookmarkablePageLink<Void> categoryLink = new BookmarkablePageLink<Void>("link-category", getBlogCategoryPageClass(), categoryPageParameters);
		if (blogEntry.getCategory() != null) {
			categoryPageParameters.set(ClassifierConstants.PARENT_ITEM, blogEntry.getCategory().getCode());
			categoryLink.add(new Label("label-category", new Model<String>(blogEntry.getCategory().getName())));
		} else {
			categoryLink.setVisible(false);
		}
		return categoryLink;
	}

	protected BookmarkablePageLink<Void> createBlogAuthorLink(final BlogEntry blogEntry) {
		PageParameters authorPageParameters = new PageParameters();
		BookmarkablePageLink<Void> authorLink = new BookmarkablePageLink<Void>("link-author", getBlogBloggerPageClass(), authorPageParameters);
		if (blogEntry.getOwner() != null) {
			authorPageParameters.set(BlogPageConstants.BLOG_USERNAME, blogEntry.getOwner().getUsername());
			authorLink.add(new Label("author", new Model<String>(blogEntry.getOwner().getDisplayName())));
		} else {
			authorLink.setVisible(false);
		}
		return authorLink;
	}
}
