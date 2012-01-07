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

import java.io.InputStream;
import java.io.Serializable;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.asynchronous.SchedulerServices;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;

/**
 * This class is moved to wicket-components as it tries to access wicket session to retrieve access token, which might be used by external services.
 * 
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefaultFileRepositoryFacade implements FileRepositoryFacade {

	private static final String NOT_AVAILABLE_JPG = "not_available.jpg";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FileDescriptorDao fileDescriptorDao;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	@Named("fileStorageJobService")
	private ScheduledJobService<FileStorageJobParameters> scheduledJobService;

	/**
	 * Manually injected service. This allows to check if exists {@link SchedulerServices} implementation.
	 */
	private SchedulerServices schedulerServices;

	/**
	 * Manually injected service. This allows custom injection.
	 */
	private FileStorageService fileStorageService;

	@Override
	public FileDescriptor storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer) {
		// Get file storage service provider
		String fileStorageServiceProvider = ServiceLocator.get().getServiceProviderName(FileStorageService.class);

		// Save file descriptor before going further
		fileDescriptor = fileDescriptorDao.save(fileDescriptor);

		// Create parameters for file storage
		FileStorageJobParameters fileStorageJobParameters = new FileStorageJobParameters().setFileDescriptor(fileDescriptor)
			.setInputStreamContainer(inputStreamContainer)
			.setUserEmail(securityFacade.getCurrentUserEmail())
			.setToken(getSecurityToken());

		if (getSchedulerServices() != null) {
			// Schedule file storage as there is an existing scheduler in system
			getSchedulerServices().runAsynchronous(scheduledJobService, fileStorageJobParameters);
		} else {
			// Execute synchronously, because there is no scheduler implementation in system
			scheduledJobService.execute(fileStorageJobParameters, false);
		}
		return fileDescriptor;
	}

	private Serializable getSecurityToken() {
		return Configuration.get().getOauthSecurityTokenProvider().getSecurityToken();
	}

	@Override
	public InputStream getFileByPath(String path) {
		// First retrieve file identifier
		FileDescriptor fileDescriptor = fileDescriptorDao.getFileDescriptorByPath(path);
		if (fileDescriptor == null) {
			return null;
		}
		// and then load file stream by unique identifier

		InputStream result = getFileStorageService(fileDescriptor.getFileStorageServiceProvider()).getInputStreamByIdentifier(
			fileDescriptor.getIdentifier());
		if (result == null) {
			// Load default picture with "Not available" information
			result = getNotAvailable();
		}
		return result;
	}

	private InputStream getNotAvailable() {
		return this.getClass().getResourceAsStream(NOT_AVAILABLE_JPG);
	}

	@Override
	public void delete(FileDescriptor fileDescriptor) {
		deleteFile(fileDescriptor, true);
	}

	@Override
	public boolean existsFile(String path) {
		return getFileDescriptorByPath(path) != null;
	}

	@Override
	public FileDescriptor newFileDescriptor() {
		return fileDescriptorDao.newFileDescriptor();
	}

	@Override
	public FileDescriptor getFileDescriptorByPath(String path) {
		if (StringUtils.isEmpty(path)) {
			return null;
		}
		return fileDescriptorDao.getFileDescriptorByPath(path);
	}

	private SchedulerServices getSchedulerServices() {
		if (schedulerServices == null) {
			schedulerServices = ServiceLocator.get().getInstance(SchedulerServices.class);
		}
		return schedulerServices;
	}

	private FileStorageService getFileStorageService(String fileStorageServiceProvider) {
		if (fileStorageService == null) {
			if (StringUtils.isEmpty(fileStorageServiceProvider)) {
				fileStorageService = ServiceLocator.get().getInstance(FileStorageService.class);
			} else {
				fileStorageService = ServiceLocator.get().getInstance(FileStorageService.class, fileStorageServiceProvider);
			}
		}
		return fileStorageService;
	}

	@Override
	public boolean deleteByPath(String path) {
		FileDescriptor fileDescriptor = fileDescriptorDao.getFileDescriptorByPath(path);
		if (fileDescriptor != null) {
			return getFileStorageService(fileDescriptor.getFileStorageServiceProvider()).delete(fileDescriptor.getIdentifier());
		}
		return false;
	}

	@Override
	public void deleteFile(FileDescriptor fileDescriptor) {
		deleteFile(fileDescriptor, false);
	}

	private void deleteFile(FileDescriptor fileDescriptor, boolean deleteFileDescriptor) {
		if (fileDescriptor == null) {
			return;
		}
		if ((StringUtils.isEmpty(fileDescriptor.getIdentifier()) || getFileStorageService(fileDescriptor.getFileStorageServiceProvider()).delete(
			fileDescriptor.getIdentifier())) &&
			deleteFileDescriptor) {
			fileDescriptorDao.delete(fileDescriptor);
		}
	}
}
