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
package org.xaloon.wicket.plugin.image.galleria.panel;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.image.galleria.GalleriaBehavior;
import org.xaloon.wicket.plugin.image.galleria.GalleriaOptions;
import org.xaloon.wicket.plugin.image.plugin.GalleryPlugin;
import org.xaloon.wicket.plugin.image.plugin.GalleryPluginBean;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class GalleriaImagesPanel extends
		AbstractPluginPanel<GalleryPluginBean, GalleryPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param images
	 */
	public GalleriaImagesPanel(String id, List<ImageComposition> images) {
		super(id, Model.of(images));

	}

	@Override
	@SuppressWarnings("unchecked")
	protected void onInitialize(GalleryPlugin plugin,
			GalleryPluginBean pluginBean) {
		// Add Javascript Galleria support.
		WebMarkupContainer galleria = new WebMarkupContainer("galleria");
		galleria.add(new GalleriaBehavior(new GalleriaOptions().height(pluginBean.getHeight())));
		add(galleria);
		
		// Add images
		ListView<ImageComposition> imagesView = new ListView<ImageComposition>(
				"images",
				(IModel<? extends List<? extends ImageComposition>>) getDefaultModel()) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ImageComposition> item) {
				Image image = item.getModelObject().getImage();

				WebMarkupContainer container = new WebMarkupContainer("link");
				String url = UrlUtils.toAbsoluteImagePath(
						ImageLink.IMAGE_RESOURCE, image.getPath());
				container.add(AttributeModifier.replace("href", url));
				item.add(container);
				ImageLink imageLink = new ImageLink("image",
						(image.getThumbnail() != null) ? image.getThumbnail()
								.getPath() : null);
				imageLink.setAlternativeText(image.getDescription());
				imageLink.setTitle(image.getTitle());
				container.add(imageLink);
			}
		};
		galleria.add(imagesView);
	}
}
