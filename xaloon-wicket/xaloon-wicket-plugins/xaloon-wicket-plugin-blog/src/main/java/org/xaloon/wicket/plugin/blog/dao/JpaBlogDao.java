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
package org.xaloon.wicket.plugin.blog.dao;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntry;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaBlogDao implements BlogDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;
	
	public BlogEntry save(BlogEntry blogEntry) {
		blogEntry = persistenceServices.edit(blogEntry);
		return blogEntry;
	}

	public boolean deleteBlogEntry(BlogEntry blogEntry) {
		if (blogEntry == null) {
			throw new IllegalArgumentException("Blog entry was not provided!");
		}
		persistenceServices.remove(JpaBlogEntry.class, blogEntry.getId());
		return true;
	}

	@Override
	public Long getCount() {
		return persistenceServices.getCount(JpaBlogEntry.class);
	}

	@Override
	public Long getCount(BlogEntrySearchRequest blogEntrySearchRequest) {
		QueryBuilder queryBuilder = createQueryBuilder("select count(be) ", blogEntrySearchRequest);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public JpaBlogEntry findEntryByPath(String username, String blogEntryPath) {
		BlogEntrySearchRequest blogEntrySearchRequest = new BlogEntrySearchRequest();
		blogEntrySearchRequest.setUsername(username);
		blogEntrySearchRequest.setBlogEntryPath(blogEntryPath);

		QueryBuilder queryBuilder = createQueryBuilder("select be ", blogEntrySearchRequest);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public List<BlogEntry> findAvailableBlogEntryList(BlogEntrySearchRequest blogEntrySearchRequest, int first, int count) {
		QueryBuilder queryBuilder = createQueryBuilder("select be ", blogEntrySearchRequest);

		queryBuilder.setFirstRow(first);
		queryBuilder.setCount(count);
		queryBuilder.addOrderBy("be.sticky desc, be.updateDate desc");
		List<BlogEntry> result = persistenceServices.executeQuery(queryBuilder);
		return result;
	}

	private QueryBuilder createQueryBuilder(String selectString, BlogEntrySearchRequest blogEntrySearchRequest) {
		QueryBuilder queryBuilder = new QueryBuilder(selectString + " from " + JpaBlogEntry.class.getSimpleName() + " be");
		if (!StringUtils.isEmpty(blogEntrySearchRequest.getUsername())) {
			queryBuilder.addParameter("be.owner.username", "USERNAME", blogEntrySearchRequest.getUsername());
		}

		if (!StringUtils.isEmpty(blogEntrySearchRequest.getCategory())) {
			queryBuilder.addJoin(QueryBuilder.INNER_JOIN, "be.category category");
			queryBuilder.addParameter("category.code", "CATEGORY_CODE", blogEntrySearchRequest.getCategory());
		}

		if (!StringUtils.isEmpty(blogEntrySearchRequest.getTagPath())) {
			queryBuilder.addJoin(QueryBuilder.INNER_JOIN, "be.tags tag");
			queryBuilder.addParameter("tag.path", "TAG_VALUE", blogEntrySearchRequest.getTagPath());
		}

		if (!StringUtils.isEmpty(blogEntrySearchRequest.getBlogEntryPath())) {
			queryBuilder.addParameter("be.path", "PATH", blogEntrySearchRequest.getBlogEntryPath());
		}
		return queryBuilder;
	}

	@Override
	public List<BlogEntry> findAvailableBlogEntryList(int first, int count) {
		return findAvailableBlogEntryList(new BlogEntrySearchRequest(), first, count);
	}

	@Override
	public void deleteBlogsByUsername(User userToBeDeleteds) {
		QueryBuilder update = new QueryBuilder("delete from " + JpaBlogEntry.class.getSimpleName() + " b");
		update.addParameter("b.owner", "_USER", userToBeDeleteds);
		persistenceServices.executeUpdate(update);
	}
}
