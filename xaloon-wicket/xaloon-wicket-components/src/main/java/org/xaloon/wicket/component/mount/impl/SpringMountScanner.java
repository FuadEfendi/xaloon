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
package org.xaloon.wicket.component.mount.impl;

import java.util.List;

import javax.inject.Named;

import org.apache.wicket.protocol.http.WebApplication;
import org.xaloon.wicket.component.mount.MountScanner;
import org.xaloon.wicket.component.mount.annotation.MountPage;

/**
 * Implementation class for {@link MountScanner} uses spring package scanner to scan packages for required annotation
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */
@Named("springMountScanner")
public class SpringMountScanner extends MountScanner<WebApplication> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AnnotatedMountScanner annotatedMountScanner = new AnnotatedMountScanner();

	@Override
	public List<Class<?>> mountPackage(WebApplication application, String packageName) {
		// Scan for web pages
		List<Class<?>> result = annotatedMountScanner.scanPackage(packageName, MountPage.class);

		// Notify waiting listeners
		getMountScannerListenerCollection().onMount(result);
		return result;
	}
}
