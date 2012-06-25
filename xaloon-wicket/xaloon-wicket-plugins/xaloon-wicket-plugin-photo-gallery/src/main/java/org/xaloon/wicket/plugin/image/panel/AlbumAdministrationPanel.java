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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.image.AlbumFacade;
import org.xaloon.core.api.image.ImageCompositionFactory;
import org.xaloon.core.api.image.model.ImageComposition;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.image.plugin.GalleryPlugin;
import org.xaloon.wicket.plugin.image.plugin.GalleryPluginBean;

/**
 * @author vytautas r.
 */
public class AlbumAdministrationPanel extends AbstractPluginPanel<GalleryPluginBean, GalleryPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxImagesAllowed = 999;
	
	// The list of images to be deleted
	private List<ImageComposition> imagesToDelete = new ArrayList<ImageComposition>();

	// The list of images to be persisted
	private List<ImageComposition> imagesToAdd = new ArrayList<ImageComposition>();

	private int imageThumbnailWidth = 200;

	private int imageThumbnailHeight = 100;

	private ImageCompositionFactory imageCompositionFactory;
	
	@Inject
	private AlbumFacade albumFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param albumImages
	 */
	public AlbumAdministrationPanel(String id, List<ImageComposition> albumImages) {
		super(id, Model.ofList(albumImages));
		setOutputMarkupId(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		final List<ImageComposition> images = (List<ImageComposition>)AlbumAdministrationPanel.this.getDefaultModelObject();
		ListView<ImageComposition> view = new ListView<ImageComposition>("images", new ArrayList<ImageComposition>(images)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ImageComposition> item) {
				ImagePanel imagePanel = new ImagePanel("single-image-panel", new Model<ImageComposition>(item.getModelObject())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void deleteFileDescriptor(ImageComposition imageToDelete) {
						images.remove(imageToDelete);
						// Delete only persisted images
						if (imageToDelete.getId() != null) {
							imagesToDelete.add(imageToDelete);
						}
						// Check if the same image was added and then deleted in the same transaction. we do not need to it to be persisted then.
						if (imagesToAdd.contains(imageToDelete)) {
							imagesToAdd.remove(imageToDelete);
						}
					}

					@Override
					protected Component getOnCloseRefreshComponent() {
						return AlbumAdministrationPanel.this;
					}
				};
				imagePanel.setImageWidth(imageThumbnailWidth);
				imagePanel.setImageHeight(imageThumbnailHeight);
				item.add(imagePanel);
			}
		}.setReuseItems(true);

		add(view);

		boolean visibleAddNewImageButtons = maxImagesAllowed < 1 || (maxImagesAllowed > 0 && images.size() < maxImagesAllowed);
		
		
		// Add link to upload new image
		addOrReplace(new NewImagePanel("image-upload", new Model<org.xaloon.core.api.image.model.Image>(albumFacade.newImage())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onImageUpload(org.xaloon.core.api.image.model.Image imageUploaded) {
				List<ImageComposition> images = (List<ImageComposition>)AlbumAdministrationPanel.this.getDefaultModelObject();
				
				ImageComposition imageComposition = imageCompositionFactory.newImageComposition(imageUploaded);
				
				// Add to be displayed
				images.add(imageComposition);

				// Add to be persisted
				imagesToAdd.add(imageComposition);
			}

			@Override
			protected Component getComponentToRefreshOnClose() {
				return AlbumAdministrationPanel.this;
			}
		}.setMultiple(maxImagesAllowed > 1).setVisible(visibleAddNewImageButtons));
	}

	/**
	 * actual deletion of these files should be made in the same transaction, so we just return the list of files which should be deleted
	 * 
	 * @return list of images to be deleted in single transaction
	 */
	public List<ImageComposition> getImagesToDelete() {
		return imagesToDelete;
	}

	/**
	 * Gets imagesToAdd.
	 * 
	 * @return imagesToAdd
	 */
	public List<ImageComposition> getImagesToAdd() {
		return imagesToAdd;
	}

	/**
	 * Sets maxImagesAllowed.
	 * 
	 * @param maxImagesAllowed
	 *            maxImagesAllowed
	 */
	public void setMaxImagesAllowed(int maxImagesAllowed) {
		this.maxImagesAllowed = maxImagesAllowed;
	}

	/**
	 * Gets imageThumbnailWidth.
	 * 
	 * @return imageThumbnailWidth
	 */
	public int getImageThumbnailWidth() {
		return imageThumbnailWidth;
	}

	/**
	 * Sets imageThumbnailWidth.
	 * 
	 * @param imageThumbnailWidth
	 *            imageThumbnailWidth
	 */
	public void setImageThumbnailWidth(int imageThumbnailWidth) {
		this.imageThumbnailWidth = imageThumbnailWidth;
	}

	/**
	 * Gets imageThumbnailHeight.
	 * 
	 * @return imageThumbnailHeight
	 */
	public int getImageThumbnailHeight() {
		return imageThumbnailHeight;
	}

	/**
	 * Sets imageThumbnailHeight.
	 * 
	 * @param imageThumbnailHeight
	 *            imageThumbnailHeight
	 */
	public void setImageThumbnailHeight(int imageThumbnailHeight) {
		this.imageThumbnailHeight = imageThumbnailHeight;
	}

	/**
	 * @param imageCompositionFactory the imageCompositionFactory to set
	 */
	public AlbumAdministrationPanel setImageCompositionFactory(ImageCompositionFactory imageCompositionFactory) {
		this.imageCompositionFactory = imageCompositionFactory;
		return this;
	}
	
	
}
