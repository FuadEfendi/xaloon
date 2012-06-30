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
package org.xaloon.core.api.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

/**
 * File system file as input stream
 * 
 * @author vytautas.r
 */
public class FilePathInputStreamContainer extends AbstractInputStreamContainer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final String absoluteFilePath;

	/**
	 * Construct.
	 * 
	 * @param absoluteFilePath
	 * 
	 * @param options
	 */
	public FilePathInputStreamContainer(String absoluteFilePath) {
		super(new InputStreamContainerOptions());
		this.absoluteFilePath = absoluteFilePath;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		if (StringUtils.isEmpty(absoluteFilePath)) {
			throw new IllegalArgumentException("file path was not provided.");
		}
		File file = new File(absoluteFilePath);
		if (!file.exists()) {
			throw new FileNotFoundException(String.format("File does not exist: %s", absoluteFilePath));
		}
		return new FileInputStream(file);
	}

	@Override
	public void close() {
		FileUtils.deleteQuietly(new File(absoluteFilePath));
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

}
