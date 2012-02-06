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
package org.xaloon.wicket.plugin.blog;

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.xaloon.core.api.plugin.AbstractPlugin;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.impl.plugin.category.DefaultPluginCategories;
import org.xaloon.core.impl.security.DefaultSecurityEntity;
import org.xaloon.core.impl.security.DefaultSecurityRole;
import org.xaloon.wicket.plugin.blog.panel.admin.BlogPluginAdministrationPanel;
import org.xaloon.wicket.plugin.system.SystemPlugin;


/**
 * Simple blog plugin with it's properties
 * 
 * @author vytautas r.
 */
@Named
public class BlogPlugin extends AbstractPlugin<BlogPluginBean> {
	private static final long serialVersionUID = 1L;

	/**
	 * Blog category classifier
	 */
	public static final String CLASSIFIER_BLOG_CATEGORY = "BLOG_CATEGORY";

	/**
	 * Blog file classifier item
	 */
	public static final String CLASSIFIER_FILE_STORAGE_BLOG_ENTRY_THUMBNAIL = "BLOG_ENTRY_THUMBNAIL";

	/**
	 * Construct.
	 */
	public BlogPlugin() {
		setCategory(DefaultPluginCategories.PRODUCTS);
	}

	@Override
	public Class<?> getAdministratorFormClass() {
		return BlogPluginAdministrationPanel.class;
	}
	
	@Override
	public List<SecurityRole> getSupportedRoles() {
		SecurityRole role = new DefaultSecurityRole(BlogSecurityAuthorities.ROLE_BLOGGER);
		role.getAuthorities().add(SystemPlugin.AUTHENTICATED_USER);
		role.getAuthorities().addAll(getSupportedAuthorities());
		return Arrays.asList(role);
	}
	
	@Override
	public List<Authority> getSupportedAuthorities() {
		return Arrays.asList(new Authority[]{new DefaultSecurityEntity(BlogSecurityAuthorities.BLOG_CREATOR)});
	}
}
