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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.image.UploadedFileDescriptor;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.InputStreamContainerOptions;
import org.xaloon.core.api.storage.UrlInputStreamContainer;
import org.xaloon.wicket.component.resource.WicketInputStreamContainer;

/**
 * @author vytautas r.
 */
public class FileUploadPanel extends GenericPanel<List<FileDescriptor>> {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadPanel.class);

	private boolean multiple;

	private String externalFilePath;

	private InputStreamContainerOptions options;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public FileUploadPanel(String id, IModel<List<FileDescriptor>> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();

		// Add the form
		Form<Void> fileUploadForm = new Form<Void>("file-upload-form");
		fileUploadForm.setMultiPart(true);
		add(fileUploadForm);

		// Add external file upload support
		fileUploadForm.add(new TextField<String>("external-file-path", new PropertyModel<String>(this, "externalFilePath")));

		// Add local file(s) upload
		final FileUploadField fileUploadField = new FileUploadField("file-upload-field", new ListModel<FileUpload>(new ArrayList<FileUpload>())) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				if (multiple) {
					tag.put("multiple", "multiple");
				}
			}
		};
		fileUploadForm.add(fileUploadField);

		// Add submit
		fileUploadForm.add(new AjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				if (fileUploadField.getFileUploads() != null) {
					onUploadLocalFiles(fileUploadField.getFileUploads());
				} else if (!StringUtils.isEmpty(getExternalFilePath())) {
					onUploadExternalFile(getExternalFilePath());
				}
				onAfterFileUpload(FileUploadPanel.this.getModelObject());
				Component componentToRefresh = getComponentToRefresh();
				if (componentToRefresh != null) {
					target.add(componentToRefresh);
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});
	}

	protected FileDescriptor newFileDescriptor() {
		return new UploadedFileDescriptor();
	}

	protected void onAfterFileUpload(List<FileDescriptor> items) {
	}

	/**
	 * Which component to refresh after file upload is complete
	 * 
	 * @return component instance to refresh via ajax
	 */
	protected Component getComponentToRefresh() {
		return this;
	}

	protected void onUploadExternalFile(String externalFilePath) {
		List<FileDescriptor> targetList = getModelObject();
		targetList.add(createFromExternalFile(externalFilePath));
	}

	protected void onUploadLocalFiles(List<FileUpload> fileUploads) {
		List<FileDescriptor> targetList = getModelObject();
		for (FileUpload fileUpload : fileUploads) {
			targetList.add(createFromFileUpload(fileUpload));
		}
	}

	private FileDescriptor createFromFileUpload(FileUpload fileUpload) {
		FileDescriptor result = newFileDescriptor();
		result.setImageInputStreamContainer(new WicketInputStreamContainer(fileUpload, getInputStreamContainerOptions()));
		result.setMimeType(fileUpload.getContentType());
		result.setName(fileUpload.getClientFileName());
		result.setPath(fileUpload.getClientFileName());
		result.setSize(fileUpload.getSize());
		return result;
	}

	private FileDescriptor createFromExternalFile(String externalFilePath) {
		FileDescriptor result = newFileDescriptor();
		result.setImageInputStreamContainer(new UrlInputStreamContainer(externalFilePath, getInputStreamContainerOptions()));
		result.setName(externalFilePath);
		result.setPath(externalFilePath);
		return result;
	}

	protected InputStreamContainerOptions getInputStreamContainerOptions() {
		if (options == null) {
			options = new InputStreamContainerOptions();
		}
		return options;
	}

	/**
	 * Gets externalFilePath.
	 * 
	 * @return externalFilePath
	 */
	public String getExternalFilePath() {
		return externalFilePath;
	}

	/**
	 * Sets externalFilePath.
	 * 
	 * @param externalFilePath
	 *            externalFilePath
	 */
	public void setExternalFilePath(String externalFilePath) {
		this.externalFilePath = externalFilePath;
	}

	/**
	 * Sets multiple.
	 * 
	 * @param multiple
	 *            multiple
	 * @return this instance
	 */
	public FileUploadPanel setMultiple(boolean multiple) {
		this.multiple = multiple;
		return this;
	}
}
