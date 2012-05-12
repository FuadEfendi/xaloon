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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntry;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntryImageComposition;
import org.xaloon.wicket.plugin.blog.path.BlogEntryPathTypeEnum;

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
	private FileDescriptorDao fileDescriptorDao;
	
	@Inject
	private UserFacade userFacade;
	
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
		QueryBuilder queryBuilder = createQueryBuilder("select be.id, be.customPath, be.path, cat.id, be.blogEntryPathType, o.username, be.description, th.id, be.createDate, be.title, count(img) ", blogEntrySearchRequest);

		queryBuilder.setFirstRow(first);
		queryBuilder.setCount(count);
		queryBuilder.addGroup("be.id, be.customPath, be.path, cat.id, be.blogEntryPathType, o.username, be.description, th.id, be.createDate, be.title");
		queryBuilder.addOrderBy("be.sticky desc, be.updateDate desc");
		List<Object[]> result = persistenceServices.executeQuery(queryBuilder);
		return transformBlogEntries(result);
	}

	private List<BlogEntry> transformBlogEntries(List<Object[]> searchResult) {
		List<BlogEntry> result = new ArrayList<BlogEntry>();
		if (searchResult == null || searchResult.isEmpty()) {
			return result;
		}
		for (Object[] item : searchResult) {
			createNewTransientBlogEntry(result, item);
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createNewTransientBlogEntry(List<BlogEntry> blogEntries, Object[] item) {
		JpaBlogEntry result = new JpaBlogEntry();
		result.setId((Long)item[0]);
		result.setCustomPath((String)item[1]);
		result.setPath((String)item[2]);
		Long categoryId = (Long)item[3];
		if (categoryId != null) {
			//result.setCategory((JpaClassifierItem)classifierItemDao.loadClassifierItemById(categoryId));
		}
		result.setBlogEntryPathType((BlogEntryPathTypeEnum)item[4]);
		String username = (String)item[5];
		if (!StringUtils.isEmpty(username)) {
			result.setOwner(userFacade.getUserByUsername(username));
		}
		result.setDescription((String)item[6]);
		Long thumbnailId = (Long)item[7];
		Long imagesCount = (Long)item[10];
		if (thumbnailId != null) {
			result.setThumbnail(fileDescriptorDao.getFileDescriptorById(thumbnailId));
		} else if (imagesCount > 0 ){
			List temporaryList = new ArrayList<ImageComposition>();
			temporaryList.add(getImageThumbnailForBlogEntry(result.getId()));
			result.getImages().addAll(temporaryList);
		}
		
		result.setCreateDate((Date)item[8]);
		result.setTitle((String)item[9]);
		blogEntries.add(result);
	}
	
	private JpaBlogEntryImageComposition getImageThumbnailForBlogEntry(Long id) {
		QueryBuilder query = new QueryBuilder("select i from " + JpaBlogEntry.class.getSimpleName() + " e inner join e.images i inner join i.image image inner join image.thumbnail th");
		query.addParameter("e.id", "_ID", id);
		query.addOrderBy("image.customOrder asc, image.updateDate desc");
		
		query.setCount(1);
		List<JpaBlogEntryImageComposition> result = persistenceServices.executeQuery(query);
		return !result.isEmpty()?result.get(0):null;
	}

	private QueryBuilder createQueryBuilder(String selectString, BlogEntrySearchRequest blogEntrySearchRequest) {
		QueryBuilder queryBuilder = new QueryBuilder(selectString + " from " + JpaBlogEntry.class.getSimpleName() + " be left join be.images img left join be.category cat left join be.owner o left join be.thumbnail th");
		if (!StringUtils.isEmpty(blogEntrySearchRequest.getUsername())) {
			queryBuilder.addParameter("o.username", "USERNAME", blogEntrySearchRequest.getUsername());
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
