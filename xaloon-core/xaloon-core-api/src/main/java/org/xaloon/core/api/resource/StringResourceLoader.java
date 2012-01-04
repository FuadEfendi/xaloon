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
package org.xaloon.core.api.resource;

import java.io.Serializable;
import java.util.Locale;

/**
 * Resource loader for plugins. It might have different implementations. Default implementation is based on Wicket string resource loader.
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */

public interface StringResourceLoader extends Serializable {
	/**
	 * Returns string value for selected class with provided key
	 * 
	 * @param resourceClass
	 * 			The class to get the string resource for
	 * @param key
	 * 			The key should be a String containing a lookup key into a resource bundle
	 * @return	The string resource value or null if the resource could not be loaded by this loader
	 */
	String getString(Class<?> resourceClass, String key);
	
	/**
	 * Get the string resource for the given combination of component class, resource key, locale
	 * and style. The component class provided is used to allow implementation of component specific
	 * resource loading (e.g. per page or per reusable component). The key should be a String
	 * containing a lookup key into a resource bundle. The locale should contain the locale of the
	 * current operation so that the appropriate set of resources can be selected. The style allows
	 * the set of resources to select to be varied by skin/brand.
	 * 
	 * @param resourceClass
	 * 			The class to get the string resource for
	 * @param key
	 * 			The key should be a String containing a lookup key into a resource bundle
	 * @param locale
	 * 			The locale should contain the locale of the current operation so that the
	 *          appropriate set of resources can be selected
	 * @param style
	 * 			The style identifying the resource set to select the strings from (useful for wicket implementation)
	 * @param variation
	 * 			The components variation (of the style).  Useful for wicket implementation.
	 * @return	The string resource value or null if the resource could not be loaded by this loader
	 */
	String getString(Class<?> resourceClass, String key, Locale locale, String style, String variation);
}
