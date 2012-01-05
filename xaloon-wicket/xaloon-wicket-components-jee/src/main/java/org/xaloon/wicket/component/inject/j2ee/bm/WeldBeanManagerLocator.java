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
package org.xaloon.wicket.component.inject.j2ee.bm;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.wicket.Application;
import org.apache.wicket.MetaDataKey;
import org.apache.wicket.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author vytautas r.
 */
public class WeldBeanManagerLocator implements BeanManagerLocator {
	private transient BeanManager beanManager;

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(WeldBeanManagerLocator.class);

	static MetaDataKey<BeanManagerHolder> BEAN_MANAGER_KEY = new MetaDataKey<BeanManagerHolder>() {

		private static final long serialVersionUID = 1L;

	};

	@Override
	public BeanManager getBeanManager() {
		if (beanManager == null) {
			Application application = ThreadContext.getApplication();
			if (application == null) {
				// Runs in different thread. get beanManager directly from JNDI.
				beanManager = locateBeanManagerFromJNDI();
			} else {
				// Get bean manager from application
				beanManager = getBeanManagerFromApplication(application);
			}
		}
		return beanManager;
	}

	private BeanManager getBeanManagerFromApplication(Application application) {
		BeanManager beanManager = null;
		BeanManagerHolder beanManagerHolder = application.getMetaData(BEAN_MANAGER_KEY);
		if (beanManagerHolder == null || beanManagerHolder.getBeanManager() == null) {
			beanManager = locateBeanManagerFromJNDI();
			Application.get().setMetaData(BEAN_MANAGER_KEY, new BeanManagerHolder(beanManager));
		} else {
			beanManager = beanManagerHolder.getBeanManager();
		}
		return beanManager;
	}

	private BeanManager locateBeanManagerFromJNDI() {
		try {
			InitialContext initialContext = new InitialContext();
			return (BeanManager)initialContext.lookup("java:comp/BeanManager");
		} catch (NamingException e) {
			LOGGER.error("Couldn't get BeanManager through JNDI");
			return null;
		}
	}
}
