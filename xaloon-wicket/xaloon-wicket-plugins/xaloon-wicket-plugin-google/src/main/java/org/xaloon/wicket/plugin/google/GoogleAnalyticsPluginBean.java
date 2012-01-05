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

import org.xaloon.core.api.plugin.StringPluginBean;

/**
 * Properties of google analytics plugin
 * 
 * @author vytautas r.
 */
public class GoogleAnalyticsPluginBean extends StringPluginBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean asynchronous;

	/**
	 * @return true if google analytics is used asynchronously
	 */
	public boolean isAsynchronous() {
		return asynchronous;
	}

	/**
	 * @param asynchronous
	 */
	public void setAsynchronous(boolean asynchronous) {
		this.asynchronous = asynchronous;
	}
}
