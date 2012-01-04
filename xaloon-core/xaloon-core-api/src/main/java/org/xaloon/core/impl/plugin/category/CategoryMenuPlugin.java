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
package org.xaloon.core.impl.plugin.category;

import javax.inject.Named;

import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.PluginType;
import org.xaloon.core.impl.plugin.tree.AbstractTreePlugin;
import org.xaloon.core.impl.plugin.tree.MenuItem;

/**
 * Category plugin which contains category tree in memory. Category tree is constructed while {@link CategoryMenuPluginRegistryListener} is invoked.
 * It should be created on application startup and do not change in runtime. This plugin will be used in administration plugin to show plugin
 * hierarchy by category
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */
@Named
public class CategoryMenuPlugin extends AbstractTreePlugin<AbstractPluginBean, MenuItem> {
	private static final long serialVersionUID = 1L;

	/**
	 * Plugin type is set to hidden by default.
	 */
	public CategoryMenuPlugin() {
		setType(PluginType.HIDDEN);
	}
}
