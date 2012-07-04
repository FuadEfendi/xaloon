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
package org.xaloon.wicket.plugin.image.panel;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.xaloon.core.api.image.model.Image;

/**
 * @author vytautas r.
 */
public class NewImagePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean multiple = true;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public NewImagePanel(String id, IModel<org.xaloon.core.api.image.model.Image> model) {
		super(id, model);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize() {
		super.onInitialize();

		add(new SingleFileUploadPanel("upload-directly", (IModel<org.xaloon.core.api.image.model.Image>)getDefaultModel()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onFileUpload(Image temporaryImage) {
				onImageUpload(temporaryImage);
			}

			@Override
			protected Component getComponentToRefresh() {
				return getComponentToRefreshOnClose();
			}
		}.setMultiple(multiple));
	}

	protected Component getComponentToRefreshOnClose() {
		return NewImagePanel.this;
	}

	protected void onImageUpload(org.xaloon.core.api.image.model.Image imageUploaded) {
	}

	/**
	 * Gets multiple.
	 * 
	 * @return multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * Sets multiple.
	 * 
	 * @param multiple
	 *            multiple
	 * @return current instance
	 */
	public NewImagePanel setMultiple(boolean multiple) {
		this.multiple = multiple;
		return this;
	}
}
