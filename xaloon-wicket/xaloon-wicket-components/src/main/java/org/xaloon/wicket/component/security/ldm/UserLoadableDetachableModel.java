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
package org.xaloon.wicket.component.security.ldm;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;

/**
 * @author vytautas r.
 */
public class UserLoadableDetachableModel extends LoadableDetachableModel<User> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserLoadableDetachableModel.class);

	@Inject
	@Named("userDao")
	private UserDao userDao;

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param username
	 */
	public UserLoadableDetachableModel() {
		Injector.get().inject(this);
	}

	@Override
	protected User load() {
		String username = securityFacade.getCurrentUsername();
		if (!StringUtils.isEmpty(username)) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Loading user for request cycle: " + username);
			}
			return userDao.getUserByUsername(username);
		}
		return null;
	}
}
