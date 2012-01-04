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
package org.xaloon.core.jcr;

import java.io.Serializable;

import javax.inject.Named;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.xaloon.core.jcr.storage.util.ContentCredentials;
import org.xaloon.core.jcr.storage.util.ContentHelper;

/**
 * Repository manager is used to create repository on startup and shutdown it when application stops.
 * 
 * @author vytautas r.
 */
@Named("repositoryFacade")
public class RepositoryFacade implements Serializable {
	private static final long serialVersionUID = 1L;

	/** stored username and password for repository */
	private ContentCredentials contentCredentials;

	/** JCR repository */
	private transient Repository repository;

	private transient ThreadLocalSessionFactory threadLocalSessionFactory;

	public ThreadLocalSessionFactory getContentSessionFactory() {
		if (threadLocalSessionFactory == null) {
			init();
		}
		return threadLocalSessionFactory;
	}

	/**
	 * Shutdown JCR repository when application terminates
	 */
	public void shutdown() {
		if ((repository != null) && (repository instanceof RepositoryImpl)) {
			((RepositoryImpl)repository).shutdown();
		}
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

	public Repository getRespository() {
		if (repository == null) {
			init();
		}
		return repository;
	}

	public void setContentSessionFactory(ThreadLocalSessionFactory threadLocalSessionFactory) {
		this.threadLocalSessionFactory = threadLocalSessionFactory;
	}

	public void init() {
		if (contentCredentials == null) {
			throw new RuntimeException("Please, use setContentProperties(...) before repository init");
		}
		setRepository(ContentHelper.createRepository(contentCredentials.getJcrRepository()));
		setContentSessionFactory(ThreadLocalSessionFactory.createSessionFactory(getRespository(), contentCredentials.getJcrUsername(),
			contentCredentials.getJcrPassword()));

	}

	public Session getDefaultSession() {
		return getContentSessionFactory().getDefaultSession();
	}

	public void setContentProperties(ContentCredentials contentCredentials) {
		this.contentCredentials = contentCredentials;
	}
}
