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
package org.xaloon.wicket.component.security.page;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.application.page.LayoutWebPage;
import org.xaloon.wicket.component.security.panel.SignInPanel;

/**
 * @author vytautas r.
 */
public abstract class AbstractSignInPage extends LayoutWebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected Panel getContentPanel(String id, PageParameters pageParameters) {
		// If there is no referer set then try to set one
		String refereUrl = WebSession.get().getMetaData(SignInPanel.METADATAKEY_REFERER);
		if (StringUtils.isEmpty(refereUrl)) {
			HttpServletRequest req = (HttpServletRequest)RequestCycle.get().getRequest().getContainerRequest();
			refereUrl = req.getHeader("referer");
			WebSession.get().setMetaData(SignInPanel.METADATAKEY_REFERER, refereUrl);
		}
		return onGetContentPanel(id, pageParameters);
	}

	protected abstract Panel onGetContentPanel(String id, PageParameters pageParameters);
}
