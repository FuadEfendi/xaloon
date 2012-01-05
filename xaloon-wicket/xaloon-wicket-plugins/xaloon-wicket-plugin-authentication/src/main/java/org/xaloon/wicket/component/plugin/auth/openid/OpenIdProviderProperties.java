package org.xaloon.wicket.component.plugin.auth.openid;

import org.xaloon.wicket.component.plugin.auth.ProviderProperties;

public class OpenIdProviderProperties extends ProviderProperties {
	private static final long serialVersionUID = 1L;

	private String link;

	public OpenIdProviderProperties(String loginType, String openidLink) {
		super(loginType);
		this.link = openidLink;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
