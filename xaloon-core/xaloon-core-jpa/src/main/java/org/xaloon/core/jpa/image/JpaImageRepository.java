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
package org.xaloon.core.jpa.image;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.image.AbstractImageRepository;
import org.xaloon.core.api.image.ImageOptions;
import org.xaloon.core.api.image.ImageRepository;
import org.xaloon.core.api.image.ImageResizer;
import org.xaloon.core.api.image.ImageSize;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.DefaultInputStreamContainer;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;

@Named("jpaImageRepository")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaImageRepository extends AbstractImageRepository {

	@Inject
	@Named(FileStorageService.FILE_STORAGE_SERVICE_JPA)
	private FileStorageService fileStorageService;

	@Inject
	private FileDescriptorDao fileDescriptorDao;

	@Inject
	@Named("imageResizer")
	private ImageResizer imageResizer;

	@Override
	public ImageRepository getAlternativeImageRepository() {
		// No alternative image repository
		return null;
	}

	@Override
	protected FileStorageService getFileStorageService() {
		return fileStorageService;
	}

	@Override
	protected KeyValue<String, String> storeFile(Image image, ImageOptions options) throws IOException {
		InputStreamContainer resizedInputStreamContainer = resize(options);
		return getFileStorageService().storeFile(image, resizedInputStreamContainer);
	}

	private InputStreamContainer resize(ImageOptions options) throws IOException {
		InputStreamContainer imageInputStreamContainer = options.getImageInputStreamContainer();
		ImageSize imageSize = options.getImageSize();
		InputStream is = imageResizer.resize(imageInputStreamContainer.getInputStream(), imageSize.getWidth(), imageSize.getHeight(), true);

		return new DefaultInputStreamContainer(is);
	}
}
