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
package org.xaloon.wicket.plugin.menu;

import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * Property bean holds {@link DynamicMenuPlugin} customizable properties
 * 
 * @author vytautas r.
 */
public class DynamicMenuPluginBean extends AbstractPluginBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean hierarchyMenu = true;

	/**
	 * Method is used to decide what menu type should be used: flat or hierarchy
	 * 
	 * @return true if hierarchical menu is used
	 */
	public boolean isHierarchyMenu() {
		return hierarchyMenu;
	}

	/**
	 * Sets property what hind of menu should be used from plugin perspective - hierarchical or flat
	 * 
	 * @param hierarchyMenu
	 *            true if hierarchical menu should be used, otherwise - false
	 */
	public void setHierarchyMenu(boolean hierarchyMenu) {
		this.hierarchyMenu = hierarchyMenu;
	}
}
