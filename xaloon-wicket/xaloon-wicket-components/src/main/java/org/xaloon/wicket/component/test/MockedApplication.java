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

import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Page;
import org.apache.wicket.injection.Injector;
import org.xaloon.core.api.annotation.AnnotatedMatcher;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.plugin.PluginLoader;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationFacade;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.impl.plugin.category.CategoryGroupPluginRegistryListener;
import org.xaloon.core.impl.plugin.category.CategoryMenuPluginRegistryListener;
import org.xaloon.wicket.application.page.LayoutComponentInitializer;
import org.xaloon.wicket.component.security.AuthenticatedWebApplication;
import org.xaloon.wicket.plugin.menu.DynamicMenuMountScannerListener;

/**
 * @author vytautas r.
 */
public class MockedApplication extends AuthenticatedWebApplication {

	private Map<String, Object> mockedServices = new HashMap<String, Object>();

	private SecurityFacade securityFacade = mock(SecurityFacade.class);

	private PluginRegistry pluginRegistry = mock(PluginRegistry.class);

	private UserFacade userFacade = mock(UserFacade.class);

	/**
	 * Construct.
	 */
	public MockedApplication() {
		DynamicMenuMountScannerListener dynamicMenuMountScannerListener = mock(DynamicMenuMountScannerListener.class);
		mockedServices.put(DynamicMenuMountScannerListener.class.getName(), dynamicMenuMountScannerListener);

		CategoryMenuPluginRegistryListener categoryMenuPluginRegistryListener = mock(CategoryMenuPluginRegistryListener.class);
		mockedServices.put(CategoryMenuPluginRegistryListener.class.getName(), categoryMenuPluginRegistryListener);

		CategoryGroupPluginRegistryListener categoryGroupPluginRegistryListener = mock(CategoryGroupPluginRegistryListener.class);
		mockedServices.put(CategoryGroupPluginRegistryListener.class.getName(), categoryGroupPluginRegistryListener);

		mockedServices.put(PluginRegistry.class.getName(), pluginRegistry);

		mockedServices.put(SecurityFacade.class.getName(), securityFacade);

		mockedServices.put("userFacade", userFacade);

		PluginLoader pluginLoader = mock(PluginLoader.class);
		mockedServices.put(PluginLoader.class.getName(), pluginLoader);

		AnnotatedMatcher annotatedMatcher = mock(AnnotatedMatcher.class);
		mockedServices.put(AnnotatedMatcher.class.getName(), annotatedMatcher);

		FileRepositoryFacade fileRepositoryFacade = mock(FileRepositoryFacade.class);
		mockedServices.put(FileRepositoryFacade.class.getName(), fileRepositoryFacade);

		AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
		mockedServices.put(AuthenticationFacade.class.getName(), authenticationFacade);

		StringResourceLoader stringResourceLoader = mock(StringResourceLoader.class);
		mockedServices.put(StringResourceLoader.class.getName(), stringResourceLoader);

		LayoutComponentInitializer layoutComponentInitializer = mock(LayoutComponentInitializer.class);
		mockedServices.put(LayoutComponentInitializer.class.getName(), layoutComponentInitializer);
	}

	@Override
	protected void initMarkupSettings() {
	}

	@Override
	protected Class<? extends User> getPersistedUserImplementation() {
		return null;
	}

	@Override
	public Class<? extends Page> getHomePage() {
		return null;
	}

	@Override
	protected void onLoadConfiguration(Configuration config) {
		Configuration.get().setBeanLocatorAdapter(new MockedBeanLocatorAdapter().setMockedServices(mockedServices));
	}

	@Override
	protected void initComponentInjector() {
		getComponentInstantiationListeners().add(new TestInjector(this).setMockedServices(mockedServices));
		Injector.get().inject(this);
	}

	/**
	 * @return the mockedServices
	 */
	public Map<String, Object> getMockedServices() {
		return mockedServices;
	}

	/**
	 * @return the securityFacade
	 */
	public SecurityFacade getSecurityFacade() {
		return securityFacade;
	}

	/**
	 * @return the pluginRegistry
	 */
	public PluginRegistry getPluginRegistry() {
		return pluginRegistry;
	}

	/**
	 * @return the userFacade
	 */
	public UserFacade getUserFacade() {
		return userFacade;
	}
}
