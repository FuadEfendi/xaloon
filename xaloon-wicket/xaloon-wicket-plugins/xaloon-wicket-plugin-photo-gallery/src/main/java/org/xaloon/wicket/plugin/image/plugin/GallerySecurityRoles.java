package org.xaloon.wicket.plugin.image.plugin;

import org.xaloon.core.api.security.SecurityRoles;

public interface GallerySecurityRoles extends SecurityRoles {
	/**
	 * Authority to edit the selected image
	 */
	String IMAGE_EDIT = "IMAGE_EDIT";

	/**
	 * Authority to delete the selected image
	 */
	String IMAGE_DELETE = "IMAGE_DELETE";
}
