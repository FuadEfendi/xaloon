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

import java.io.Serializable;

/**
 * Abstract java bean which should contain configuration properties for concrete plugin. Every java bean should extend this class in order to be used
 * as configuration parameter container.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */

public class AbstractPluginBean implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * Any additional rules to check before setting plugin visibility. If plugin is not valid then it will not be visible by default.
	 * 
	 * @return additional rules to check for plugin validity.
	 */
	public boolean isValid() {
		return true;
	}
}
