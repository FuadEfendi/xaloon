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
package org.xaloon.wicket.plugin.jquery.ckeditor;

import org.apache.wicket.Component;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.odlabs.wiquery.core.behavior.WiQueryAbstractAjaxBehavior;


/**
 * @author vytautas r.
 */
public class CkEditorBehavior extends WiQueryAbstractAjaxBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ResourceReference baseRef = new PackageResourceReference(CkEditorOptions.class, "");

	/**
	 * Construct.
	 * 
	 * @param opt
	 */
	public CkEditorBehavior(CkEditorOptions opt) {
		options = opt.getOptions();
	}

	@Override
	public void renderHead(Component arg0, IHeaderResponse response) {
		super.renderHead(arg0, response);
		CharSequence baseUrl = RequestCycle.get().urlFor(baseRef, null);

		String ckeditor = "function CKEDITOR_GETURL(resource){\n"
			+ "return resource.indexOf(CKEDITOR_BASEPATH) >= 0 ? resource : CKEDITOR_BASEPATH + resource;\n" + "}";
		response.render(JavaScriptHeaderItem.forScript(String.format("var CKEDITOR_BASEPATH = '%s/';\n" + ckeditor, baseUrl), null));


		response.render(JavaScriptHeaderItem.forReference(CkEditorOptions.JS_CKEDITOR_RESOURCE));
		response.render(JavaScriptHeaderItem.forReference(CkEditorOptions.JS_CKEDITOR_CONFIG));
		response.render(JavaScriptHeaderItem.forReference(CkEditorOptions.JS_CKEDITOR_STYLE));
	}
}
