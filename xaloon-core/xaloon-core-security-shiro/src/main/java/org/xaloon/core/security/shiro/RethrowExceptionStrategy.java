package org.xaloon.core.security.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.pam.FirstSuccessfulStrategy;
import org.apache.shiro.realm.Realm;

public class RethrowExceptionStrategy extends FirstSuccessfulStrategy {
	@Override
	public AuthenticationInfo afterAttempt(Realm realm, AuthenticationToken token, AuthenticationInfo singleRealmInfo,
			AuthenticationInfo aggregateInfo, Throwable t) throws AuthenticationException {
		if (t != null) {
			throw new AuthenticationException(t);
		}
		return super.afterAttempt(realm, token, singleRealmInfo, aggregateInfo, t);
	}
}
