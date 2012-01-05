#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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

package ${package}.temp;

import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.classifier.dao.ClassifierDao;
import org.xaloon.core.api.classifier.dao.ClassifierItemDao;
import org.xaloon.core.api.security.SecurityRoles;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.jpa.classifier.model.JpaClassifier;
import org.xaloon.core.jpa.classifier.model.JpaClassifierItem;
import org.xaloon.core.jpa.user.model.JpaUser;
import org.xaloon.wicket.plugin.blog.BlogPlugin;


@Named
public class TemporaryFacadeImpl implements TemporaryFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ClassifierDao classifierDao;
	
	@Inject
	private ClassifierItemDao classifierItemDao;
	
	@Inject
	private UserFacade userFacade;
	
	/**
	 * create initial demo data
	 */
	public void initDemoData() {
		initClassifiers();
		initUserInfo();
	}

	private void initUserInfo() {
		String username = "demo";
		if (userFacade.getUserByUsername(username) == null) {
			JpaUser user = new JpaUser();
			
			user.setUsername(username);
			user.setEmail("test@test.com");
			user.setFirstName("Demo");
			user.setLastName("Demum");
			String activationKey = userFacade.registerUser(user, username, true, null);
			userFacade.activate(activationKey);
			
			userFacade.assignRole(username, SecurityRoles.AUTHENTICATED_USER);
			userFacade.assignRole(username, SecurityRoles.BLOG_CREATOR);
			userFacade.assignRole(username, SecurityRoles.SYSTEM_ADMINISTRATOR);	
			
		}
	}

	private void initClassifiers() {
		if (classifierDao.findClassifierByType(BlogPlugin.CLASSIFIER_BLOG_CATEGORY) == null ) {
			JpaClassifier cl = classifierDao.newClassifier();
			cl.setType(BlogPlugin.CLASSIFIER_BLOG_CATEGORY);
			cl.setName("Blog categories");
			cl.beforeCreate();
			classifierDao.createClassifier(cl);
		}
		
		if (classifierDao.findClassifierByType(FileRepositoryFacade.CLASSIFIER_FILE_STORAGE_CATEGORY) == null ) {
			JpaClassifier cl = classifierDao.newClassifier();
			cl.setType(FileRepositoryFacade.CLASSIFIER_FILE_STORAGE_CATEGORY);
			cl.setName("File storage categories");
			cl.beforeCreate();
			classifierDao.createClassifier(cl);
			
			JpaClassifierItem item = new JpaClassifierItem();
			item.setClassifier(cl);
			item.setCode(BlogPlugin.CLASSIFIER_FILE_STORAGE_BLOG_ENTRY_THUMBNAIL);
			item.setName("Blog thumbnails");
			classifierItemDao.createClassifierItem(item);			
		}
	}
}
