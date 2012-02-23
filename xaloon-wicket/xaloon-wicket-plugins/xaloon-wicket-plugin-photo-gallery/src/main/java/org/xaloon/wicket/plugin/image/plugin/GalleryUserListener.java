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
package org.xaloon.wicket.plugin.image.plugin;

import javax.inject.Inject;

import org.apache.wicket.injection.Injector;
import org.xaloon.core.api.image.AlbumFacade;
import org.xaloon.core.api.user.UserListener;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public class GalleryUserListener implements UserListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AlbumFacade albumFacade;

	@Override
	public void onBeforeDelete(User userToBeDeleted) {
		Injector.get().inject(this);
		albumFacade.deleteImagesByUsername(userToBeDeleted);
		albumFacade.deleteAlbumsByUsername(userToBeDeleted);
	}
}
