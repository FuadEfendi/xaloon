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

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

/**
 * @author vytautas r.
 */
public abstract class CustomModalWindow extends ModalWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String MODEL_WINDOW_TITLE = "MODEL_WINDOW_TITLE";

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public CustomModalWindow(String id) {
		this(id, MODEL_WINDOW_TITLE);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param titleKey
	 */
	public CustomModalWindow(String id, String titleKey) {
		super(id);
		// setCookieName(id + "4");
		// setTitle(getString(titleKey));
		setInitialWidth(800);
		// setInitialHeight(400);

		setCloseButtonCallback(new ModalWindow.CloseButtonCallback() {
			private static final long serialVersionUID = 1L;

			public boolean onCloseButtonClicked(AjaxRequestTarget target) {
				return true;
			}
		});

		setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
			private static final long serialVersionUID = 1L;

			public void onClose(AjaxRequestTarget target) {
				Component component = getOnCloseComponent();
				if (component != null) {
					target.add(component);
				}
			}
		});
	}

	/**
	 * Target component on modal window close
	 * 
	 * @return target component which will be added to {@link AjaxRequestTarget}
	 */
	protected abstract Component getOnCloseComponent();
}
