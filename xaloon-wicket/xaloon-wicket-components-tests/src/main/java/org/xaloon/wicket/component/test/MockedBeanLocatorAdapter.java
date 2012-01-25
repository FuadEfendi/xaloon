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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xaloon.core.api.inject.BeanLocatorAdapter;

/**
 * @author vytautas r.
 */
public class MockedBeanLocatorAdapter implements BeanLocatorAdapter {

	private Map<String, Object> mockedServices = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getBean(String beanName, Class<T> clazz) {
		System.out.println("Looking for: " + clazz.getName());
		T item = (T)mockedServices.get(clazz.getName());
		if (item == null) {
			throw new IllegalArgumentException("Mocking interface not found: " + clazz.getName());
		}
		return item;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getBeans(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		T item = (T)mockedServices.get(clazz.getName());
		if (item == null) {
			throw new IllegalArgumentException("Mocking interface not found: " + clazz.getName());
		}
		result.add(item);
		return result;
	}

	/**
	 * Sets mockedServices.
	 * 
	 * @param mockedServices
	 *            mockedServices
	 * @return
	 */
	public MockedBeanLocatorAdapter setMockedServices(Map<String, Object> mockedServices) {
		this.mockedServices = mockedServices;
		return this;
	}
}
