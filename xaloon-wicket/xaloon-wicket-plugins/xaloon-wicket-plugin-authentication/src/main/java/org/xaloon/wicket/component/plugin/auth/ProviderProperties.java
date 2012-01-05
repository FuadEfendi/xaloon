package org.xaloon.wicket.component.plugin.auth;

import java.io.Serializable;

public class ProviderProperties implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private boolean enabled = true;

	private String loginType;
	
	public ProviderProperties(String loginType) {
		this.loginType = loginType;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	/**
	 * @return unique login type
	 */
	public String getLoginType() {
		return loginType;
	}
}
