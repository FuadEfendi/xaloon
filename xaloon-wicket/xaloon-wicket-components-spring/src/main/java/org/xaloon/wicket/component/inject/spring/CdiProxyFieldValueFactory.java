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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.injection.IFieldValueFactory;
import org.apache.wicket.proxy.LazyInitProxyFactory;
import org.apache.wicket.spring.ISpringContextLocator;
import org.apache.wicket.spring.SpringBeanLocator;
import org.apache.wicket.util.lang.Generics;
import org.apache.wicket.util.string.Strings;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class CdiProxyFieldValueFactory implements IFieldValueFactory {
	private static final String NULL = "NULL";
	
	private final ISpringContextLocator contextLocator;
	private final SpringAnnotationResolver[] beanNameResolvers;
	
	private final ConcurrentHashMap<SpringBeanLocator, Object> cache = Generics.newConcurrentHashMap();

	private final ConcurrentHashMap<Class<?>, String> beanNameCache = Generics.newConcurrentHashMap();

	private final boolean wrapInProxies;

	public CdiProxyFieldValueFactory(ISpringContextLocator contextLocator, boolean wrapInProxies, SpringAnnotationResolver ... beanNameResolvers) {
		this.contextLocator = contextLocator;
		this.wrapInProxies = wrapInProxies;
		this.beanNameResolvers = beanNameResolvers;
	}

	public Object getFieldValue(Field field, Object fieldOwner) {
		if (supportsField(field)) {
			String beanName = getBeanName(field);
			if (StringUtils.isEmpty(beanName)) {
				return null;
			}
			SpringBeanLocator locator = new SpringBeanLocator(beanName, field.getType(), contextLocator);

			// only check the cache if the bean is a singleton
			Object cachedValue = cache.get(locator);
			if (cachedValue != null) {
				return cachedValue;
			}

			final Object target;
			if (wrapInProxies) {
				target = LazyInitProxyFactory.createProxy(field.getType(), locator);
			} else {
				target = locator.locateProxyTarget();
			}

			// only put the proxy into the cache if the bean is a singleton
			if (locator.isSingletonBean()) {
				cache.put(locator, target);
			}
			return target;
		}
		return null;
	}

	private String getBeanName(final Field field) {
		String name = beanNameCache.get(field.getType());
		if (StringUtils.isEmpty(name)) {
			name = resolveBeanName(field);
			if (StringUtils.isEmpty(name)) {
				name = getBeanNameOfClass(contextLocator.getSpringContext(), field.getType());					
			}
			if (!StringUtils.isEmpty(name)) {
				beanNameCache.put(field.getType(), name);			
			} else {
				beanNameCache.put(field.getType(), NULL);
			}
		} else if (NULL.equals(name)) {
			return null;
		}
		return name;
	}

	private String resolveBeanName(Field field) {
		String name = null;
		for (SpringAnnotationResolver beanNameResolver : beanNameResolvers) {
			name = beanNameResolver.getBeanName(field);
			if (!StringUtils.isEmpty(name)) {
				break;
			}
		}
		return name;
	}

	private final String getBeanNameOfClass(ApplicationContext ctx, Class<?> clazz) {
		// get the list of all possible matching beans
		List<String> names = new ArrayList<String>(Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(ctx, clazz)));

		// filter out beans that are not candidates for autowiring
		Iterator<String> it = names.iterator();
		while (it.hasNext()) {
			final String possibility = it.next();
			if (ctx instanceof AbstractApplicationContext) {
				BeanDefinition beanDef = getBeanDefinition(((AbstractApplicationContext) ctx).getBeanFactory(), possibility);
				if (BeanFactoryUtils.isFactoryDereference(possibility) || possibility.startsWith("scopedTarget.") || !beanDef.isAutowireCandidate()) {
					it.remove();
				}
			}
		}

		if (names.isEmpty()) {
			return null;
		} else if (names.size() > 1) {
			if (ctx instanceof AbstractApplicationContext) {
				List<String> primaries = new ArrayList<String>();
				for (String name : names) {
					BeanDefinition beanDef = getBeanDefinition(((AbstractApplicationContext) ctx).getBeanFactory(), name);
					if (beanDef instanceof AbstractBeanDefinition) {
						if (((AbstractBeanDefinition) beanDef).isPrimary()) {
							primaries.add(name);
						}
					}
				}
				if (primaries.size() == 1) {
					return primaries.get(0);
				}
			}
			StringBuilder msg = new StringBuilder();
			msg.append("More than one bean of type [");
			msg.append(clazz.getName());
			msg.append("] found, you have to specify the name of the bean ");
			msg.append("(@Named(\"foo\")) in order to resolve this conflict. ");
			msg.append("Matched beans: ");
			msg.append(Strings.join(",", names.toArray(new String[0])));
			throw new IllegalStateException(msg.toString());
		} else {
			return names.get(0);
		}
	}

	private BeanDefinition getBeanDefinition(ConfigurableListableBeanFactory beanFactory, String name) {
		if (beanFactory.containsBeanDefinition(name)) {
			return beanFactory.getBeanDefinition(name);
		} else {
			BeanFactory parent = beanFactory.getParentBeanFactory();
			if (parent != null && parent instanceof ConfigurableListableBeanFactory) {
				return getBeanDefinition((ConfigurableListableBeanFactory) parent, name);
			} else {
				return null;
			}
		}
	}

	public boolean supportsField(Field field) {
		boolean supports = false;
		for (SpringAnnotationResolver beanNameResolver : beanNameResolvers) {
			supports = field.isAnnotationPresent(beanNameResolver.getAnnotationToSupport());
			if (supports) {
				break;
			}
		}
		return supports;
	}
}
