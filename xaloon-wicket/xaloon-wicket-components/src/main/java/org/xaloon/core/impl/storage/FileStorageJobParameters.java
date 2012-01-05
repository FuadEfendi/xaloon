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

import java.io.Serializable;

import org.xaloon.core.api.asynchronous.JobParameters;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.InputStreamContainer;

/**
 * @author vytautas r.
 */
public class FileStorageJobParameters implements JobParameters {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private FileDescriptor fileDescriptor;

	private InputStreamContainer inputStreamContainer;

	private String userEmail;

	private Serializable token;

	/**
	 * Gets fileDescriptor.
	 * 
	 * @return fileDescriptor
	 */
	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}


	/**
	 * Gets userEmail.
	 * 
	 * @return userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * Gets token.
	 * 
	 * @return token
	 */
	public Serializable getToken() {
		return token;
	}

	/**
	 * Sets fileDescriptor.
	 * 
	 * @param fileDescriptor
	 *            fileDescriptor
	 * @return current instance
	 */
	public FileStorageJobParameters setFileDescriptor(FileDescriptor fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
		return this;
	}

	/**
	 * Sets userEmail.
	 * 
	 * @param userEmail
	 *            userEmail
	 * @return current instance
	 */
	public FileStorageJobParameters setUserEmail(String userEmail) {
		this.userEmail = userEmail;
		return this;
	}

	/**
	 * Sets token.
	 * 
	 * @param token
	 *            token
	 * @return current instance
	 */
	public FileStorageJobParameters setToken(Serializable token) {
		this.token = token;
		return this;
	}


	/**
	 * Gets inputStreamContainer.
	 * 
	 * @return inputStreamContainer
	 */
	public InputStreamContainer getInputStreamContainer() {
		return inputStreamContainer;
	}


	/**
	 * Sets inputStreamContainer.
	 * 
	 * @param inputStreamContainer
	 *            inputStreamContainer
	 * @return current instance
	 */
	public FileStorageJobParameters setInputStreamContainer(InputStreamContainer inputStreamContainer) {
		this.inputStreamContainer = inputStreamContainer;
		return this;
	}
}
