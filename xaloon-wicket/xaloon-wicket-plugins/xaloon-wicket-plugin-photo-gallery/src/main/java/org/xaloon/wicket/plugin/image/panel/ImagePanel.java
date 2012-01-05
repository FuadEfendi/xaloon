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

import javax.inject.Inject;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.util.HtmlElementEnum;
import org.xaloon.wicket.component.resource.ImageLink;

/**
 * @author vytautas r.
 */
public class ImagePanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ImagePanel.class);

	private int imageWidth = 200;

	private int imageHeight = 100;

	@Inject
	private FileRepositoryFacade fileRepositoryFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public ImagePanel(String id, IModel<org.xaloon.core.api.image.model.Image> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		final org.xaloon.core.api.image.model.Image image = (org.xaloon.core.api.image.model.Image)getDefaultModelObject();


		// Add show existing image
		ImageLink imageLink = new ImageLink("display-image", (image.getPath() != null) ? image.getPath() : null);
		imageLink.setWidth(imageWidth);
		imageLink.setHeight(imageHeight);
		add(imageLink);

		// Add show temporary image
		TemporaryResource temporaryResource = new TemporaryResource((image.getThumbnail() != null) ? image.getThumbnail() : image);
		Image temporaryImage = new Image("temporary-image", temporaryResource);
		temporaryImage.add(AttributeModifier.replace(HtmlElementEnum.WIDTH.value(), String.valueOf(imageWidth)));
		temporaryImage.add(AttributeModifier.replace(HtmlElementEnum.HEIGHT.value(), String.valueOf(imageHeight)));
		temporaryImage.setVisible(!temporaryResource.isEmpty());
		add(temporaryImage);

		// Add delete image link
		add(new AjaxLink<Void>("delete-image-link") {
			private static final long serialVersionUID = 1L;

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new AjaxCallDecorator() {
					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence decorateScript(Component c, CharSequence script) {
						return "if(!confirm('Are you sure you want to delete this item?')) return false;" + script;
					}
				};
			}

			@Override
			public void onClick(AjaxRequestTarget target) {
				deleteFileDescriptor(image);
				Component componentToRefresh = getOnCloseRefreshComponent();
				if (componentToRefresh != null) {
					target.add(componentToRefresh);
				}
			}
		});
	}

	protected Component getOnCloseRefreshComponent() {
		return ImagePanel.this;
	}


	/**
	 * Delete file descriptor and file
	 * 
	 * @param imageToDelete
	 */
	protected void deleteFileDescriptor(org.xaloon.core.api.image.model.Image imageToDelete) {

	}

	/**
	 * @param imageWidth
	 */
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}

	/**
	 * @param imageHeight
	 */
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}
