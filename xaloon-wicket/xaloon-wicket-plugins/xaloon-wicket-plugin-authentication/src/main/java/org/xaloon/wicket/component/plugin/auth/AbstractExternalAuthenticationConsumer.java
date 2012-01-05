package org.xaloon.wicket.component.plugin.auth;

import javax.inject.Inject;

import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.external.AuthenticationConsumer;

/**
 * Abstract external authentication facade
 * 
 * @author vytautas r.
 * 
 */
public abstract class AbstractExternalAuthenticationConsumer<T extends ProviderProperties> implements AuthenticationConsumer {
	private static final long serialVersionUID = 1L;

	@Inject
	private PluginRegistry pluginRegistry;

	@Inject
	protected StringResourceLoader stringResourceLoader;

	/**
	 * @return
	 */
	public T getAuthenticationProviderProperties() {
		if (pluginRegistry == null) {
			return null;
		}
		ExternalAuthenticationPluginBean pluginBean = pluginRegistry.getPluginBean(ExternalAuthenticationPlugin.class);
		return getCustomAuthenticationProvider(pluginBean);
	}

	/**
	 * Default authentication provider
	 * 
	 * @param pluginBean
	 * @return
	 */
	protected abstract T getCustomAuthenticationProvider(ExternalAuthenticationPluginBean pluginBean);

	protected String fixUsername(String username) {
		if (username.contains("id=")) {
			return username.substring(username.indexOf("id=") + 3);
		}
		return username;
	}
}
