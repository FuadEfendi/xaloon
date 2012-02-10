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

import java.io.IOException;
import java.io.InputStream;

/**
 * @author vytautas r.
 */
public class DefaultInputStreamContainer extends AbstractInputStreamContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private InputStream is;

	/**
	 * Construct.
	 */
	public DefaultInputStreamContainer() {
		this(null);
	}

	/**
	 * Construct.
	 * 
	 * @param is
	 */
	public DefaultInputStreamContainer(InputStream is) {
		this(is, new InputStreamContainerOptions());
	}

	/**
	 * Construct.
	 * 
	 * @param is
	 * @param options
	 */
	public DefaultInputStreamContainer(InputStream is, InputStreamContainerOptions options) {
		super(options);
		this.is = is;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return is;
	}

	@Override
	public void close() {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public boolean isEmpty() {
		return is == null;
	}
}
