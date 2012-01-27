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
package org.xaloon.wicket.component.custom;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class ConfirmationAjaxLink<T> extends AjaxLink<T> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Provided key to get default message while deleting entry
	 */
	private static final String DELETE_CONFIRMATION = "DELETE_CONFIRMATION";

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public ConfirmationAjaxLink(String id) {
		super(id);
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		return new AjaxCallDecorator() {
			private static final long serialVersionUID = 1L;

			@Override
			public CharSequence decorateScript(Component c, CharSequence script) {
				return "if(!confirm('" + getString(DELETE_CONFIRMATION) + "')) return false;" + script;
			}
		};
	}
}
