package org.xaloon.wicket.plugin.blog;

import org.xaloon.core.api.security.SecurityAuthorities;

public interface BlogSecurityAuthorities extends SecurityAuthorities {
	/**
	 * User is able to create blog entries when having this role
	 */
	String BLOG_CREATOR = "BLOG_CREATOR";
	
	String ROLE_BLOGGER = "Blogger";
}
