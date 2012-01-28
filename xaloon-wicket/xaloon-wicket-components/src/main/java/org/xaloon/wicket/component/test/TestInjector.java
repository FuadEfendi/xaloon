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
package org.xaloon.wicket.component.test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.Component;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.protocol.http.WebApplication;


/**
 * @author vytautas r.
 */
public class TestInjector extends org.apache.wicket.injection.Injector implements org.apache.wicket.application.IComponentInstantiationListener {

	protected IFieldValueFactory factory = null;

	private Map<String, Object> mockedServices = new HashMap<String, Object>();

	/**
	 * Construct.
	 * 
	 * @param webapp
	 */
	public TestInjector(WebApplication webapp) {
		bind(webapp);
		factory = new IFieldValueFactory() {

			@Override
			public Object getFieldValue(Field field, Object fieldOwner) {
				return getCachedProxy(field);
			}

			@Override
			public boolean supportsField(Field field) {
				return field.isAnnotationPresent(Inject.class);
			}
		};
	}


	@Override
	public void onInstantiation(Component component) {
		inject(component);
	}

	@Override
	public void inject(Object object) {
		inject(object, factory);
	}

	protected Object getCachedProxy(Field field) {

		if (field.isAnnotationPresent(Inject.class)) {
			String name = null;
			if (field.isAnnotationPresent(Named.class)) {
				name = field.getAnnotation(Named.class).value();
			}
			if (name == null) {
				name = field.getType().getName();
			}
			Object item = mockedServices.get(name);
			if (item == null) {
				throw new IllegalArgumentException("Mocking interface not found: " + name);
			}
			return item;

		}
		return null;
	}

	/**
	 * Sets mockedServices.
	 * 
	 * @param mockedServices
	 *            mockedServices
	 * @return
	 */
	public TestInjector setMockedServices(Map<String, Object> mockedServices) {
		this.mockedServices = mockedServices;
		return this;
	}
}
