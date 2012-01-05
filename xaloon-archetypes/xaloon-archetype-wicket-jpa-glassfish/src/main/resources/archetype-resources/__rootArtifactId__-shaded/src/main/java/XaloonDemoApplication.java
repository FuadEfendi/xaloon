#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
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
package ${package};

import javax.inject.Inject;

import org.apache.wicket.Page;
import org.apache.wicket.injection.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wicketstuff.javaee.injection.JavaEEComponentInjector;
import org.wicketstuff.shiro.authz.ShiroUnauthorizedComponentListener;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.user.model.JpaUser;
import org.xaloon.core.api.plugin.resource.ResourceRepositoryListener;
import org.xaloon.wicket.component.inject.j2ee.WeldComponentInjector;
import org.xaloon.wicket.component.security.AuthenticatedWebApplication;
import org.xaloon.wicket.component.security.page.SignInPage;
import org.xaloon.wicket.component.sitemap.SiteMap;
import org.xaloon.wicket.plugin.blog.rss.BlogRssFeed;
import org.xaloon.wicket.component.inject.j2ee.WeldInjectorAdapter;

import ${package}.temp.TemporaryFacade;
import ${package}.page.UnauthorizedPage;

/**
 * @author vytautas.r
 */
public class XaloonDemoApplication extends AuthenticatedWebApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(XaloonDemoApplication.class);

	@Inject
	private TemporaryFacade temporaryFacade;

	@Override
	public Class<? extends Page> getHomePage() {
		return ${package}.page.IndexPage.class;
	}

	@Override
	protected void init() {
		super.init();
		// Custom settings
		getRequestLoggerSettings().setRequestLoggerEnabled(true);

		temporaryFacade.initDemoData();

		mountPage("/sitemap.xml", SiteMap.class);
		mountPage("/blog-rss", BlogRssFeed.class);

		mountPage("/index", ${package}.page.IndexPage.class);

		getSecuritySettings().setUnauthorizedComponentInstantiationListener(
			new ShiroUnauthorizedComponentListener(SignInPage.class, UnauthorizedPage.class, null));
	}

	@Override
	protected void onLoadConfiguration(Configuration config) {
		Configuration.get().getResourceRepositoryListeners().add(new ResourceRepositoryListener() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void onBeforeSaveProperty(Plugin plugin, String propertyKey, Object value) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(plugin.getName() + "${symbol_escape}t" + propertyKey + "${symbol_escape}t" + value);
				}
			}

			public void onAfterSaveProperty(Plugin plugin, String propertyKey) {
			}
		});
		Configuration.get().setBeanLocatorAdapter(new WeldInjectorAdapter());
	}

	@Override
	protected void initComponentInjector() {
		getComponentInstantiationListeners().add(new JavaEEComponentInjector(this));
		getComponentInstantiationListeners().add(new WeldComponentInjector(this));
		Injector.get().inject(this);
	}
	
	@Override
	protected Class<? extends User> getPersistedUserImplementation() {
		return JpaUser.class;
	}
}
