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

import org.xaloon.wicket.component.mount.MountScanner;

/**
 * <p>
 * <code>MountPage</code> has several purposes when adding it to <code>org.apache.wicket.markup.html.WebPage</code>
 * </p>
 * <p>
 * <ul>
 * <li>
 * Mount page on application startup;</li>
 * <li>
 * Generate dynamic menu on application startup;</li>
 * <li>
 * Generate sitemap.xml from mounted pages.</li>
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
public @interface MountPage {
	/**
	 * Default priority of page if not provided
	 */
	static final double DEFAULT_PRIORITY = 0.7;

	/**
	 * Default order of page if not provided
	 */
	static final int DEFAULT_ORDER = 1;

	/**
	 * Default page lookup frequency if not provided
	 */
	static final String DEFAULT_FREQUENCY = "daily";

	/**
	 * Registers unique value name, which will be later used for mounting web page
	 * 
	 * @return value name for annotated WebPage
	 */
	String value();

	/**
	 * Annotation is used to exclude WebPage when generating sitemap.xml or dynamic menu
	 * 
	 * @return false if WebPage is hidden, otherwise - true
	 */
	boolean visible() default true;

	/**
	 * 
	 * Priority is used when generating sitemap.xml file. You should read sitemap.xml specification for more information.
	 * 
	 * @return double value of priority for WebPage
	 */
	double priority() default DEFAULT_PRIORITY;

	/**
	 * Provides order functionality of WebPage in dynamic menu.
	 * 
	 * @return when order > 1 then it will be sorted according other WebPage
	 */
	int order() default DEFAULT_ORDER;

	/**
	 * Frequency is used when generating sitemap.xml file. You should read sitemap.xml specification for more information.
	 * 
	 * @return string format of WebPage frequency in sitemap.xml
	 */
	String frequency() default DEFAULT_FREQUENCY;
}
