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
 
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.Page;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.jpa.user.model.JpaUser;
import org.xaloon.core.api.plugin.resource.ResourceRepositoryListener;
import org.xaloon.wicket.component.inject.spring.CdiAnnotationResolver;
import org.xaloon.wicket.component.inject.spring.CdiComponentInjector;
import org.xaloon.wicket.component.inject.spring.SpringAnnotationResolver;
import org.xaloon.wicket.component.inject.spring.SpringBeanAnnotationResolver;
import org.xaloon.wicket.component.inject.spring.SpringBeanLocatorAdapter;
import org.xaloon.wicket.component.security.AuthenticatedWebApplication;
import org.xaloon.wicket.component.sitemap.SiteMap;
import org.xaloon.wicket.plugin.blog.rss.BlogRssFeed;


import ${package}.page.IndexPage;
import ${package}.temp.TemporaryFacade;


public class WicketApplication extends AuthenticatedWebApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(WicketApplication.class);

	@Inject
	private TemporaryFacade temporaryFacade;
	
	@Override 
	public Class<? extends Page> getHomePage() {
		return IndexPage.class;
	}
	
	@Override
	protected void initComponentInjector() {
		SpringAnnotationResolver[] resolvers = new SpringAnnotationResolver[] {new CdiAnnotationResolver(), new SpringBeanAnnotationResolver()};
		getComponentInstantiationListeners().add(new CdiComponentInjector(this, resolvers));
		Injector.get().inject(this);
	}
	
	@Override
	protected void onLoadConfiguration(Configuration config) {
		//Add action before saving resource
		Configuration.get().getResourceRepositoryListeners().add(new ResourceRepositoryListener() {
			private static final long serialVersionUID = 1L;

			public void onBeforeSaveProperty(Plugin plugin, String propertyKey, Object value) {
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug(plugin.getName() + "${symbol_escape}t" + propertyKey + "${symbol_escape}t" + value);
				}
			}

			public void onAfterSaveProperty(Plugin plugin, String propertyKey) {}
		});
		Configuration.get().setBeanLocatorAdapter(new SpringBeanLocatorAdapter());
	}
	
	@Override
	protected void onBeforeMountPackage(List<String> mountingPackages) {
		mountingPackages.add("${package}");
	}
	
	@Override
	protected void init() {
		super.init();
		
		mountPage("/sitemap.xml", SiteMap.class);
		mountPage("/blog-rss", BlogRssFeed.class);
		
		temporaryFacade.initDemoData();
		setRootRequestMapper(new HttpsMapper(getRootRequestMapper(), new HttpsConfig(8080, 8443)));
	}	
	
	@Override
	protected Class<? extends User> getPersistedUserImplementation() {
		return JpaUser.class;
	}
}