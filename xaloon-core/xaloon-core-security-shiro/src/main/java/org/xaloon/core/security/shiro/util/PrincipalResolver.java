package org.xaloon.core.security.shiro.util;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.subject.PrincipalCollection;

public class PrincipalResolver implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static <T> T resolvePrincipal(Object principal, Class<T> principalClass) {
		if (principal == null) {
			return null;
		}
		if (principal instanceof PrincipalCollection) {
			return resolvePrincipal((PrincipalCollection)principal, principalClass);			
		}
		if (principalClass.isAssignableFrom(principal.getClass())) {
			return (T)principal;
		}
		return null;
	}	
	
	public static <T> T resolvePrincipal(PrincipalCollection principals, Class<T> principalClass) {
		Collection<T> principalCollection = principals.byType(principalClass);
		if (principalCollection != null && !principalCollection.isEmpty()) {
			return principalCollection.iterator().next();
		}
		return null;
	}
}
