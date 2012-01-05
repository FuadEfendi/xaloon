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
package org.xaloon.wicket.plugin.blog.sitemap;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.wicket.component.sitemap.DynamicLinkProvider;
import org.xaloon.wicket.plugin.blog.BlogFacade;
import org.xaloon.wicket.plugin.blog.dao.BlogDao;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;

/**
 * @author vytautas r.
 */
@Named
public class BlogEntryDynamicLinkProvider implements DynamicLinkProvider {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private BlogDao blogDao;

	@Inject
	BlogFacade blogFacade;

	@Override
	public List<KeyValue<Class<? extends Page>, PageParameters>> retrieveSiteMapPageList() {
		List<KeyValue<Class<? extends Page>, PageParameters>> result = new ArrayList<KeyValue<Class<? extends Page>, PageParameters>>();
		List<BlogEntry> blogEntries = blogDao.findAvailableBlogEntryList(-1, -1);

		for (BlogEntry blogEntry : blogEntries) {
			KeyValue<Class<? extends Page>, PageParameters> blogEntryLink = blogFacade.getBlogEntrylink(blogEntry);
			result.add(blogEntryLink);
		}
		return result;
	}

}
