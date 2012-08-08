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
package org.xaloon.wicket.plugin.blog.rss;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.component.rss.RssFeed;
import org.xaloon.wicket.component.rss.RssItem;
import org.xaloon.wicket.plugin.blog.BlogFacade;
import org.xaloon.wicket.plugin.blog.dao.BlogDao;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class BlogRssFeed extends RssFeed {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private BlogDao blogDao;

	@Inject
	private BlogFacade blogFacade;

	@Override
	protected List<RssItem> getRssItems() {
		List<BlogEntry> blogEntries = blogDao.findAvailableBlogEntryList(-1, -1);
		List<RssItem> rssItems = new ArrayList<RssItem>();
		convertToRss(blogEntries, rssItems);
		return rssItems;
	}

	private void convertToRss(List<BlogEntry> blogEntries, List<RssItem> rssItems) {
		for (BlogEntry blogEntry : blogEntries) {
			RssItem rssItem = convertItem(blogEntry);
			if (rssItem != null) {
				rssItems.add(rssItem);
			}
		}
	}

	private RssItem convertItem(BlogEntry blogEntry) {
		RssItem result = new RssItem();
		result.setTitle(blogEntry.getTitle());
		result.setLink(blogFacade.resolveLink(blogEntry));
		result.setPubDate(blogEntry.getCreateDate());
		result.setDescription(createDescription(blogEntry));
		if (blogEntry.getCategory() != null) {
			result.setCategory(blogEntry.getCategory().getName());
		}
		return result;
	}

	private String createDescription(BlogEntry blogEntry) {
		StringBuilder result = new StringBuilder();
		result.append("<![CDATA[");
		if (blogEntry.getThumbnail() != null) {
			result.append(getImageLink(blogEntry, blogEntry.getThumbnail()));
		} else if (!blogEntry.getImages().isEmpty()) {
			Image image = blogEntry.getImages().get(0).getImage();
			if (image != null && image.getThumbnail() != null) {
				result.append(getImageLink(blogEntry, image.getThumbnail()));
			}
		}
		if (!StringUtils.isEmpty(blogEntry.getDescription())) {
			result.append(blogEntry.getDescription());
		}
		result.append("]]>");

		return result.toString();
	}

	private String getImageLink(BlogEntry blogEntry, FileDescriptor fileDescriptor) {
		StringBuilder imageLink = new StringBuilder();
		imageLink.append("<a href=\"");
		imageLink.append(blogFacade.resolveLink(blogEntry));
		imageLink.append("\">");

		imageLink.append("<img border=\"0\" src=\"");
		imageLink.append(UrlUtils.toAbsoluteImagePath(ImageLink.IMAGE_RESOURCE, fileDescriptor.getPath()));
		imageLink.append("\"/>");
		imageLink.append("</a>");

		return imageLink.toString();
	}
}
