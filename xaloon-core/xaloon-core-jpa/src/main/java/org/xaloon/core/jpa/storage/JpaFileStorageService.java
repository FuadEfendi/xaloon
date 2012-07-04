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
package org.xaloon.core.jpa.storage;

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.cache.Cache;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.core.jpa.storage.model.JpaFileStorage;

/**
 * @author vytautas r.
 */
@Named(FileStorageService.FILE_STORAGE_SERVICE_JPA)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaFileStorageService implements FileStorageService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JpaFileStorageService.class);

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	private Cache fileRepositoryCache;

	private boolean configRead;

	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer) {
		return storeFile(fileDescriptor, inputStreamContainer, new HashMap<String, Object>());
	}

	@Override
	public byte[] getByteArrayByIdentifier(String identifier) {
		if (StringUtils.isEmpty(identifier)) {
			return null;// TODO or return default empty image?
		}
		byte[] result = getFromCache(identifier);
		if (result != null) {
			return result;
		}
		JpaFileStorage jfs = persistenceServices.find(JpaFileStorage.class, Long.valueOf(identifier));// TODO fix save convert
		if (jfs == null || jfs.getFile() == null) {
			return null;
		}
		result = jfs.getFile();
		storeToCache(identifier, result);
		return result;
	}

	@Override
	public boolean delete(String uniqueIdentifier) {
		if (StringUtils.isEmpty(uniqueIdentifier) || !StringUtils.isNumeric(uniqueIdentifier)) {
			return false;
		}
		JpaFileStorage jfs = persistenceServices.find(JpaFileStorage.class, Long.valueOf(uniqueIdentifier));// TODO fix save convert
		if (jfs == null) {
			return false;
		}
		persistenceServices.remove(JpaFileStorage.class, Long.valueOf(uniqueIdentifier));
		return true;
	}

	private boolean isCacheUsed() {
		if (configRead) {
			return fileRepositoryCache != null;
		}
		fileRepositoryCache = Configuration.get().getFileRepositoryCache();
		configRead = true;
		return fileRepositoryCache != null;
	}

	private byte[] getFromCache(String key) {
		if (isCacheUsed()) {
			return fileRepositoryCache.readFromCache(key);
		}
		return null;
	}

	private void storeToCache(String key, byte[] value) {
		if (isCacheUsed()) {
			fileRepositoryCache.storeToCache(key, value);
		}
	}

	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer,
		Map<String, Object> additionalProperties) {
		InputStream in = null;
		try {
			in = inputStreamContainer.getInputStream();
			JpaFileStorage jpaFileStorage = new JpaFileStorage();
			jpaFileStorage.setFile(IOUtils.toByteArray(in));
			jpaFileStorage = persistenceServices.create(jpaFileStorage);
			String id = String.valueOf(jpaFileStorage.getId());
			String path = Configuration.get().getFileDescriptorAbsolutePathStrategy().generateAbsolutePath(fileDescriptor, true, "");
			return new DefaultKeyValue<String, String>(path, id);
		} catch (IOException e) {
			LOGGER.error("Could not convert input into byte array", e);
		} finally {
			// inputStreamContainer.close();
			if (in != null) {
				IOUtils.closeQuietly(in);
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return FileStorageService.FILE_STORAGE_SERVICE_JPA;
	}

}
