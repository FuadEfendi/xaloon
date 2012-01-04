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
package org.xaloon.wicket.security.spring.external;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.xaloon.core.api.security.external.AuthenticationToken;

/**
 * @author vytautas r.
 */
public class SpringAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;

	private final AuthenticationToken token;

	/**
	 * This constructor can be safely used by any code that wishes to create a <code>ExternalAuthenticationToken</code>, as the
	 * {@link #isAuthenticated()} will return <code>false</code>.
	 * 
	 * @param token
	 * 
	 */
	public SpringAuthenticationToken(AuthenticationToken token) {
		super(null);
		this.token = token;
		setAuthenticated(false);
	}

	/**
	 * Construct.
	 * 
	 * @param name
	 * @param token
	 * @param authorities
	 */
	public SpringAuthenticationToken(AuthenticationToken token, Collection<GrantedAuthority> authorities) {
		super(authorities);
		this.token = token;
		setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	/**
	 * @return authentication token requests
	 */
	public AuthenticationToken getToken() {
		return token;
	}

	@Override
	public String getName() {
		return token.getName();
	}
}
