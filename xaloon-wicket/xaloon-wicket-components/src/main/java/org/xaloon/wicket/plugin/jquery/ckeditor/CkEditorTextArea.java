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

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;

/**
 * @author vytautas r.
 * @param <T>
 */
public class CkEditorTextArea<T> extends TextArea<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected CkEditorBehavior behavior;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public CkEditorTextArea(String id) {
		this(id, new CkEditorOptions());
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param options
	 */
	public CkEditorTextArea(String id, CkEditorOptions options) {
		super(id);
		add(behavior = newBehavior(options));
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public CkEditorTextArea(String id, IModel<T> model) {
		this(id, new CkEditorOptions(), model);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param options
	 * @param model
	 */
	public CkEditorTextArea(String id, CkEditorOptions options, IModel<T> model) {
		super(id, model);
		add(behavior = newBehavior(options));
	}

	protected CkEditorBehavior newBehavior(CkEditorOptions options) {
		return new CkEditorBehavior(options);
	}


}
