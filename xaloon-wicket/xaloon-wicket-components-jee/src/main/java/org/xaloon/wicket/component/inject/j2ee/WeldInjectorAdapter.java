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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.xaloon.core.api.inject.BeanLocatorAdapter;
import org.xaloon.wicket.component.inject.j2ee.bm.BeanManagerLocator;
import org.xaloon.wicket.component.inject.j2ee.bm.WeldBeanManagerLocator;

/**
 * @author vytautas r.
 */
public class WeldInjectorAdapter implements BeanLocatorAdapter {

	private transient BeanManagerLocator beanManagerLocator;

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName, Class<T> clazz) {
		return (T)new JavaWeldBeanLocator(beanName, clazz, getBeanLocatorAdapter()).locateProxyTarget();
	}

	private BeanManagerLocator getBeanLocatorAdapter() {
		if (beanManagerLocator == null) {
			beanManagerLocator = new WeldBeanManagerLocator();
		}
		return beanManagerLocator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> getBeans(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		BeanManager beanManager = getBeanLocatorAdapter().getBeanManager();

		Set<Bean<?>> beans = beanManager.getBeans(clazz);
		if (beans != null && !beans.isEmpty()) {
			Iterator<Bean<?>> beanIterator = beans.iterator();
			while (beanIterator.hasNext()) {
				Bean<?> bean = beanIterator.next();
				CreationalContext<?> ctx = beanManager.createCreationalContext(bean);
				result.add((T)beanManager.getReference(bean, clazz, ctx));
			}
		}
		return result;
	}
}
