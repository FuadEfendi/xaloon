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
package org.xaloon.core.jcr.storage;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Binary;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.value.BinaryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileStorageService;
import org.xaloon.core.api.storage.InputStreamContainer;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.core.jcr.RepositoryFacade;
import org.xaloon.core.jcr.storage.util.ContentHelper;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
@Named("jcrFileStorageService")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JcrFileStorageService implements FileStorageService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JcrFileStorageService.class);

	private static final String NAME = "jcrFileStorageService";
	
	@Inject
	private RepositoryFacade repositoryFacade;

	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer) {
		return storeFile(fileDescriptor, inputStreamContainer, new HashMap<String, Object>());
	}
	
	@Override
	public byte[] getByteArrayByIdentifier(String uniqueIdentifier) {
		if (!org.apache.commons.lang.StringUtils.isEmpty(uniqueIdentifier)) {
			if (uniqueIdentifier.contains("/")) {
				return getInputStreamByPath(uniqueIdentifier);
			}
			try {
				Node fileContent = repositoryFacade.getDefaultSession().getNodeByIdentifier(uniqueIdentifier);
				Binary binary = fileContent.getProperty("jcr:data").getBinary();
				return toByteArray(binary);
			} catch (Exception e) {
				throw new RuntimeException("Error while retrieving file", e);
			}
		}
		return null;
	}

	private byte[] toByteArray(Binary binary) throws RepositoryException, IOException {
		byte result[] = new byte[(int)binary.getSize()];
		binary.read(result, 0);
		return result;
	}

	private byte[] getInputStreamByPath(String uniqueIdentifier) {
		try {
			Node fileContent = repositoryFacade.getDefaultSession().getRootNode().getNode(uniqueIdentifier);
			Binary binary = fileContent.getNode("jcr:content").getProperty("jcr:data").getBinary();
			return toByteArray(binary);
		} catch (Exception e) {
			throw new JcrException("Error while retrieving file", e);
		}
	}

	@Override
	public boolean delete(String uniqueIdentifier) {
		if (!org.apache.commons.lang.StringUtils.isEmpty(uniqueIdentifier)) {
			try {
				Session session = repositoryFacade.getDefaultSession();
				Node nodeToRemove = session.getNodeByIdentifier(uniqueIdentifier);
				nodeToRemove = nodeToRemove.getParent();
				if (nodeToRemove != null) {
					nodeToRemove.remove();
					session.save();
					return true;
				}
			} catch (ItemNotFoundException e) {
				// node was not found
				return true;
			} catch (Exception e) {
				LOGGER.error("Could not delete file.", e);
			}
		}
		return false;
	}

	@Override
	public KeyValue<String, String> storeFile(FileDescriptor fileDescriptor, InputStreamContainer inputStreamContainer, Map<String, Object> additionalProperties) {
		if (StringUtils.isEmpty(fileDescriptor.getPath())) {
			return null;
		}
		try {
			Session session = repositoryFacade.getDefaultSession();
			Node rootNode = session.getRootNode();
			String[] split = new String[] { };
			if (!StringUtils.isEmpty(fileDescriptor.getLocation())) {
				split = fileDescriptor.getLocation().split("/");
			}
			Node folder = ContentHelper.findOrCreateFolder(session, split, rootNode);

			Node file = folder.addNode(fileDescriptor.getName(), "nt:file");
			Node fileContent = file.addNode("jcr:content", "nt:resource");
			fileContent.addMixin("mix:referenceable");
			if (!StringUtils.isEmpty(fileDescriptor.getMimeType())) {
				fileContent.setProperty("jcr:mimeType", fileDescriptor.getMimeType());
			}
			fileContent.setProperty("jcr:lastModified", Calendar.getInstance());
			fileContent.setProperty("jcr:data", new BinaryImpl(inputStreamContainer.getInputStream()));
			session.save();
			return new DefaultKeyValue<String, String>(fileContent.getIdentifier(), fileContent.getIdentifier());
		} catch (Exception e) {
			LOGGER.error("Error while storing file", e);
		} finally {
			inputStreamContainer.close();
		}
		return null;
	}

	@Override
	public String getName() {
		return NAME;
	}
}
