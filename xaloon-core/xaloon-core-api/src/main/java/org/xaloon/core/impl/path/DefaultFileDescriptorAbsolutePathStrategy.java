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
package org.xaloon.core.impl.path;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.path.AbsolutePathStrategy;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.util.UrlUtil;

/**
 * @author vytautas r.
 */
public class DefaultFileDescriptorAbsolutePathStrategy implements AbsolutePathStrategy<FileDescriptor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String generateAbsolutePath(FileDescriptor descriptor, boolean generateUuid, String prefix) {
		if (descriptor == null) {
			throw new IllegalArgumentException("File descriptor is missing in argument list.");
		}
		String name = descriptor.getName();
		String suffix = null;
		if (name.contains(DelimiterEnum.SLASH.value())) {
			name = name.substring(name.lastIndexOf(DelimiterEnum.SLASH.value()) + 1);
		}
		int suffixIndex = name.lastIndexOf(DelimiterEnum.DOT.value());
		if (suffixIndex > 0) {
			suffix = name.substring(suffixIndex + 1);
			name = name.substring(0, suffixIndex);
		}
		StringBuilder result = new StringBuilder();

		// 1. Add prefix
		if (!StringUtils.isEmpty(prefix)) {
			result.append(prefix).append(DelimiterEnum.DASH.value());
		}

		// 2. Generate unique id
		if (generateUuid) {
			result.append(UUID.randomUUID().toString()).append(DelimiterEnum.DASH.value());
		}

		// 3. Add current file name
		result.append(UrlUtil.encode(name));

		// 4. Add default suffix
		if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(suffix)) {
			result.append(DelimiterEnum.DOT.value()).append(suffix);
		}

		return result.toString();
	}

	@Override
	public String generateAbsolutePath(FileDescriptor descriptor) {
		return generateAbsolutePath(descriptor, true, null);
	}
}
