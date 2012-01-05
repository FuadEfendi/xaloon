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
package org.xaloon.wicket.component.inject.j2ee;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.proxy.IProxyTargetLocator;
import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.xaloon.wicket.component.inject.j2ee.bm.WeldBeanManagerLocator;

/**
 * @author vytautas.r
 */
public class JavaWeldProxyFieldValueFactory implements IFieldValueFactory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ConcurrentHashMap<IProxyTargetLocator, Object> cache = new ConcurrentHashMap<IProxyTargetLocator, Object>();

	@Override
	public Object getFieldValue(Field field, Object fieldOwner) {
		IProxyTargetLocator locator = getProxyTargetLocator(field);
		return getCachedProxy(field, locator);
	}

	@Override
	public boolean supportsField(Field field) {
		return field.isAnnotationPresent(Inject.class);
	}

	private Object getCachedProxy(Field field, IProxyTargetLocator locator) {
		Class<?> type = field.getType();
		if (locator == null) {
			return null;
		}

		if (cache.containsKey(locator)) {
			return cache.get(locator);
		}
		if (!Modifier.isFinal(type.getModifiers())) {
			Object proxy = LazyInitProxyFactory.createProxy(type, locator);
			cache.put(locator, proxy);
			return proxy;
		} else {
			Object value = locator.locateProxyTarget();
			cache.put(locator, value);
			return value;
		}

	}

	private IProxyTargetLocator getProxyTargetLocator(Field field) {
		if (field.isAnnotationPresent(Inject.class)) {
			String name = null;
			if (field.isAnnotationPresent(Named.class)) {
				name = field.getAnnotation(Named.class).value();
			}
			return new JavaWeldBeanLocator(name, field.getType(), new WeldBeanManagerLocator());
		}
		return null;
	}
}
