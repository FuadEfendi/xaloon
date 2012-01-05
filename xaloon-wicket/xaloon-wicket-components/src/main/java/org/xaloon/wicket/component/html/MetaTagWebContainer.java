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
package org.xaloon.wicket.component.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.IModel;
import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * Simple meta tag markup container
 * 
 * <p>
 * Java method:
 * 
 * <pre>
 * addOrReplace(new MetaTagWebContainer(new Model&lt;KeyValue&lt;String, String&gt;&gt;(new KeyValue&lt;String, String&gt;(&quot;keywords&quot;, &quot;keyword1, keyword2&quot;))));
 * </pre>
 * 
 * HTML:
 * 
 * <pre>
 * &lt;meta wicket:id="keywords" name="keywords" content="test"&gt;
 * </pre>
 * 
 * @author vytautas r.
 * @since 1.5
 * 
 */
public class MetaTagWebContainer extends WebMarkupContainer {
	private static final long serialVersionUID = 1L;


	/**
	 * HTML tag "name"
	 */
	private static final String NAME = "name";

	/**
	 * HTML tag "content"
	 */
	private static final String CONTENT = "content";

	/**
	 * Construct.
	 * 
	 * @param model
	 */
	public MetaTagWebContainer(IModel<KeyValue<String, String>> model) {
		super(model.getObject().getKey(), model);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public MetaTagWebContainer(String id) {
		super(id);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		KeyValue<String, String> tagInformation = (KeyValue<String, String>)getDefaultModelObject();
		if (!tagInformation.isEmpty()) {
			tag.addBehavior(AttributeModifier.replace(CONTENT, tagInformation.getValue()));
			tag.addBehavior(AttributeModifier.replace(NAME, tagInformation.getKey()));
		}
	}
}
