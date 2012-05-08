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
package org.xaloon.core.api.image;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.util.DefaultKeyValue;

public abstract class AbstractImageRepository implements ImageRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractImageRepository.class);

	@Inject
	private PersistenceServices persistenceServices;

	@Inject
	private FileDescriptorDao fileDescriptorDao;

	@Override
	public Image uploadImage(Image image, ImageOptions options) {
		try {
			// Create image
			storeOriginalFile(image, options);

			if (options.getImageSize() != null) {
				// Create copy
				ImageOptions copy = new ImageOptions(options.getImageInputStreamContainer(), options.getImageSize());
				copy.setGenerateUuid(options.isGenerateUuid());
				// create thumbnail if necessary
				FileDescriptor thumbnail = uploadFileDescriptor(image, copy);
				image.setThumbnail(thumbnail);
			}

			if (options.getAdditionalImageSizes() != null) {
				// create additional sizes if necessary
				List<FileDescriptor> items = new ArrayList<FileDescriptor>();
				for (ImageSize additionalSize : options.getAdditionalImageSizes()) {
					// Create copy
					ImageOptions copy = new ImageOptions(options.getImageInputStreamContainer(), additionalSize);
					copy.setGenerateUuid(options.isGenerateUuid());

					FileDescriptor item = uploadFileDescriptor(image, copy);
					items.add(item);
				}
				if (!items.isEmpty()) {
					image.setAdditionalSizes(items);
				}
			}
			return persistenceServices.createOrEdit(image);
		} catch (Exception e) {
			LOGGER.error("Could not store image using picasa service", e);
			if (getAlternativeImageRepository() != null) {
				return getAlternativeImageRepository().uploadImage(image, options);
			}
		} finally {
			options.getImageInputStreamContainer().close();
		}
		LOGGER.warn("Could not store image using any provider. Giving up.");
		return null;
	}

	private void storeOriginalFile(Image image, ImageOptions options) {
		KeyValue<String, String> originalImageUid = null;
		// store physical file only if it ir from local file system
		if (!image.isExternal()) {
			originalImageUid = getFileStorageService().storeFile(image, options.getImageInputStreamContainer());
		} else {
			originalImageUid = new DefaultKeyValue<String, String>(image.getPath(), null);
		}
		image.setIdentifier(originalImageUid.getValue());
		image.setPath(originalImageUid.getKey());
		image.setFileStorageServiceProvider(getFileStorageService().getName());
	}

	private FileDescriptor uploadFileDescriptor(Image image, ImageOptions options) throws IOException {
		FileDescriptor fileDescriptor = createFileDescriptor(image, options);
		KeyValue<String, String> fileDescriptorUniqueIdentifier = storeFile(image, options);
		return fileDescriptorDao.save(fileDescriptor, fileDescriptorUniqueIdentifier);
	}

	protected FileDescriptor createFileDescriptor(Image image, ImageOptions options) {
		FileDescriptor fd = fileDescriptorDao.newFileDescriptor();
		fd.setFileStorageServiceProvider(getFileStorageService().getName());
		fd.setLocation(options.getImageSize().getLocation());
		fd.setMimeType(image.getMimeType());
		fd.setName(options.getImageSize().getFullTitle());
		fd.setPath(Configuration.get().getFileDescriptorAbsolutePathStrategy().generateAbsolutePath(fd, options.isGenerateUuid(), ""));
		return fd;
	}

	protected abstract KeyValue<String, String> storeFile(Image image, ImageOptions options) throws IOException;

	protected abstract FileStorageService getFileStorageService();
}
