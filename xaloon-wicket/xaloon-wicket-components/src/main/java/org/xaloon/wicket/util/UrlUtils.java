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
package org.xaloon.wicket.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.RequestUtils;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.apache.wicket.util.crypt.Base64;
import org.xaloon.core.api.path.DelimiterEnum;
import org.xaloon.core.api.util.HtmlElementEnum;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.wicket.component.mount.annotation.MountPage;
import org.xaloon.wicket.component.mount.annotation.MountPageGroup;


/**
 * Wicket url utils.
 * <p>
 * Generates full path for provided page, which has {@link MountPage} annotation.
 * <p>
 * Encodes and decodes provided string into/from Base64
 * 
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */
public class UrlUtils {
	/**
	 * Generates full path for provided page class
	 * 
	 * @param pageClass
	 *            page class which has {@link MountPage} annotation
	 * @return string representation of full path
	 */
	public static String generateFullvalue(Class<?> pageClass) {
		if (pageClass == null) {
			return null;
		}
		MountPage mountPage = pageClass.getAnnotation(MountPage.class);
		if (mountPage == null) {
			return null;
		}
		String context = generateContext(pageClass);
		String fullvalue = UrlUtil.mergeIntoUrl(context, mountPage.value());
		return fullvalue;
	}

	/**
	 * Takes {@link MountPageGroup} annotation from hierarchy and merges into one url path
	 * 
	 * @param classForAnnotation
	 * @return merged page group url
	 */
	public static String generateContext(Class<?> classForAnnotation) {
		if (classForAnnotation == null || classForAnnotation.getName().equals(WebPage.class.getName())) {
			return null;
		}
		MountPageGroup result = classForAnnotation.getAnnotation(MountPageGroup.class);
		if (result == null) {
			return generateContext(classForAnnotation.getSuperclass());
		}
		String context = generateContext(classForAnnotation.getSuperclass());
		return (!StringUtils.isEmpty(context)) ? (UrlUtil.mergeIntoUrl(context, result.value())) : result.value();
	}

	/**
	 * Encodes provided string into base64
	 * 
	 * @param value
	 * @param preffix
	 * @return encoded string
	 */
	public static String encodeBase64(String value, String preffix) {
		String result = new String(org.apache.wicket.util.crypt.Base64.encodeBase64(value.getBytes()));
		return result + preffix;
	}

	/**
	 * Decodes provided string from base64
	 * 
	 * @param value
	 * @param preffix
	 * @return decoded string from base64
	 */
	public static String decodeBase64(String value, String preffix) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}
		if (StringUtils.isNotEmpty(preffix) && value.contains(preffix)) {
			value = value.substring(0, value.indexOf(preffix));
		}
		byte[] bytes = value.getBytes();
		if (!Base64.isArrayByteBase64(bytes)) {
			return value;
		}
		return new String(Base64.decodeBase64(bytes));
	}

	/**
	 * @param pageParams
	 * @return string representation of provided url
	 */
	public static String generateFullPathFromParams(PageParameters pageParams) {
		StringBuilder result = new StringBuilder();
		if (pageParams.getIndexedCount() > 0) {
			for (int i = 0; i < pageParams.getIndexedCount(); i++) {
				result.append(DelimiterEnum.SLASH.value());
				result.append(pageParams.get(i).toString());
			}
		}
		return result.toString();
	}

	/**
	 * @param pageClass
	 * @param params
	 * 
	 * @return absolute link of page with parameters
	 */
	public static String toAbsolutePath(Class<? extends Page> pageClass, PageParameters params) {
		HttpServletRequest req = (HttpServletRequest)RequestCycle.get().getRequest().getContainerRequest();
		StringBuilder domainPath = new StringBuilder(StringUtils.removeEnd(req.getRequestURL().toString(), req.getServletPath()));
		domainPath.append(DelimiterEnum.SLASH.value());
		return RequestUtils.toAbsolutePath(domainPath.toString(), RequestCycle.get().mapUrlFor(pageClass, params).toString());
	}

	/**
	 * @param sharedResource
	 * @param absoluteImagePath
	 * @return absolute image path
	 */
	public static String toAbsoluteImagePath(String sharedResource, String absoluteImagePath) {
		String url;
		if (absoluteImagePath.startsWith(HtmlElementEnum.PROTOCOL_HTTP.value())) {
			url = absoluteImagePath;
		} else {
			ResourceReference imageResource = new SharedResourceReference(sharedResource);
			PageParameters params = new PageParameters();
			params.set(0, absoluteImagePath);
			HttpServletRequest req = (HttpServletRequest)RequestCycle.get().getRequest().getContainerRequest();
			url = RequestCycle.get().getUrlRenderer().renderFullUrl(RequestCycle.get().mapUrlFor(imageResource, params));
		}
		return url;
	}
}
