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
package org.xaloon.wicket.plugin.admin;

import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.wicket.plugin.AbstractPluginPanel;

/**
 * Abstract administration panel. There are no specific features currently, but left for future usage.
 * 
 * @author vytautas r.
 * @since 1.5
 * @param <K>
 *            plugin bean of selected plugin
 * @param <T>
 *            plugin class
 */
public abstract class AbstractPluginAdministrationPanel<K extends AbstractPluginBean, T extends Plugin> extends AbstractPluginPanel<K, T> {

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public AbstractPluginAdministrationPanel(String id) {
		super(id);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * For administrative panel always return true
	 * 
	 * @see org.xaloon.wicket.plugin.AbstractPluginPanel#isPluginEnabled()
	 */
	@Override
	public boolean isPluginEnabled() {
		return true;
	}

	/**
	 * For administrative panel always return true
	 * 
	 * @see org.xaloon.wicket.plugin.AbstractPluginPanel#isPluginValid()
	 */
	@Override
	protected boolean isPluginValid() {
		return true;
	}

	/**
	 * @param model
	 */
	public void initModel(IModel<K> model) {
		setDefaultModel(new CompoundPropertyModel<K>(model));
	}
}
