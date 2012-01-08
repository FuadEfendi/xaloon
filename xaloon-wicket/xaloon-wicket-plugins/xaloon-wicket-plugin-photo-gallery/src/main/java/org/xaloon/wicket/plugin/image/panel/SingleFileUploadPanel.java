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

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.xaloon.core.api.image.AlbumFacade;
import org.xaloon.core.api.image.model.Image;
import org.xaloon.core.api.storage.UrlInputStreamContainer;
import org.xaloon.wicket.component.resource.WicketInputStreamContainer;

/**
 * @author vytautas r.
 */
public class SingleFileUploadPanel extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private AlbumFacade albumFacade;

	private ModalWindow window;

	private boolean multiple = true;


	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public SingleFileUploadPanel(String id, IModel<org.xaloon.core.api.image.model.Image> model) {
		super(id, model);
		add(new SingleFileUploadForm("file-upload-form", new CompoundPropertyModel<org.xaloon.core.api.image.model.Image>(model)));
	}

	/**
	 * Construct.
	 * 
	 * @param model
	 * @param window
	 */
	public SingleFileUploadPanel(ModalWindow window, IModel<org.xaloon.core.api.image.model.Image> model) {
		super(window.getContentId(), model);
		this.window = window;
		add(new SingleFileUploadForm("file-upload-form", new CompoundPropertyModel<org.xaloon.core.api.image.model.Image>(model)));
	}

	class SingleFileUploadForm extends Form<org.xaloon.core.api.image.model.Image> {
		private static final long serialVersionUID = 1L;

		private FileUploadField fileUploadField;

		private List<FileUpload> fileUploads = new ArrayList<FileUpload>();

		public SingleFileUploadForm(String id, IModel<org.xaloon.core.api.image.model.Image> model) {
			super(id, model);
			setMultiPart(true);
		}

		@Override
		protected void onInitialize() {
			super.onInitialize();
			// Add external file support
			add(new TextField<String>("path"));

			// Add file uploading field
			fileUploadField = new FileUploadField("file-upload-field", new ListModel<FileUpload>(fileUploads)) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onComponentTag(ComponentTag tag) {
					super.onComponentTag(tag);
					if (multiple) {
						tag.put("multiple", "multiple");
					}
				}
			};
			add(fileUploadField);

			// Add submit
			add(new AjaxButton("submit", this) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					org.xaloon.core.api.image.model.Image temporaryImage = SingleFileUploadForm.this.getModelObject();
					if (fileUploadField.getFileUploads() != null) {
						for (FileUpload fu : fileUploadField.getFileUploads()) {
							Image image = albumFacade.newImage();
							fillImageProperties(fu, image);
							onFileUpload(image);
						}
					} else if (!StringUtils.isEmpty(temporaryImage.getPath())) {
						fillImageProperties(temporaryImage);
						onFileUpload(temporaryImage);
					}

					// Check if form is in modal window. if yes - close it
					if (window != null) {
						window.close(target);
					} else {
						target.add(getComponentToRefresh());
					}
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {

				}
			});
		}


		protected void fillImageProperties(Image temporaryImage) {
			temporaryImage.setImageInputStreamContainer(new UrlInputStreamContainer(temporaryImage.getPath()));
			temporaryImage.setName(temporaryImage.getPath());
		}

		private void fillImageProperties(FileUpload fileUpload, org.xaloon.core.api.image.model.Image temporaryImage) {
			temporaryImage.setImageInputStreamContainer(new WicketInputStreamContainer(fileUpload));
			temporaryImage.setMimeType(fileUpload.getContentType());
			temporaryImage.setName(fileUpload.getClientFileName());
			temporaryImage.setPath(fileUpload.getClientFileName());
			temporaryImage.setSize(fileUpload.getSize());
		}

		/**
		 * Gets fileUploads.
		 * 
		 * @return fileUploads
		 */
		public List<FileUpload> getFileUploads() {
			return fileUploads;
		}

		/**
		 * Sets fileUploads.
		 * 
		 * @param fileUploads
		 *            fileUploads
		 */
		public void setFileUploads(List<FileUpload> fileUploads) {
			this.fileUploads = fileUploads;
		}
	}

	protected void onFileUpload(org.xaloon.core.api.image.model.Image temporaryImage) {
	}

	protected Component getComponentToRefresh() {
		return SingleFileUploadPanel.this;
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
	public SingleFileUploadPanel setMultiple(boolean multiple) {
		this.multiple = multiple;
		return this;
	}
}
