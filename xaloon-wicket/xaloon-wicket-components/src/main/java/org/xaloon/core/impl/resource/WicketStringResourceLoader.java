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
package org.xaloon.core.impl.resource;

import java.util.Locale;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.resource.loader.ClassStringResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.util.TextUtil;

/**
 * Wicket implementation for {@link StringResourceLoader} interface.
 * <p>
 * Resources are loaded from wicket properties xml files depending on resource class and resource key
 * 
 * @author vytautas r.
 */
@Named("stringResourceLoader")
public class WicketStringResourceLoader implements StringResourceLoader {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(WicketStringResourceLoader.class);

	/**
	 * @see StringResourceLoader#getString(Class, String)
	 */
	public String getString(Class<?> resourceClass, String key) {
		String result = getString(resourceClass, key, WebSession.get().getLocale(), null, null);
		if (StringUtils.isEmpty(result)) {
			result = TextUtil.parseName(resourceClass.getSimpleName());
			result = result.substring(0, 1).toUpperCase() + result.substring(1);
		}
		return result;
	}

	/**
	 * @see StringResourceLoader#getString(Class, String, Locale, String, String)
	 */
	public String getString(Class<?> resourceClass, String key, Locale locale, String style, String variation) {
		String value = new ClassStringResourceLoader(resourceClass).loadStringResource(resourceClass, key, locale, style, variation);
		if (StringUtils.isEmpty(value) && logger.isWarnEnabled()) {
			logger.warn("Resource [" + key + "] could not be found for class '" + resourceClass.getName() + "'");
		}
		return value;
	}
}
