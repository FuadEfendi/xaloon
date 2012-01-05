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
package org.xaloon.wicket.component.security;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.IRequestCycleProvider;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.servlet.ServletWebRequest;
import org.apache.wicket.protocol.http.servlet.ServletWebResponse;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.cycle.RequestCycleContext;
import org.apache.wicket.request.resource.SharedResourceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.plugin.PluginLoader;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.impl.path.DefaultFileDescriptorAbsolutePathStrategy;
import org.xaloon.core.impl.plugin.category.CategoryGroupPluginRegistryListener;
import org.xaloon.core.impl.plugin.category.CategoryMenuPluginRegistryListener;
import org.xaloon.wicket.component.mount.DefaultPageMountScannerListener;
import org.xaloon.wicket.component.mount.impl.SpringMountScanner;
import org.xaloon.wicket.component.resource.FileResource;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.plugin.menu.DynamicMenuMountScannerListener;

/**
 * @author vytautas r.
 */
public abstract class AuthenticatedWebApplication extends WebApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticatedWebApplication.class);

	private static final String DEFAULT_MOUNTING_XALOON = "org.xaloon.wicket";

	private static final String ENCODING_UTF_8 = "UTF-8";

	private static final String[] botAgents = { "googlebot", "msnbot", "slurp", "jeeves", "appie", "architext", "jeeves", "bjaaland", "ferret",
			"gulliver", "harvest", "htdig", "linkwalker", "lycos_", "moget", "muscatferret", "myweb", "nomad", "scooter", "yahoo!\\sslurp\\schina",
			"slurp", "weblayers", "antibot", "bruinbot", "digout4u", "echo!", "ia_archiver", "jennybot", "mercator", "netcraft", "msnbot",
			"petersnews", "unlost_web_crawler", "voila", "webbase", "webcollage", "cfetch", "zyborg", "wisenutbot", "robot", "crawl", "spider" };

	@Inject
	private DynamicMenuMountScannerListener dynamicMenuMountScannerListener;

	@Inject
	private CategoryMenuPluginRegistryListener categoryNodeObserver;

	@Inject
	private CategoryGroupPluginRegistryListener categoryGroupPluginRegistryListener;

	@Inject
	private PluginLoader pluginLoader;

	@Override
	public Session newSession(Request request, Response response) {
		return new AuthenticatedWebSession(request);
	}

	@Override
	protected void init() {
		initComponentInjector();
		initRequestCycleSettings();
		initDebugSettings();
		initSecuritySettings();
		initMarkupSettings();
		initConfiguration();
		initJavascriptComponents();

		loadPlugins();
		mountPages();
		mountImages();


	}

	private void initConfiguration() {
		Configuration config = Configuration.get();
		config.getPluginRegistryListenerCollection().add(categoryNodeObserver);
		config.getPluginRegistryListenerCollection().add(categoryGroupPluginRegistryListener);
		config.setPersistedUserClass(getPersistedUserImplementation());
		config.setFileDescriptorAbsolutePathStrategy(new DefaultFileDescriptorAbsolutePathStrategy());
		onLoadConfiguration(config);
	}

	/**
	 * Persisted user implementation, which will be used to store and select user from database. By default if should be JpaUser.class
	 * 
	 * @return class which implements {@link User} interface. or extension
	 */
	protected abstract Class<? extends User> getPersistedUserImplementation();

	/**
	 * Add specific values to application config
	 * 
	 * @param config
	 */
	protected void onLoadConfiguration(Configuration config) {
	}

	private void loadPlugins() {
		pluginLoader.loadSystemPlugins();
		pluginLoader.loadUserPlugins();
		pluginLoader = null;
	}

	private void mountPages() {
		List<String> mountingPackages = new ArrayList<String>();
		mountingPackages.add(DEFAULT_MOUNTING_XALOON);
		onBeforeMountPackage(mountingPackages);
		mountPackages(mountingPackages);
	}

	private void mountPackages(List<String> mountingPackages) {
		SpringMountScanner scanner = new SpringMountScanner();
		scanner.addMountScannerListener(dynamicMenuMountScannerListener);
		onAddMountScannerListener(scanner);
		for (String mountingPackage : mountingPackages) {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Mounting package: " + mountingPackage);
			}
			scanner.mountPackage(this, mountingPackage);
		}
	}

	protected void onAddMountScannerListener(SpringMountScanner scanner) {
		scanner.addMountScannerListener(new DefaultPageMountScannerListener());
	}

	/**
	 * Add all packages which you want to be scanned for page mounting
	 * 
	 * @param mountingPackages
	 *            package list which will be scanned and pages will be mounted
	 */
	protected void onBeforeMountPackage(List<String> mountingPackages) {
	}

	protected void mountImages() {
		String resourceKey = ImageLink.IMAGE_RESOURCE;
		getSharedResources().add(resourceKey, new FileResource());
		mountResource(resourceKey, new SharedResourceReference(resourceKey));
	}

	protected void initComponentInjector() {
	}

	protected void initMarkupSettings() {
		getMarkupSettings().setCompressWhitespace(true);
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setDefaultMarkupEncoding(ENCODING_UTF_8);
	}

	protected void initDebugSettings() {
		getDebugSettings().setDevelopmentUtilitiesEnabled(true);
	}

	protected void initSecuritySettings() {
		getSecuritySettings().setAuthorizationStrategy(new AnnotationsRoleAuthorizationStrategy());
	}

	protected void initRequestCycleSettings() {
		getRequestCycleSettings().setResponseRequestEncoding(ENCODING_UTF_8);

		setRequestCycleProvider(new IRequestCycleProvider() {
			public RequestCycle get(RequestCycleContext context) {
				return new AuthenticatedRequestCycle(context);
			}
		});
	}

	/**
	 * Initialize jqwicket by default
	 */
	protected void initJavascriptComponents() {
	}

	@Override
	protected org.apache.wicket.request.http.WebResponse newWebResponse(final org.apache.wicket.request.http.WebRequest webRequest,
		javax.servlet.http.HttpServletResponse httpServletResponse) {
		return new ServletWebResponse((ServletWebRequest)webRequest, httpServletResponse) {
			@Override
			public String encodeURL(CharSequence url) {
				final String agent = webRequest.getHeader("User-Agent");
				return isAgent(agent) ? url.toString() : super.encodeURL(url).toString();
			}
		};
	};

	protected boolean isAgent(final String agent) {
		if (agent != null) {
			final String lowerAgent = agent.toLowerCase();
			for (final String bot : botAgents) {
				if (lowerAgent.indexOf(bot) != -1) {
					return true;
				}
			}
		}
		return false;
	}
}
