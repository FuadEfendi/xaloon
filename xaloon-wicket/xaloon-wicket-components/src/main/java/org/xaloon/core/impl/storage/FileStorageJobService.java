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
package org.xaloon.core.impl.storage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.asynchronous.RetryAction;
import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.image.ImageResizer;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.storage.DefaultInputStreamContainer;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.storage.InputStreamContainerOptions;

/**
 * @author vytautas r.
 */
@Named("fileStorageJobService")
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class FileStorageJobService implements ScheduledJobService<FileStorageJobParameters> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileStorageJobService.class);

	@Inject
	private FileDescriptorDao fileDescriptorDao;

	@Inject
	private PersistenceServices persistenceServices;

	private FileStorageService fileStorageService;

	@Inject
	@Named("imageResizer")
	private ImageResizer imageResizer;

	@Override
	public <V> V execute(FileStorageJobParameters jobParameters, boolean isScheduled) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Started: scheduleInMemory()");
		}
		try {
			// Get file storage service provider
			String fileStorageServiceProvider = ServiceLocator.get().getServiceProviderName(FileStorageService.class);
			FileStorageService fileStorageService = getFileStorageService(fileStorageServiceProvider);
			if (fileStorageService == null) {
				throw new IllegalStateException(String.format("File storage service was not found with bean name: %s", fileStorageServiceProvider));
			}

			// Reload file descriptor
			FileDescriptor fileDescriptor = getFileDescriptor(jobParameters.getFileDescriptor(), isScheduled);
			System.out.println("DESCRIPTOR: " + fileDescriptor);
			if (fileDescriptor == null) {
				LOGGER.error(String.format("Could not store file into repository, because file descriptor was not found: %s",
					jobParameters.getFileDescriptor().getName()));
				return null;
			}
			// Load parameters
			InputStreamContainer inputStreamContainer = resizeIfRequired(jobParameters.getInputStreamContainer());
			String userEmail = jobParameters.getUserEmail();

			Map<String, Object> additionalProperties = new HashMap<String, Object>();
			additionalProperties.put(FileStorageService.PARAMETER_USER_EMAIL, userEmail);
			additionalProperties.put(FileStorageService.PARAMETER_USER_TOKEN, jobParameters.getToken());

			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Start storing file into repository: %s", fileDescriptor.getName()));
			}
			// store file into physical location and get it's unique id
			KeyValue<String, String> uniqueIdentifier = fileStorageService.storeFile(fileDescriptor, inputStreamContainer, additionalProperties);

			// Update file descriptor
			fileDescriptor.setFileStorageServiceProvider(fileStorageServiceProvider);
			fileDescriptorDao.save(fileDescriptor, uniqueIdentifier);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info(String.format("Storage complete for file: %s", fileDescriptor.getName()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Exception while executing job.", e);
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Finished: scheduleInMemory()");
		}
		return null;
	}

	private InputStreamContainer resizeIfRequired(InputStreamContainer inputStreamContainer) throws IOException {
		InputStreamContainerOptions options = inputStreamContainer.getOptions();
		if (!options.isResize()) {
			return inputStreamContainer;
		}
		InputStream is = imageResizer.resize(inputStreamContainer.getInputStream(), options.getWidth(), options.getHeight(), true);
		return new DefaultInputStreamContainer(is);
	}

	private FileDescriptor getFileDescriptor(FileDescriptor fileDescriptor, boolean isScheduled) throws InterruptedException {
		if (isScheduled) {
			return new RetryAction<FileDescriptor, FileDescriptor>(true) {
				private static final long serialVersionUID = 1L;

				@Override
				protected FileDescriptor onPerform(FileDescriptor parameters) {
					return persistenceServices.find(parameters.getClass(), parameters.getId());
				}
			}.perform(fileDescriptor);
		} else {
			return fileDescriptor;
		}
	}

	private FileStorageService getFileStorageService(String fileStorageServiceProvider) {
		if (fileStorageService == null) {
			fileStorageService = ServiceLocator.get().getInstance(FileStorageService.class, fileStorageServiceProvider);
		}
		return fileStorageService;
	}

}
