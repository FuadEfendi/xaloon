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
package org.xaloon.wicket.plugin.image.panel;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.storage.FileDescriptor;

/**
 * @author vytautas r.
 */
public class TemporaryResource extends DynamicImageResource {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(TemporaryResource.class);

	private FileDescriptor temporaryImage;

	/**
	 * Construct.
	 * 
	 * @param temporaryImage
	 */
	public TemporaryResource(FileDescriptor temporaryImage) {
		this.temporaryImage = temporaryImage;
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		if (!isEmpty()) {
			try {
				return IOUtils.toByteArray(temporaryImage.getImageInputStreamContainer().getInputStream());
			} catch (IOException e) {
				LOGGER.error("Failed loading input stream from temporary resource", e);
			}
		}
		return null;
	}

	/**
	 * @return true if image is not provided or it does not contain input stream
	 */
	public boolean isEmpty() {
		return temporaryImage == null || temporaryImage.getImageInputStreamContainer() == null;
	}

}
