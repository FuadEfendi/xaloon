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

import java.io.Serializable;

import org.xaloon.core.api.plugin.Plugin;


/**
 * Resource repository listener will be called when resource will be stored into repository.
 * 
 * @author vytautas r.
 */
public interface ResourceRepositoryListener extends Serializable {

	/**
	 * Method is called before saving property into repository
	 * 
	 * @param plugin
	 *            plugin which contains modified property
	 * @param propertyKey
	 *            property key which will store value
	 * @param value
	 *            value which is modified
	 */
	void onBeforeSaveProperty(Plugin plugin, String propertyKey, Object value);

	/**
	 * Method is called after saving property into repository
	 * 
	 * @param plugin
	 *            plugin which contains modified property
	 * @param propertyKey
	 *            property key which will store value
	 */
	void onAfterSaveProperty(Plugin plugin, String propertyKey);

}
