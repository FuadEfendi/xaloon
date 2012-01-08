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

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.xaloon.core.api.annotation.AnnotatedMatcher;
import org.xaloon.core.api.inject.ServiceLocator;
import org.xaloon.core.api.path.DelimiterEnum;

/**
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */
class AnnotatedMountScanner implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getPatternForPackage(String packageName) {
		if (packageName == null) {
			packageName = DelimiterEnum.EMPTY.value();
		}
		packageName = packageName.replace('.', '/');
		if (!packageName.endsWith(DelimiterEnum.SLASH.value())) {
			packageName += '/';
		}

		return "classpath*:" + packageName + "**/*.class";
	}

	public List<Class<?>> scanPackage(String packageName, Class<? extends Annotation> annotation) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for (Class<?> mount : getPackagePatternMatches(getPatternForPackage(packageName), annotation)) {
			Annotation foundAnnotation = mount.getAnnotation(annotation);
			if (foundAnnotation != null) {
				result.add(mount);
			}
		}
		return result;
	}

	private List<Class<?>> getPackagePatternMatches(String pattern, Class<? extends Annotation> annotation) {
		AnnotatedMatcher annotatedMatcher = getAnnotatedMatcher();
		if (annotatedMatcher == null) {
			throw new RuntimeException("Could not find org.xaloon.wicket.component.mount.AnnotatedMatcher implementation!");
		}
		return annotatedMatcher.getAnnotatedMatches(pattern, annotation);
	}

	protected AnnotatedMatcher getAnnotatedMatcher() {
		return ServiceLocator.get().getInstance(AnnotatedMatcher.class);
	}
}