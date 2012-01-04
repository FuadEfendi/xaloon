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
package org.xaloon.core.api.plugin.resource;

import org.xaloon.core.api.config.ListenerCollection;
import org.xaloon.core.api.plugin.Plugin;

/**
 * Collection, containing list of {@link ResourceRepositoryListener} listeners. These listeners will be executed every time Resource value is stored
 * into repository.
 * <p>
 * Implementation of {@link PluginResourceRepository} interface is responsible to call this collection
 * 
 * @author vytautas r.
 */
public class ResourceRepositoryListenerCollection extends ListenerCollection<ResourceRepositoryListener> implements ResourceRepositoryListener {
	private static final long serialVersionUID = 1L;

	public void onBeforeSaveProperty(final Plugin plugin, final String propertyKey, final Object value) {
		notify(new Notifier<ResourceRepositoryListener>() {
			public void notify(ResourceRepositoryListener listener) {
				listener.onBeforeSaveProperty(plugin, propertyKey, value);
			}
		});
	}

	public void onAfterSaveProperty(final Plugin plugin, final String propertyKey) {
		notify(new Notifier<ResourceRepositoryListener>() {
			public void notify(ResourceRepositoryListener listener) {
				listener.onAfterSaveProperty(plugin, propertyKey);
			}
		});
	}
}
