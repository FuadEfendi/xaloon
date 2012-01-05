/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xaloon.wicket.plugin.google;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.plugin.KeyValuePluginBean;
import org.xaloon.core.api.plugin.PluginRegistry;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.wicket.component.html.MetaTagWebContainer;

/**
 * Simple markup container which produces meta tag information with provided name and content.
 * <p>
 * Name and content are taken from {@link GoogleWebMasterPlugin} plugin's properties. Container will be hidden if plugin is disabled.
 * <p>
 * Java method:
 * 
 * <pre>
 * add(new GoogleWebMasterMarkupContainer(&quot;webmaster-keys&quot;));
 * </pre>
 * <p>
 * HTML:
 * 
 * <pre>
 * &lt;meta wicket:id="webmaster-keys" name="keywords" content="test"/&gt;
 * </pre>
 * 
 * @author vytautas r.
 * @since 1.5
 */
public class GoogleWebMasterMarkupContainer extends MetaTagWebContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PluginRegistry pluginRegistry;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public GoogleWebMasterMarkupContainer(String id) {
		super(id);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		Class<?> pluginClass = GoogleWebMasterPlugin.class;
		if (pluginRegistry.isEnabled(pluginClass)) {
			KeyValuePluginBean pluginBean = pluginRegistry.getPluginBean(pluginClass);
			if (!StringUtils.isEmpty(pluginBean.getKey()) && !StringUtils.isEmpty(pluginBean.getValue())) {
				setDefaultModel(new Model<KeyValue<String, String>>(new DefaultKeyValue<String, String>(pluginBean.getKey(), pluginBean.getValue())));
				return;
			}
		}
		setVisible(false);
	}
}
