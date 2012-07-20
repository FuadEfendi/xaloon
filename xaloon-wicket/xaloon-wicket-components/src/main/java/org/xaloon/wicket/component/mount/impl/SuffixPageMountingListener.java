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

import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.core.request.mapper.MountedMapper;
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.component.IRequestablePage;
import org.xaloon.wicket.component.mount.AbstractPageMountScannerListener;

/**
 * @author vytautas r.
 */
public class SuffixPageMountingListener extends AbstractPageMountScannerListener {

	private static final String PARAMETER_VALUE = "/$";
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String suffix;

	/**
	 * Construct.
	 * 
	 * @param suffix
	 */
	public SuffixPageMountingListener(String suffix) {
		this.suffix = suffix;
	}

	@Override
	protected IRequestMapper getMountedMapper(Class<? extends IRequestablePage> pageClass) {
		String value = org.xaloon.wicket.util.UrlUtils.generateFullvalue(pageClass);
		return createRequestMapper(pageClass, value);
	}

	private IRequestMapper createRequestMapper(Class<? extends IRequestablePage> pageClass, String value) {
		if (StringUtils.isEmpty(value)) {
			throw new RuntimeException("Could not mount class: " + pageClass);
		}
		if (value.contains(PARAMETER_VALUE)) {
			StringBuilder valueBuilder = new StringBuilder(value.substring(0, value.indexOf(PARAMETER_VALUE)));
			valueBuilder.append(suffix);
			valueBuilder.append(value.substring(value.indexOf(PARAMETER_VALUE)));
			value = valueBuilder.toString();
		} else {
			value = value + suffix;
		}
		return new MountedMapper(value, pageClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected IRequestMapper getMountedMapper(Entry<String, Class<?>> item) {
		return createRequestMapper((Class<? extends IRequestablePage>)item.getValue(), item.getKey());
	}
}
