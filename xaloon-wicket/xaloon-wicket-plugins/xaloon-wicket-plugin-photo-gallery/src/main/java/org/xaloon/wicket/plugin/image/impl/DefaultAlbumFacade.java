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
package org.xaloon.wicket.plugin.image.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.image.AlbumFacade;
import org.xaloon.core.api.image.ImageOptions;
import org.xaloon.core.api.image.ImageRepository;
import org.xaloon.core.api.image.ImageSize;
import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.storage.UrlInputStreamContainer;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.JpaCategoryPrimaryKey;
import org.xaloon.wicket.plugin.image.model.JpaAlbum;
import org.xaloon.wicket.plugin.image.model.JpaImage;

/**
 * @author vytautas r.
 */
@Named("albumFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefaultAlbumFacade implements AlbumFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultAlbumFacade.class);

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Inject
	private FileRepositoryFacade fileRepositoryFacade;

	private ImageRepository imageRepository;

	public Album newAlbum() {
		return new JpaAlbum();
	}

	@Override
	public Image newImage() {
		return new JpaImage();
	}

	@Override
	public Album createNewAlbum(User owner, String title, String description, Album parent) {
		Album album = newAlbum();
		album.setOwner(owner);
		album.setTitle(title);
		album.setDescription(description);
		album.setParent(parent);
		persistenceServices.create(album);
		return album;
	}

	@Override
	public void addNewImagesToAlbum(final Album album, List<Image> imagesToAdd, final String imageLocation, final String thumbnailLocation) {
		if (album == null || imagesToAdd == null || imagesToAdd.isEmpty()) {
			throw new IllegalArgumentException("Missing arguments!");
		}
		for (final Image image : imagesToAdd) {
			if (image.getId() == null) {
				createImage(album, image, imageLocation, thumbnailLocation);
			}
		}
	}

	@Override
	public void deleteImages(Album album, List<Image> imagesToDelete) {
		deleteImages(album, imagesToDelete, false);
	}

	private void deleteImages(Album album, List<Image> imagesToDelete, boolean deleteAlbum) {
		if (album == null || imagesToDelete == null || imagesToDelete.isEmpty()) {
			return;
		}
		for (Image image : imagesToDelete) {
			FileDescriptor thumbnailFileDescriptor = image.getThumbnail();

			if (thumbnailFileDescriptor != null) {
				fileRepositoryFacade.deleteFile(thumbnailFileDescriptor);
			}
			fileRepositoryFacade.deleteFile(image);
			persistenceServices.remove(image);
		}
	}

	@Override
	public void deleteAlbum(Album imageAlbum) {
		if (imageAlbum == null) {
			return;
		}
		List<Image> toDelete = new ArrayList<Image>(getImagesByAlbum(imageAlbum));
		deleteImages(imageAlbum, toDelete, true);
		//TODO remove album
	}

	@Override
	public FileDescriptor createPhysicalFile(Image temporaryImage) throws MalformedURLException, IOException {
		return createPhysicalFile(temporaryImage, null);
	}

	/**
	 * Parses image and stores it.
	 * 
	 * @param temporaryImage
	 * @param existingToUpdate
	 * @param imageLocation
	 * 
	 * @return file descriptor
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public FileDescriptor createPhysicalFile(Image temporaryImage, FileDescriptor existingToUpdate) throws MalformedURLException, IOException {
		return null;
	}

	/**
	 * @param album
	 * @param newImage
	 * @param imageLocation
	 * @param thumbnailLocation
	 */
	public void createImage(Album album, Image newImage, String imageLocation, String thumbnailLocation) {
		newImage.setReferer(new JpaCategoryPrimaryKey(album.getId(), album.getTrackingCategoryId()));
		newImage.setOwner(album.getOwner());
		
		// Threats image as original file descriptor and modifies required
		// properties
		newImage.setLocation(imageLocation);

		ImageSize thumbnailSize = new ImageSize(158).setHeight(82).location(thumbnailLocation).title(newImage.getName());
		InputStreamContainer inputStreamContainer = null;
		if (newImage.isExternal()) {
			inputStreamContainer = new UrlInputStreamContainer(newImage.getPath());
		} else {
			inputStreamContainer = newImage.getImageInputStreamContainer();
		}
		ImageOptions options = new ImageOptions(inputStreamContainer, thumbnailSize);
		if (!newImage.isExternal()) {
			options.setGenerateUuid(true);
			options.setModifyPath(true);
			newImage.setPath(Configuration.get().getFileDescriptorAbsolutePathStrategy().generateAbsolutePath(newImage, true, ""));
		}
		getImageRepository().uploadImage(newImage, options);
	}

	@Override
	public void save(Image image) {
		persistenceServices.edit(image);
	}

	@Override
	public void deleteImagesByUsername(User userToBeDeleted) {
		QueryBuilder update = new QueryBuilder("delete from " + JpaImage.class.getSimpleName() + " i");
		update.addParameter("i.owner", "_USER", userToBeDeleted);
		persistenceServices.executeUpdate(update);
	}

	@Override
	public void deleteAlbumsByUsername(User userToBeDeleted) {
		QueryBuilder update = new QueryBuilder("delete from " + JpaAlbum.class.getSimpleName() + " a");
		update.addParameter("a.owner", "_USER", userToBeDeleted);
		persistenceServices.executeUpdate(update);
	}

	/**
	 * @return the imageRepository
	 */
	public ImageRepository getImageRepository() {
		if (imageRepository == null) {
			imageRepository = ServiceLocator.get().getInstance(ImageRepository.class);
		}
		return imageRepository;
	}

	@Override
	public List<Image> getImagesByAlbum(Album album) {
		if (album.getId() == null) {
			return new ArrayList<Image>();
		}
		QueryBuilder query = new QueryBuilder("select i from " + JpaImage.class.getSimpleName() + " i");
		query.addParameter("i.referer.categoryId", "CATEGORY_ID", album.getTrackingCategoryId());
		query.addParameter("i.referer.entityId", "ENTITY_ID", album.getId());
		return persistenceServices.executeQuery(query);
	}
}
