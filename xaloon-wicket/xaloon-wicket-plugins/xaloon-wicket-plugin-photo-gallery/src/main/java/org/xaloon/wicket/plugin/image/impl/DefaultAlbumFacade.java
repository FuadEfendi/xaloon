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
import org.xaloon.core.api.image.ImageCompositionFactory;
import org.xaloon.core.api.image.ImageOptions;
import org.xaloon.core.api.image.ImageRepository;
import org.xaloon.core.api.image.ImageSize;
import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.storage.UrlInputStreamContainer;
import org.xaloon.core.api.user.model.User;
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
	public void addNewImagesToAlbum(Album album, List<ImageComposition> imagesToAdd, final String imageLocation, final String thumbnailLocation) {
		if (imagesToAdd == null || imagesToAdd.isEmpty()) {
			throw new IllegalArgumentException("Missing arguments!");
		}
		for (final ImageComposition image : imagesToAdd) {
			if (image.getId() == null) {
				createImage(album, image, imageLocation, thumbnailLocation);
			}
		}
	}

	@Override
	public void deleteImages(Album album, List<ImageComposition> imagesToDelete) {
		deleteImages(album, imagesToDelete, false);
	}

	private void deleteImages(Album album, List<ImageComposition> imagesToDelete, boolean deleteAlbum) {
		if (album == null || imagesToDelete == null || imagesToDelete.isEmpty()) {
			return;
		}
		for (ImageComposition image : imagesToDelete) {
			FileDescriptor thumbnailFileDescriptor = image.getImage().getThumbnail();

			if (thumbnailFileDescriptor != null) {
				fileRepositoryFacade.deleteFile(thumbnailFileDescriptor);
			}
			fileRepositoryFacade.deleteFile(image.getImage());
			album.getImages().remove(image);
			persistenceServices.remove(image);
		}
	}

	@Override
	public void deleteAlbum(Album imageAlbum) {
		if (imageAlbum == null) {
			return;
		}
		List<ImageComposition> toDelete = new ArrayList<ImageComposition>(getImagesByAlbum(imageAlbum));
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
	public void createImage(Album album, ImageComposition newImage, String imageLocation, String thumbnailLocation) {
		newImage.setObject(album);
		newImage.getImage().setOwner(album.getOwner());
		
		
		// Threats image as original file descriptor and modifies required properties
		newImage.getImage().setLocation(imageLocation);

		ImageOptions options = newImageOptions(newImage.getImage(), thumbnailLocation);
		getImageRepository().uploadImage(newImage, options);
	}

	private ImageOptions newImageOptions(Image newImage, String thumbnailLocation) {
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
		return options;
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
	public List<ImageComposition> getImagesByAlbum(Album album) {
		if (album.getId() == null) {
			return new ArrayList<ImageComposition>();
		}
		QueryBuilder query = new QueryBuilder("select composition from " + album.getClass().getSimpleName() + " a inner join a.images composition inner join composition.image i");
		query.addParameter("a.id", "_ID", album.getId());
		query.addOrderBy("i.customOrder asc");
		return persistenceServices.executeQuery(query);
	}

	@Override
	public <T extends Album> T uploadThumbnail(T album, Image thumbnailToAdd, String thumbnailLocation) {
		thumbnailToAdd.setOwner(album.getOwner());
		thumbnailToAdd.setLocation(thumbnailLocation);
		ImageOptions options = newImageOptions(thumbnailToAdd, thumbnailLocation);
		return getImageRepository().uploadThumbnail(album, thumbnailToAdd, options);
	}
	
	@Override
	public <T extends Image> T uploadThumbnail(T image, Image thumbnailToAdd, String thumbnailLocation) {
		thumbnailToAdd.setOwner(image.getOwner());
		thumbnailToAdd.setLocation(thumbnailLocation);
		ImageOptions options = newImageOptions(thumbnailToAdd, thumbnailLocation);
		return getImageRepository().uploadThumbnail(image, thumbnailToAdd, options);
	}
}
