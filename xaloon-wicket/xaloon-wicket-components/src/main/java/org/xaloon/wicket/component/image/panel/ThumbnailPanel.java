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
package org.xaloon.wicket.component.image.panel;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.image.NonCachingImage;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.util.HtmlElementEnum;
import org.xaloon.wicket.component.custom.ConfirmationAjaxLink;

/**
 * @author vytautas r.
 */
public class ThumbnailPanel extends GenericPanel<FileDescriptor> {
	private static final long serialVersionUID = 1L;

	private int width = 200;

	private int height = 100;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public ThumbnailPanel(String id, IModel<FileDescriptor> model) {
		super(id, model);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		FileDescriptor fileDescriptor = getModelObject();

		FileDescriptorResource fileDescriptorResource = new FileDescriptorResource(fileDescriptor);

		Image temporaryImage = new NonCachingImage("image", fileDescriptorResource);
		temporaryImage.add(AttributeModifier.replace(HtmlElementEnum.WIDTH.value(), String.valueOf(width)));
		temporaryImage.add(AttributeModifier.replace(HtmlElementEnum.HEIGHT.value(), String.valueOf(height)));

		addOrReplace(temporaryImage);

		// Add delete image link
		addOrReplace(new ConfirmationAjaxLink<Void>("delete-image-link") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				ThumbnailPanel.this.setModelObject(null);
				Component componentToRefresh = getComponentToRefresh();
				if (componentToRefresh != null) {
					target.add(componentToRefresh);
				}
			}
		});
	}

	/**
	 * Which component to refresh after thumbnail is deleted
	 * 
	 * @return component instance to refresh via ajax
	 */
	protected Component getComponentToRefresh() {
		return null;
	}

	/**
	 * Sets width.
	 * 
	 * @param width
	 *            width
	 * @return this instance
	 */
	public ThumbnailPanel setWidth(int width) {
		this.width = width;
		return this;
	}

	/**
	 * Sets height.
	 * 
	 * @param height
	 *            height
	 * @return this instance
	 */
	public ThumbnailPanel setHeight(int height) {
		this.height = height;
		return this;
	}


}
