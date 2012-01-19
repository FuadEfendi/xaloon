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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.image.AlbumFacade;
import org.xaloon.core.api.image.model.Album;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.storage.DefaultInputStreamContainer;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.storage.InputStreamContainerOptions;
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

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultAlbumFacade.class);

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Inject
	private FileRepositoryFacade fileRepositoryFacade;

	public Album newAlbum() {
		return new JpaAlbum();
	}

	@Override
	public Image newImage() {
		return new JpaImage();
	}

	@Override
	public Album createNewAlbum(User owner, String title, String description,
			Album parent) {
		Album album = newAlbum();
		album.setOwner(owner);
		album.setTitle(title);
		album.setDescription(description);
		album.setParent(parent);
		persistenceServices.create(album);
		return album;
	}

	@Override
	public void addNewImagesToAlbum(final Album album, List<Image> imagesToAdd,
			final String imageLocation, final String thumbnailLocation) {
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

	private void deleteImages(Album album, List<Image> imagesToDelete,
			boolean deleteAlbum) {
		if (album == null || imagesToDelete == null || imagesToDelete.isEmpty()) {
			return;
		}
		for (Image image : imagesToDelete) {
			FileDescriptor thumbnailFileDescriptor = image.getThumbnail();

			if (thumbnailFileDescriptor != null) {
				fileRepositoryFacade.deleteFile(thumbnailFileDescriptor);
			}
			fileRepositoryFacade.deleteFile(image);

			// Just remove image from album image list
			album.getImages().remove(image);
		}
	}

	@Override
	public void deleteAlbum(Album imageAlbum) {
		if (imageAlbum == null) {
			return;
		}
		List<Image> toDelete = new ArrayList<Image>(imageAlbum.getImages());
		deleteImages(imageAlbum, toDelete, true);
	}

	@Override
	public FileDescriptor createPhysicalFile(Image temporaryImage)
			throws MalformedURLException, IOException {
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
	public FileDescriptor createPhysicalFile(Image temporaryImage,
			FileDescriptor existingToUpdate) throws MalformedURLException,
			IOException {
		FileDescriptor result = fileRepositoryFacade.newFileDescriptor();
		temporaryImage.setGenerateUuid(temporaryImage.isGenerateUuid()
				|| result != null);
		if (existingToUpdate != null) {
			result = existingToUpdate;
		}
		updateFileDescriptor(temporaryImage, result);

		if (temporaryImage.isExternal() && !temporaryImage.isResize()) {
			return result;
		}
		InputStreamContainer inputStreamContainer = null;
		InputStreamContainerOptions options = new InputStreamContainerOptions()
				.setHeight(temporaryImage.getHeight())
				.setWidth(temporaryImage.getWidth())
				.setResize(temporaryImage.isResize());

		if (temporaryImage.isExternal()) {
			inputStreamContainer = new UrlInputStreamContainer(temporaryImage.getExternalImage());
		} else {
			inputStreamContainer = temporaryImage.getImageInputStreamContainer();
		}
		inputStreamContainer.setOptions(options);
		if (inputStreamContainer != null && !inputStreamContainer.isEmpty()) {
			return fileRepositoryFacade.storeFile(result, inputStreamContainer);
		}
		return result;
	}

	private void updateFileDescriptor(Image temporaryImage,
			FileDescriptor result) {

		result.setSize(temporaryImage.getSize());
		result.setMimeType(temporaryImage.getMimeType());
		result.setName(temporaryImage.getName());
		if (StringUtils.isEmpty(result.getLocation())) {
			result.setLocation(temporaryImage.getLocation());
		}
		if (StringUtils.isEmpty(result.getName())) {
			result.setName(temporaryImage.getPath());
		}
		if (temporaryImage.isModifyPath()) {
			result.setPath(Configuration
					.get()
					.getFileDescriptorAbsolutePathStrategy()
					.generateAbsolutePath(result,
							temporaryImage.isGenerateUuid(),
							temporaryImage.getPathPrefix()));
		} else {
			result.setPath(temporaryImage.getPath());
		}
	}

	/**
	 * @param album
	 * @param newImage
	 * @param imageLocation
	 * @param thumbnailLocation
	 */
	public void createImage(Album album, Image newImage, String imageLocation,
			String thumbnailLocation) {
		newImage.setOwner(album.getOwner());
		album.getImages().add(newImage);
		try {
			if (!newImage.isExternal()) {
				newImage.setGenerateUuid(true);
				newImage.setModifyPath(true);
			}
			// Make a copy of input stream for thumbnail processing
			InputStream copy = new ByteArrayInputStream(
					IOUtils.toByteArray(newImage.getImageInputStreamContainer()
							.getInputStream()));
			InputStreamContainer inputStreamContainer = new DefaultInputStreamContainer(
					copy);

			// Threats image as original file descriptor and modifies required
			// properties
			newImage.setLocation(imageLocation);
			createPhysicalFile(newImage, newImage);

			// Always create thumbnail
			createThumbnail(newImage, thumbnailLocation, inputStreamContainer);
		} catch (MalformedURLException e) {
			LOGGER.error(
					String.format("URL exception: %s", newImage.getPath()), e);
		} catch (IOException e) {
			LOGGER.error("Could not store file.", e);
		}
	}

	private void createThumbnail(Image newImage, String thumbnailLocation,
			InputStreamContainer thumbnailInputStreamContainer)
			throws MalformedURLException, IOException {
		FileDescriptor fileDescriptor = fileRepositoryFacade
				.newFileDescriptor();
		fileDescriptor.setLocation(thumbnailLocation);
		newImage.setResize(true);
		newImage.setModifyPath(true);
		newImage.setWidth(158);
		newImage.setHeight(82);
		newImage.setImageInputStreamContainer(thumbnailInputStreamContainer);
		newImage.setThumbnail(createPhysicalFile(newImage, fileDescriptor));
	}
}
