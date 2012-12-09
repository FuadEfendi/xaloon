package org.xaloon.plugin;

import javax.inject.Named;

@Named("blogPlugin")
public class BlogPlugin extends AbstractPlugin {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "blogPlugin";
	}

	@Override
	public <T extends PluginProperties> T getProperties() {
		// TODO Auto-generated method stub
		return (T)new BlogPluginProperties();
	}

	 
}
