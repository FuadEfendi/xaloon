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
package org.xaloon.wicket.component.mount.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xaloon.core.api.plugin.EmptyPlugin;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.wicket.component.mount.MountScanner;


/**
 * <p>
 * <code>MountPageGroup</code> makes a group of {@link MountPage} It is useful when aggregating dynamic menu items into group.
 * </p>
 * <p>
 * Use in combination with {@link MountScanner}
 * </p>
 * 
 * @see MountScanner
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MountPageGroup {
	/**
	 * Default ordering of page groups if not provided
	 */
	static final int DEFAULT_ORDER = 1;

	/**
	 * Context name which will be used as a group of menu items
	 * 
	 * @return string format of context name. The same name will aggregate menu items into single context.
	 */
	String value();

	/**
	 * Page group should belong to some plugin in order to manage page groups, for example to hide pages if plugin is disabled
	 * 
	 * @return plugin class which page group belongs to
	 */
	Class<? extends Plugin> plugin() default EmptyPlugin.class;

	/**
	 * Provides order functionality of WebPage in dynamic menu.
	 * 
	 * @return when order > 1 then it will be sorted according other WebPage
	 */
	int order() default DEFAULT_ORDER;
}
