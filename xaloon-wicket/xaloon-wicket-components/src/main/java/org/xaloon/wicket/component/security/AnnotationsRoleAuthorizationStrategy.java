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

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.request.component.IRequestableComponent;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.util.ClassUtil;

/**
 * @author vytautas r.
 */
public class AnnotationsRoleAuthorizationStrategy implements IAuthorizationStrategy {

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 */
	public AnnotationsRoleAuthorizationStrategy() {
		Injector.get().inject(this);
	}

	@Override
	public <T extends IRequestableComponent> boolean isInstantiationAuthorized(Class<T> componentClass) {
		final RolesAllowed classAnnotation = ClassUtil.getAnnotation(componentClass, RolesAllowed.class);
		boolean authorized = true;
		if (classAnnotation != null) {
			authorized = securityFacade.hasAny(classAnnotation.value());
		}

		return authorized;
	}

	@Override
	public boolean isActionAuthorized(Component component, Action action) {
		// Get component's class
		final Class<?> componentClass = component.getClass();
		return isActionAuthorized(componentClass);
	}

	private boolean isActionAuthorized(Class<?> componentClass) {
		final RolesAllowed classAnnotation = ClassUtil.getAnnotation(componentClass, RolesAllowed.class);
		boolean authorized = true;
		if (classAnnotation != null) {
			authorized = securityFacade.hasAny(classAnnotation.value());
		}
		return authorized;
	}
}
