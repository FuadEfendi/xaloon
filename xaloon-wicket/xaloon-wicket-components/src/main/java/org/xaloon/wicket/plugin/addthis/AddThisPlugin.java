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
package org.xaloon.wicket.plugin.addthis;

import javax.inject.Named;

import org.xaloon.core.api.plugin.AbstractPlugin;
import org.xaloon.core.impl.plugin.category.DefaultPluginCategories;
import org.xaloon.wicket.plugin.addthis.panel.AddThisAdministrationPanel;

/**
 * 
 * Please, visit <a href= "http://www.addthis.com/">http://www.addthis.com/</a> for more details
 * 
 * @author vytautas r.
 * @version 1.1, 09/19/10
 * @since 1.3
 */
@Named
public class AddThisPlugin extends AbstractPlugin<AddThisPluginBean> {
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 */
	public AddThisPlugin() {
		setCategory(DefaultPluginCategories.ADVERTISEMENT);
	}

	@Override
	public Class<?> getAdministratorFormClass() {
		return AddThisAdministrationPanel.class;
	}
}
