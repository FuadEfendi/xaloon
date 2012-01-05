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
package org.xaloon.wicket.component.inject.spring;

import org.apache.wicket.Component;
import org.apache.wicket.application.IComponentInstantiationListener;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author vytautas r.
 */
public class CdiComponentInjector extends Injector implements IComponentInstantiationListener {

	private final IFieldValueFactory fieldValueFactory;

	/**
	 * Construct.
	 * 
	 * @param webapp
	 * @param beanNameResolver
	 */
	public CdiComponentInjector(WebApplication webapp, SpringAnnotationResolver... beanNameResolver) {
		// locate application context through spring's default location
		// mechanism and pass it on to the proper constructor
		this(webapp, WebApplicationContextUtils.getRequiredWebApplicationContext(webapp.getServletContext()), beanNameResolver);
	}

	/**
	 * Construct.
	 * 
	 * @param webapp
	 * @param ctx
	 * @param beanNameResolver
	 */
	public CdiComponentInjector(WebApplication webapp, ApplicationContext ctx, SpringAnnotationResolver... beanNameResolver) {
		this(webapp, ctx, true, beanNameResolver);
	}

	/**
	 * Construct.
	 * 
	 * @param webapp
	 * @param ctx
	 * @param wrapInProxies
	 * @param beanNameResolver
	 */
	public CdiComponentInjector(WebApplication webapp, ApplicationContext ctx, boolean wrapInProxies, SpringAnnotationResolver... beanNameResolver) {
		if (webapp == null) {
			throw new IllegalArgumentException("Argument [[webapp]] cannot be null");
		}

		if (ctx == null) {
			throw new IllegalArgumentException("Argument [[ctx]] cannot be null");
		}
		fieldValueFactory = new CdiProxyFieldValueFactory(new SpringContextLocator(), wrapInProxies, beanNameResolver);
		bind(webapp);
	}

	@Override
	protected void inject(Object object, IFieldValueFactory factory) {
		super.inject(object, fieldValueFactory);
	}


	public void onInstantiation(Component component) {
		inject(component);
	}

	@Override
	public void inject(Object object) {
		inject(object, fieldValueFactory);
	}
}
