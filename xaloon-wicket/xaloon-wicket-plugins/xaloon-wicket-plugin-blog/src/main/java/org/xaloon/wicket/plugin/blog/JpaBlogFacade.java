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
package org.xaloon.wicket.plugin.blog;

import java.io.IOException;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.classifier.dao.ClassifierItemDao;
import org.xaloon.core.api.classifier.search.ClassifierItemSearchRequest;
import org.xaloon.core.api.image.AlbumFacade;
import org.xaloon.core.api.image.ImageCompositionFactory;
import org.xaloon.core.api.image.ImageLocationResolver;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.wicket.plugin.blog.dao.BlogDao;
import org.xaloon.wicket.plugin.blog.model.BlogEntry;
import org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest;
import org.xaloon.wicket.plugin.blog.model.JpaBlogEntry;
import org.xaloon.wicket.plugin.blog.path.BlogEntryPathResolver;
import org.xaloon.wicket.plugin.blog.path.BlogEntryPathTypeEnum;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaBlogFacade implements BlogFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private BlogDao blogDao;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	private ClassifierItemDao classifierItemDao;

	@Inject
	private FileRepositoryFacade fileRepositoryFacade;

	@Inject
	private AlbumFacade albumFacade;
	
	private ImageLocationResolver<BlogEntry> imageLocationResolver;

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#storeBlogEntry(BlogEntry, BlogPluginBean, List, List)
	 */
	@Override
	public void storeBlogEntry(BlogEntry entry, Image thumbnailToAdd, boolean deleteThumbnail, BlogPluginBean pluginBean, List<Image> imagesToDelete,
		List<Image> imagesToAdd) throws IOException {

		// Delete images if any
		albumFacade.deleteImages(entry, imagesToDelete);

		if (deleteThumbnail) {
			fileRepositoryFacade.delete(entry.getThumbnail());
			entry.setThumbnail(null);
		}
		// Set author of blog entry
		entry.setOwner(securityFacade.getCurrentUser());
		if (entry.getOwner() == null || StringUtils.isEmpty(entry.getOwner().getUsername())) {
			throw new RuntimeException("Anonymous blogging is not supported!");
		}

		// Set short description of blog entry
		entry.setDescription(createDescription(entry, pluginBean));

		entry = blogDao.save(entry);
		
		if (thumbnailToAdd != null) {
			ImageCompositionFactory compositionFactory =  new BlogImageCompositionFactory();
			albumFacade.uploadThumbnail(entry, compositionFactory, thumbnailToAdd, getImageLocationResolver().resolveThumbnailLocation(entry));
		}
		//entry = blogDao.save(entry);
		
		// Check and store images for this blog entry
		storeImagesToBlogEntry(pluginBean, entry, imagesToAdd);
		// entry.setThumbnail(null);
		
	}

	private void storeImagesToBlogEntry(BlogPluginBean pluginBean, final BlogEntry entry, final List<Image> imagesToAdd) {
		if (entry == null || imagesToAdd == null || imagesToAdd.isEmpty()) {
			return;
		}
		ImageCompositionFactory compositionFactory =  new BlogImageCompositionFactory();
		albumFacade.addNewImagesToAlbum(entry, compositionFactory, imagesToAdd, getImageLocationResolver().resolveImageLocation(entry), getImageLocationResolver().resolveThumbnailLocation(entry));
	}

	private String createDescription(BlogEntry entry, BlogPluginBean pluginBean) {
		String[] sentences = entry.getContent().replaceAll("\\<.*?\\>", "").replaceAll("(\r\n|\n|\t)", "").trim().split("\\.");
		StringBuilder description = new StringBuilder();
		int i = 0;
		int maxSentenceCount = pluginBean.getBlogDescriptionLengthInSentences();
		while (i < maxSentenceCount && i < sentences.length) {
			String sentence = sentences[i++];
			// Shortcuts should be ignored (Mr., Mrs., etc.)
			if (sentence.length() < 4) {
				maxSentenceCount++;
			}
			description.append(sentence).append(DelimiterEnum.DOT.value());
		}
		return description.toString();
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#getBlogCategories()
	 */
	@Override
	public List<ClassifierItem> getBlogCategories() {
		ClassifierItemSearchRequest classifierItemSearchRequest = new ClassifierItemSearchRequest();
		classifierItemSearchRequest.setClassifierType(BlogPlugin.CLASSIFIER_BLOG_CATEGORY);

		return classifierItemDao.find(classifierItemSearchRequest);
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#deleteThumbnailFromBlogEntry(BlogEntry)
	 */
	@Override
	public void deleteThumbnailFromBlogEntry(BlogEntry entry) {
		FileDescriptor thumbnail = entry.getThumbnail();
		if (thumbnail == null) {
			return;
		}
		entry.setThumbnail(null);
		if (entry.getId() != null) {
			blogDao.save(entry);
		}
		if (thumbnail != null) {
			fileRepositoryFacade.delete(entry.getThumbnail());
		}
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#deleteBlogEntryByPath(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteBlogEntryByPath(String username, String blogEntryPath) {
		BlogEntry blogEntry = blogDao.findEntryByPath(username, blogEntryPath);
		if (blogEntry == null) {
			return;
		}
		fileRepositoryFacade.delete(blogEntry.getThumbnail());
		albumFacade.deleteAlbum(blogEntry);
		blogDao.deleteBlogEntry(blogEntry);
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#findAvailableBlogEntryList(int, int)
	 */
	@Override
	public List<BlogEntry> findAvailableBlogEntryList(int first, int count) {
		return blogDao.findAvailableBlogEntryList(first, count);
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#getCount()
	 */
	@Override
	public Long getCount() {
		return blogDao.getCount();
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#findAvailableBlogEntryList(org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest, int, int)
	 */
	@Override
	public List<BlogEntry> findAvailableBlogEntryList(BlogEntrySearchRequest blogEntrySearchRequest, int first, int count) {
		return blogDao.findAvailableBlogEntryList(blogEntrySearchRequest, first, count);
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#getCount(org.xaloon.wicket.plugin.blog.model.BlogEntrySearchRequest)
	 */
	@Override
	public Long getCount(BlogEntrySearchRequest blogEntrySearchRequest) {
		return blogDao.getCount(blogEntrySearchRequest);
	}

	/**
	 * @see org.xaloon.wicket.plugin.blog.BlogFacade#findEntryByPath(java.lang.String, java.lang.String)
	 */
	@Override
	public BlogEntry findEntryByPath(String username, String blogEntryPath) {
		return blogDao.findEntryByPath(username, blogEntryPath);
	}

	/**
	 * @param blogEntry
	 * @return absolute path of blog entry
	 */
	public String resolveLink(BlogEntry blogEntry) {
		KeyValue<Class<? extends Page>, PageParameters> blogEntryLink = getBlogEntrylink(blogEntry);
		return UrlUtils.toAbsolutePath(blogEntryLink.getKey(), blogEntryLink.getValue());
	}

	/**
	 * @param blogEntry
	 * @return collection of blog entry page class and parameters
	 */
	public KeyValue<Class<? extends Page>, PageParameters> getBlogEntrylink(BlogEntry blogEntry) {
		BlogEntryPathTypeEnum blogEntryPathTypeEnum = BlogEntryPathTypeEnum.USERNAME_DATE_TITLE;
		if (blogEntry.getBlogEntryPathType() != null) {
			blogEntryPathTypeEnum = blogEntry.getBlogEntryPathType();
		}
		BlogEntryPathResolver blogEntryPathResolver = ServiceLocator.get().getInstance(BlogEntryPathResolver.class, blogEntryPathTypeEnum.name());
		if (blogEntryPathResolver == null) {
			throw new RuntimeException(String.format("Blog entry path type is not supported: %s", blogEntry.getBlogEntryPathType().name()));
		}
		return blogEntryPathResolver.getBlogEntryLink(blogEntry);
	}

	@Override
	public BlogEntry newBlogEntry() {
		BlogEntry blogEntry = new JpaBlogEntry();
		blogEntry.setBlogEntryPathType(BlogEntryPathTypeEnum.USERNAME_DATE_TITLE);// default one

		return blogEntry;
	}

	@Override
	public Image newImage() {
		return albumFacade.newImage();
	}

	/**
	 * @return the imageLocationResolver
	 */
	@SuppressWarnings("unchecked")
	public ImageLocationResolver<BlogEntry> getImageLocationResolver() {
		if (imageLocationResolver == null) {
			imageLocationResolver = ServiceLocator.get().getInstance(ImageLocationResolver.class);
		}
		return imageLocationResolver;
	}
	
	
}
