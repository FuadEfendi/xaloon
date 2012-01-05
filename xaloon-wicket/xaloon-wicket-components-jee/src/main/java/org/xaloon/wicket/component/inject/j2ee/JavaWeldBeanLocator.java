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

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.proxy.IProxyTargetLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.wicket.component.inject.j2ee.bm.BeanManagerLocator;

/**
 * @author vytautas.r
 */
public class JavaWeldBeanLocator implements IProxyTargetLocator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(JavaWeldBeanLocator.class);

	private String beanName;
	private Class<?> beanType;

	private BeanManagerLocator beanManagerLocator;

	/**
	 * Construct.
	 * 
	 * @param beanId
	 * @param beanType
	 * @param locator
	 */
	public JavaWeldBeanLocator(String beanId, Class<?> beanType, final BeanManagerLocator locator) {
		if (beanType == null) {
			throw new IllegalArgumentException("[beanType] argument cannot be null");
		}
		if (locator == null) {
			throw new IllegalArgumentException("[locator] argument cannot be null");
		}
		this.beanType = beanType;
		beanName = beanId;
		beanManagerLocator = locator;
	}

	@Override
	public Object locateProxyTarget() {
		BeanManager bm = beanManagerLocator.getBeanManager();
		if (!StringUtils.isEmpty(beanName)) {
			Bean<?> bean = bm.getBeans(beanName).iterator().next();
			CreationalContext<?> ctx = bm.createCreationalContext(bean);
			return bm.getReference(bean, bean.getClass(), ctx);
		}
		Set<Bean<?>> beans = bm.getBeans(beanType);
		if (beans == null || beans.isEmpty()) {
			return null;
		}
		Bean<?> bean = beans.iterator().next();
		CreationalContext<?> ctx = bm.createCreationalContext(bean);
		if (bean == null) {
			return null;
		}
		return bm.getReference(bean, beanType, ctx); // this could be inlined, but intentionally left this way
	}
}
