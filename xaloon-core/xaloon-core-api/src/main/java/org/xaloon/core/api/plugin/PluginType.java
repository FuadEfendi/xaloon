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
package org.xaloon.core.api.plugin;

/**
 * Default plugin types used to determine which logic should be used depending on plugin type. Custom plugin types might be also used.
 * 
 * @author vytautas r.
 */
public interface PluginType {

	/**
	 * Visible type means when generating dynamic menu this plugin might be involved while generating menu item
	 */
	String VISIBLE = "VISIBLE";

	/**
	 * Hidden type should be used for system plugins, which are not visible to user and might be not configurable
	 */
	String HIDDEN = "HIDDEN";
}
