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

import javax.inject.Inject;

import org.apache.wicket.Session;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationToken;

/**
 * @author vytautas r.
 */
public class AuthenticatedWebSession extends WebSession {
	private static final long serialVersionUID = 1L;

	@Inject
	private transient SecurityFacade securityFacade;

	@Inject
	private transient StringResourceLoader stringResourceLoader;

	/**
	 * Construct.
	 * 
	 * @param request
	 */
	public AuthenticatedWebSession(Request request) {
		super(request);
		Injector.get().inject(this);
	}

	public static AuthenticatedWebSession get() {
		return (AuthenticatedWebSession)Session.get();
	}

	/**
	 * Try to logon the user. It'll call {@link SecurityFacade#authenticate(String, String)} to do the real work.
	 * 
	 * @param username
	 * @param password
	 * @return true, if logon was successful
	 */
	public final AuthenticationToken signIn(final String username, final String password) {
		AuthenticationToken token = getSecurityFacade().authenticate(username, password);
		return signinInternal(token);
	}

	/**
	 * Sign the user out.
	 */
	public void signOut() {
		getSecurityFacade().logout();
		invalidateNow();
	}

	/**
	 * @param token
	 * @return true if authentication successful
	 */
	public AuthenticationToken signIn(AuthenticationToken token) {
		AuthenticationToken authenticationResultToken = getSecurityFacade().authenticate(token);
		return signinInternal(token);
	}

	private AuthenticationToken signinInternal(AuthenticationToken authenticationResultToken) {
		if (authenticationResultToken.isAuthenticated()) {
			bind();
		}
		return authenticationResultToken;
	}

	/**
	 * Gets securityFacade.
	 * 
	 * @return securityFacade
	 */
	public SecurityFacade getSecurityFacade() {
		if (securityFacade == null) {
			Injector.get().inject(this);
		}
		return securityFacade;
	}
}
