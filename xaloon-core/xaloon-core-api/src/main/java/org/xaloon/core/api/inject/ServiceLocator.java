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
package org.xaloon.core.api.inject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;

import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;

/**
 * A simple service-provider loading facility using {@link BeanLocatorAdapter}
 * 
 * @author vytautas r.
 * @since 1.5
 * 
 */
public class ServiceLocator implements Serializable {

	private static final String SERVICE_LOCATOR_PROPERTIES = "/META-INF/service_locator.properties";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLocator.class);

	private static ServiceLocator instance;

	private Properties injectorProperties;

	/**
	 * Simple method to load class instance using {@link BeanLocatorAdapter} class.
	 * <p>
	 * First entry will be taken if several implementations is found.
	 * 
	 * @param <T>
	 *            class type to load
	 * @param classForInstance
	 *            class to be loaded
	 * @return instance loaded by {@link BeanLocatorAdapter}
	 */
	public <T> T getInstance(Class<T> classForInstance) {
		String beanName = getServiceProviderName(classForInstance);
		if (!StringUtils.isEmpty(beanName)) {
			// Found custom configuration, so we will load it from CDI
			return getInstance(classForInstance, beanName);
		}
		// Could not find custom configuration, try to load from service loader
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[%s] Could not find custom configuration. Loading by class instance only", classForInstance.getName()));
		}
		return getInstance(classForInstance, null);
	}

	/**
	 * Simple method to load class instance using {@link BeanLocatorAdapter} class.
	 * <p>
	 * First entry will be taken if several implementations is found.
	 * 
	 * @param <T>
	 *            class type to load
	 * @param classForInstance
	 *            class to be loaded
	 * @param customPropertyName
	 *            custom property name to lookup
	 * @return instance loaded by {@link BeanLocatorAdapter}
	 */
	public <T> T getInstanceByCustomProperty(Class<T> classForInstance, String customPropertyName) {
		String beanName = getServiceProviderName(customPropertyName);
		if (!StringUtils.isEmpty(beanName)) {
			// Found custom configuration, so we will load it from CDI
			return getInstance(classForInstance, beanName);
		}
		// Could not find custom configuration, try to load from service loader
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[%s] Could not find custom configuration. Loading by class instance only", classForInstance.getName()));
		}
		return getInstance(classForInstance, null);
	}

	/**
	 * Returns a list off all instances for provided class
	 * 
	 * @param <T>
	 *            class type to load
	 * @param classForInstance
	 *            class to be loaded
	 * @return list of instances loaded by {@link BeanLocatorAdapter}. Empty list if there are no implementations
	 */
	public <T> List<T> getInstances(Class<T> classForInstance) {
		BeanLocatorAdapter adapter = Configuration.get().getBeanLocatorAdapter();
		return adapter.getBeans(classForInstance);
	}

	/**
	 * Used in case when there are several implementations of interface. Bean name should be provided at this point to find proper implementation.
	 * 
	 * @param <T>
	 *            instance type to be returned
	 * @param clazz
	 *            bean type to load
	 * @param beanName
	 *            bean name to load
	 * @return instance loaded by dependency injector
	 */
	public <T> T getInstance(Class<T> clazz, String beanName) {
		if (clazz == null) {
			throw new IllegalArgumentException("Missing parameters!");
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[%s] Loading class instance with bean name '%s'", clazz.getName(), beanName));
		}
		BeanLocatorAdapter adapter = Configuration.get().getBeanLocatorAdapter();
		try {
			return adapter.getBean(beanName, clazz);
		} catch (Exception e) {
			LOGGER.error(String.format("[%s] Could not get bean class with name '%s'", clazz, beanName), e);
			throw new RuntimeException(e);
		}
	}

	private Properties getProperties() {
		if (injectorProperties == null) {
			injectorProperties = new Properties();
			loadServiceLocatorProperties();
		}
		return injectorProperties;
	}

	private void loadServiceLocatorProperties() {
		try {
			InputStream is = ServiceLocator.class.getResourceAsStream(SERVICE_LOCATOR_PROPERTIES);
			if (is != null) {
				injectorProperties.load(is);
			}
		} catch (IOException e) {
			LOGGER.warn(
				String.format("Property file was not found or is not correct[%s]. Custom configuration will not be used.", SERVICE_LOCATOR_PROPERTIES),
				e);
		}
	}

	/**
	 * @return singleton instance
	 */
	public static ServiceLocator get() {
		if (instance == null) {
			instance = new ServiceLocator();
		}
		return instance;
	}

	/**
	 * Tries to load bean name if it is configured in properties. <br/>
	 * Returns null if it is not found in property file
	 * 
	 * @param <T>
	 *            the type of the class modeled by this {@code Class} object.
	 * @param classForInstance
	 * @return value for {@link Named} if found in property file
	 */
	public <T> String getServiceProviderName(Class<T> classForInstance) {
		if (classForInstance == null) {
			throw new IllegalArgumentException("Argument is missing.");
		}
		String classForInstanceName = classForInstance.getName();
		return getServiceProviderName(classForInstanceName);
	}

	/**
	 * Tries to load bean name if it is configured in properties. <br/>
	 * Returns null if it is not found in property file
	 * 
	 * @param <T>
	 *            the type of the class modeled by this {@code Class} object.
	 * @param customPropertyName
	 * @return value for {@link Named} if found in property file
	 */
	public <T> String getServiceProviderName(String customPropertyName) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("[%s] Looking for bean name", customPropertyName));
		}
		return getProperties().getProperty(customPropertyName);
	}
}
