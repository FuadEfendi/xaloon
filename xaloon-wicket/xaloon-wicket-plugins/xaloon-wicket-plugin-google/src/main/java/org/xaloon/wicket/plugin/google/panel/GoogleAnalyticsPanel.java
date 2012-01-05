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
package org.xaloon.wicket.plugin.google.panel;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.google.GoogleAnalyticsPlugin;
import org.xaloon.wicket.plugin.google.GoogleAnalyticsPluginBean;

/**
 * @author vytautas r.
 */
public class GoogleAnalyticsPanel extends AbstractPluginPanel<GoogleAnalyticsPluginBean, GoogleAnalyticsPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public GoogleAnalyticsPanel(String id) {
		super(id);
	}

	@Override
	protected void onInitialize(GoogleAnalyticsPlugin plugin, GoogleAnalyticsPluginBean pluginBean) {
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if (!isEnabled() || StringUtils.isEmpty(getPluginBean().getValue())) {
			return;
		}
		Response response = RequestCycle.get().getResponse();
		response.write(getJavaScript());
	}

	private CharSequence getJavaScript() {
		if (getPluginBean().isAsynchronous()) {
			return getAsynchronousJavaScript();
		}
		return getSynchronousJavaScript();
	}

	private CharSequence getSynchronousJavaScript() {
		StringBuilder result = new StringBuilder();
		result.append("<script type=\"text/javascript\">\n");
		result.append("var gaJsHost = ((\"https:\" == document.location.protocol) ? \"https://ssl.\" : \"http://www.\");\n");
		result.append("document.write(unescape(\"%3Cscript src='\" + gaJsHost + \"google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E\"));\n");
		result.append("</script>\n");
		result.append("<script type=\"text/javascript\">\n");
		result.append("try {\n");
		result.append("var pageTracker = _gat._getTracker(\"" + getPluginBean().getValue() + "\");\n");
		result.append("pageTracker._trackPageview();\n");
		result.append("} catch(err) {}</script>\n");
		return result.toString();
	}

	private CharSequence getAsynchronousJavaScript() {
		StringBuilder result = new StringBuilder();
		result.append("<script type=\"text/javascript\">");
		result.append("var _gaq = _gaq || [];");
		result.append("_gaq.push(['_setAccount', '" + getPluginBean().getValue() + "']);");
		result.append("_gaq.push(['_trackPageview']);");
		result.append("(function() {");
		result.append("  var ga = document.createElement('script');");
		result.append("  ga.src = ('https:' == document.location.protocol ? 'https://ssl' : ");
		result.append("  'http://www') + '.google-analytics.com/ga.js';");
		result.append("  ga.setAttribute('async', 'true');");
		result.append("  document.documentElement.firstChild.appendChild(ga);");
		result.append("})();");
		result.append("</script>");
		return result.toString();
	}
}
