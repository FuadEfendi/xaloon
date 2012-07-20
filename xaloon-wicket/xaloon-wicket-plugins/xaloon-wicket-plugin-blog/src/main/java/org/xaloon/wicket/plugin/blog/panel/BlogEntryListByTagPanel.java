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

import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.component.tag.TagCloudPanel;
import org.xaloon.wicket.plugin.blog.ldm.BlogEntryDetachableModel;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest;
import org.xaloon.wicket.plugin.blog.page.BlogEntryListPage;
import org.xaloon.wicket.util.Link;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class BlogEntryListByTagPanel extends BlogEntryListPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public BlogEntryListByTagPanel(String id, PageParameters pageParameters) {
		super(id, pageParameters);
	}

	@Override
	protected Link getCurrentRedirectLink() {
		return new Link(getBlogEntryListByTagPageClass(), getPageRequestParameters());
	}

	@Override
	protected IDataProvider<BlogEntry> getBlogEntryDataProvider() {
		String url = UrlUtils.generateFullvalue(BlogEntryListPage.class);
		if (getPageRequestParameters().isEmpty()) {
			throw new RedirectToUrlException(url);
		}
		String tagPath = getPageRequestParameters().get(TagCloudPanel.QUERY_BY_TAG).toString();
		if (StringUtils.isEmpty(tagPath)) {
			throw new RedirectToUrlException(url);
		}
		return new JpaBlogEntryDataByTagProvider(tagPath);
	}

	private class JpaBlogEntryDataByTagProvider implements IDataProvider<BlogEntry> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private BlogEntrySearchRequest blogEntrySearchRequest = new BlogEntrySearchRequest();

		public JpaBlogEntryDataByTagProvider(String tagPath) {
			blogEntrySearchRequest.setTagPath(tagPath);
		}

		@Override
		public void detach() {
		}

		@Override
		public Iterator<? extends BlogEntry> iterator(long first, long count) {
			return getBlogFacade().findAvailableBlogEntryList(blogEntrySearchRequest, first, count).iterator();
		}

		@Override
		public long size() {
			return getBlogFacade().getCount(blogEntrySearchRequest);
		}

		@Override
		public IModel<BlogEntry> model(BlogEntry object) {
			return new BlogEntryDetachableModel(object.getOwner().getUsername(), object.getPath());
		}
	}
}
