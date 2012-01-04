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
package org.xaloon.core.api.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author vytautas r.
 */
public interface AnnotatedMatcher extends Serializable {

	/**
	 * Get all matching classes that are annotated with the given Annotation. Note that this method only returns those classes that actually contain
	 * the annotation, and does not return classes whose superclass contains the annotation.
	 * 
	 * @param pattern
	 *            the pattern to search for
	 * @param annotation
	 *            an annotation class
	 * @return List of all classes that have the given annotation. List is empty if non matches found.
	 */
	List<Class<?>> getAnnotatedMatches(String pattern, Class<? extends Annotation> annotation);

}
