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
package org.xaloon.core.impl.plugin.tree;

import javax.inject.Inject;
import javax.inject.Named;

import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.plugin.PluginRegistryListener;
import org.xaloon.core.api.resource.StringResourceLoader;

/**
 * Base class for tree observer, which is usually used to create menu trees depending on requirements. This class prepares general initialization of
 * observer and then passes observable object to subclass.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 * 
 * @see AbstractTreePlugin
 */

public abstract class AbstractTreePluginRegistryListener implements PluginRegistryListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Resource loader for current application. It is injected via spring IoC
	 */
	@Inject
	private StringResourceLoader stringResourceLoader;

	@Inject
	@Named("pluginRegistry")
	private PluginRegistry pluginRegistry;

	/**
	 * Returns injected plugin registry instance
	 * 
	 * @return plugin registry
	 */
	public PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}

	/**
	 * Returns string resource loader which is used in current application
	 * 
	 * @return resource loader is injected via default DI. It should not be null in normal flow, otherwise try to load using {@link ServiceLocator}
	 */
	public StringResourceLoader getStringResourceLoader() {
		return stringResourceLoader;
	}
}
