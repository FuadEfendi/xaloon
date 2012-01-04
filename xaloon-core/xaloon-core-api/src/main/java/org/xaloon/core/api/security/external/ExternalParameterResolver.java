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
package org.xaloon.core.api.security.external;

import java.io.Serializable;

import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
@Named("externalParameterResolver")
public class ExternalParameterResolver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FileRepositoryFacade fileRepository;


	/**
	 * @param initialToken
	 * @param user
	 * 
	 */
	public void resolve(AuthenticationToken initialToken, User user) {
		if (initialToken != null) {
			for (AuthenticationAttribute attribute : initialToken.getAttributes()) {
				if (AuthenticationConsumer.PARAM_FIRST_NAME.equals(attribute.getName())) {
					user.setFirstName(attribute.getValue());
				} else if (AuthenticationConsumer.PARAM_LAST_NAME.equals(attribute.getName())) {
					user.setLastName(attribute.getValue());
				} else if (AuthenticationConsumer.PARAM_EMAIL.equals(attribute.getName())) {
					user.setEmail(attribute.getValue());
				} else if (AuthenticationConsumer.PARAM_PICTURE_SMALL.equals(attribute.getName())) {
					FileDescriptor photoThumbnail = fileRepository.newFileDescriptor();
					photoThumbnail.setName(attribute.getValue());
					photoThumbnail.setPath(attribute.getValue());
					user.setPhotoThumbnail(photoThumbnail);
				}
			}
		}
	}
}
