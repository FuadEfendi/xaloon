package org.xaloon.wicket.plugin.image.plugin;

import org.xaloon.core.api.security.SecurityAuthorities;

public interface GallerySecurityAuthorities extends SecurityAuthorities {
	/**
	 * Authority to create/edit the selected image
	 */
	String IMAGE_EDIT = "IMAGE_EDIT";

	/**
	 * Authority to delete the selected image
	 */
	String IMAGE_DELETE = "IMAGE_DELETE";
	
	
	String ROLE_GALLERY_USER = "Gallery user";
}
