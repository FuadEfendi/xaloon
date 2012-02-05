package org.xaloon.wicket.plugin.blog;

import org.xaloon.core.api.security.SecurityRoles;

public interface BlogSecurityRoles extends SecurityRoles {
	/**
	 * User is able to create blog entries when having this role
	 */
	String BLOG_CREATOR = "BLOG_CREATOR";
}
