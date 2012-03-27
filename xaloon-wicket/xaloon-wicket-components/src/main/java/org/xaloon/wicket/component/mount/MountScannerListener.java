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
package org.xaloon.wicket.component.mount;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Listener gets result of class list which was mounted depending on application
 * 
 * @author vytautas r.
 * @since 1.5
 */
public interface MountScannerListener extends Serializable {
	/**
	 * Method is called when classes are scanned and mounted
	 * 
	 * @param classesToMount
	 *            list of classes which are mounted
	 * 
	 */
	@Deprecated
	void onMount(List<Class<?>> classesToMount);

	/**
	 * Method is called when classes are scanned and mounted
	 * 
	 * @param mountPages
	 *            map of classes with paths which should be mounted
	 * 
	 */
	void onMount(Map<String, Class<?>> mountPages);
}
