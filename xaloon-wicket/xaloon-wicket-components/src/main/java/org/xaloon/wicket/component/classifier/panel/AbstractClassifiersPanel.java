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
package org.xaloon.wicket.component.classifier.panel;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.EmptyPlugin;
import org.xaloon.wicket.plugin.AbstractPluginPanel;

/**
 * @author vytautas r.
 */
public abstract class AbstractClassifiersPanel extends AbstractPluginPanel<AbstractPluginBean, EmptyPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageRequestParameters
	 */
	public AbstractClassifiersPanel(String id, PageParameters pageRequestParameters) {
		super(id, pageRequestParameters);
	}

	@Override
	protected boolean isPluginEnabled() {
		return true;
	}

	@Override
	protected EmptyPlugin getPlugin() {
		return null;
	}

	@Override
	protected AbstractPluginBean getPluginBean() {
		return null;
	}
}
