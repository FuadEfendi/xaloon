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

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.util.HtmlElementEnum;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;

/**
 * @author vytautas r.
 */
@Named("fileDescriptorDao")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaFiledescriptorDao implements FileDescriptorDao {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("persistenceServices")
	private PersistenceServices persistenceServices;

	@Override
	public FileDescriptor save(FileDescriptor fileDescriptor) {
		if (fileDescriptor == null) {
			throw new IllegalArgumentException("fileDescriptor is null");
		}
		return persistenceServices.createOrEdit(fileDescriptor);
	}

	@Override
	public FileDescriptor save(FileDescriptor fileDescriptor, KeyValue<String, String> fileIdentifier) {
		if (fileDescriptor == null) {
			throw new IllegalArgumentException("fileDescriptor is null");
		}
		if (fileIdentifier == null || StringUtils.isEmpty(fileIdentifier.getKey()) || StringUtils.isEmpty(fileIdentifier.getValue())) {
			throw new IllegalArgumentException("fileIdentifier is not properly populated");
		}
		fileDescriptor.setIdentifier(fileIdentifier.getValue());
		String path = fileIdentifier.getKey();
		if (!StringUtils.isEmpty(path) && path.startsWith(HtmlElementEnum.PROTOCOL_HTTP.value())) {
			fileDescriptor.setPath(path);
		}
		return persistenceServices.createOrEdit(fileDescriptor);
	}

	@Override
	public FileDescriptor getFileDescriptorByPath(String path) {
		QueryBuilder queryBuilder = new QueryBuilder("select jfd from " + JpaFileDescriptor.class.getSimpleName() + " jfd ");
		queryBuilder.addParameter("jfd.path", "ABSOLUTE_PATH", path);

		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public FileDescriptor newFileDescriptor() {
		return new JpaFileDescriptor();
	}

	@Override
	public void delete(FileDescriptor fileDescriptor) {
		if (fileDescriptor == null) {
			return;
		}
		persistenceServices.remove(JpaFileDescriptor.class, fileDescriptor.getId());
	}

	private FileDescriptor getFileDescriptorByIdentifier(String uniqueIdentifier) {
		QueryBuilder queryBuilder = new QueryBuilder("select jfd from " + JpaFileDescriptor.class.getSimpleName() + " jfd ");
		queryBuilder.addParameter("jfd.identifier", "IDENTIFIER", uniqueIdentifier);

		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public FileDescriptor getFileDescriptorById(Long id) {
		return persistenceServices.find(JpaFileDescriptor.class, id);
	}
}
