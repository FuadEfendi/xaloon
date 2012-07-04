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
package org.xaloon.wicket.plugin.addthis.panel;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.addthis.AddThisPlugin;
import org.xaloon.wicket.plugin.addthis.AddThisPluginBean;

/**
 * 
 * Please, visit <a href= "http://www.addthis.com/">http://www.addthis.com/</a> for more details
 * 
 * @author vytautas r.
 * @version 1.1, 09/19/10
 * @since 1.3
 */

public class AddThisPanel extends AbstractPluginPanel<AddThisPluginBean, AddThisPlugin> {
	private static final long serialVersionUID = 1L;

	private static final String ADD_THIS_JAVASCRIPT_END_TAG = "\"></script>";
	private static final String ADD_THIS_JAVA_SCRIPT = "<script type=\"text/javascript\" src=\"http://s7.addthis.com/js/250/addthis_widget.js#username=";

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public AddThisPanel(String id) {
		super(id);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		Response response = RequestCycle.get().getResponse();
		response.write(getJavaScript());
	}

	/**
	 * Returns javascript of addthis component with provided username
	 * 
	 * @return string format of formatted javascript
	 */

	private CharSequence getJavaScript() {
		return ADD_THIS_JAVA_SCRIPT + getPluginBean().getUsername() + ADD_THIS_JAVASCRIPT_END_TAG;
	}

	@Override
	protected void onInitialize(AddThisPlugin plugin, AddThisPluginBean pluginBean) {
	}

	@Override
	protected boolean isPluginEnabled() {
		String username = getPluginBean().getUsername();
		return super.isPluginEnabled() && !StringUtils.isEmpty(username);
	}
}
