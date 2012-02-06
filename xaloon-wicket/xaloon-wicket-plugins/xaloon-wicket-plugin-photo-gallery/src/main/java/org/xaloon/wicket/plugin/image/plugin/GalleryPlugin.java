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

import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import org.xaloon.core.api.plugin.AbstractPlugin;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.SecurityRole;
import org.xaloon.core.impl.plugin.category.DefaultPluginCategories;
import org.xaloon.core.impl.security.DefaultSecurityEntity;
import org.xaloon.core.impl.security.DefaultSecurityRole;
import org.xaloon.wicket.plugin.system.SystemPlugin;

/**
 * @author vytautas r.
 */
@Named("galleryPlugin")
public class GalleryPlugin extends AbstractPlugin<GalleryPluginBean> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public GalleryPlugin() {
		setCategory(DefaultPluginCategories.PRODUCTS);
	}
	
	@Override
	public List<SecurityRole> getSupportedRoles() {
		SecurityRole role = new DefaultSecurityRole(GallerySecurityAuthorities.ROLE_GALLERY_USER);
		role.getAuthorities().add(SystemPlugin.AUTHENTICATED_USER);
		role.getAuthorities().addAll(getSupportedAuthorities());
		return Arrays.asList(role);
	}
	
	@Override
	public List<Authority> getSupportedAuthorities() {
		return Arrays.asList(new Authority[]{new DefaultSecurityEntity(GallerySecurityAuthorities.IMAGE_EDIT), new DefaultSecurityEntity(GallerySecurityAuthorities.IMAGE_DELETE)});
	}
}
