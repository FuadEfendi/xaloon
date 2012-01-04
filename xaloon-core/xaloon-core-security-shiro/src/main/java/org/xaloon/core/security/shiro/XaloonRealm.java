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
package org.xaloon.core.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.security.shiro.util.PrincipalResolver;

/**
 * @author vytautas.r
 */
public class XaloonRealm extends AbstractRealm {

	/**
	 * Construct.
	 */
	public XaloonRealm() {
		setCacheManager(new MemoryConstrainedCacheManager());
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		UserDetails userDetails = PrincipalResolver.resolvePrincipal(principals.getPrimaryPrincipal(), UserDetails.class);
		if (userDetails != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			addInternalUserRoles(info, userDetails);
			return info;
		}
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken)authcToken;
		return doGetAuthenticationInfoInternal(token.getUsername());

	}
}
