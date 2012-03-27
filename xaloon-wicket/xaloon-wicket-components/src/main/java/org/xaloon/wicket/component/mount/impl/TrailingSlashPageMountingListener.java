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
import org.apache.wicket.request.IRequestMapper;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.MountedMapper;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.wicket.component.mount.AbstractPageMountScannerListener;

/**
 * @author vytautas r.
 */
public class TrailingSlashPageMountingListener extends AbstractPageMountScannerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected IRequestMapper getMountedMapper(Class<? extends IRequestablePage> pageClass) {
		String value = org.xaloon.wicket.util.UrlUtils.generateFullvalue(pageClass);
		return createRequestMapper(pageClass, value);
	}

	private IRequestMapper createRequestMapper(Class<? extends IRequestablePage> pageClass, String value) {
		String suffix = DelimiterEnum.SLASH.value();
		if (StringUtils.isEmpty(value)) {
			throw new RuntimeException("Could not mount class: " + pageClass);
		}
		if (!value.endsWith(suffix)) {
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
