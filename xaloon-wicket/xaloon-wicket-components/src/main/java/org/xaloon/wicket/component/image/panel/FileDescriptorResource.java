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
package org.xaloon.wicket.component.image.panel;

import javax.inject.Inject;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.resource.DynamicImageResource;
import org.xaloon.core.api.storage.ByteArrayAsInputStreamContainer;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.storage.UrlInputStreamContainer;

/**
 * @author vytautas r.
 */
public class FileDescriptorResource extends DynamicImageResource {
	private static final long serialVersionUID = 1L;

	private FileDescriptor fileDescriptor;

	@Inject
	private FileRepositoryFacade fileRepositoryFacade;

	/**
	 * Construct.
	 * 
	 * @param fileDescriptor
	 */
	public FileDescriptorResource(FileDescriptor fileDescriptor) {
		this.fileDescriptor = fileDescriptor;
		Injector.get().inject(this);
		init();
	}

	private void init() {
		if (fileDescriptor != null && fileDescriptor.getImageInputStreamContainer() == null) {
			if (fileDescriptor.isExternal()) {
				fileDescriptor.setImageInputStreamContainer(new UrlInputStreamContainer(fileDescriptor.getPath()));
			} else {
				fileDescriptor.setImageInputStreamContainer(new ByteArrayAsInputStreamContainer(
					fileRepositoryFacade.getFileByPath(fileDescriptor.getPath())));
			}
		}
	}

	@Override
	protected byte[] getImageData(Attributes attributes) {
		if (!isEmpty()) {
			return fileDescriptor.getImageInputStreamContainer().asByteArray();
		}
		return null;
	}

	/**
	 * @return true if image is not provided or it does not contain input stream
	 */
	public boolean isEmpty() {
		return fileDescriptor == null || fileDescriptor.getImageInputStreamContainer() == null;
	}
}
