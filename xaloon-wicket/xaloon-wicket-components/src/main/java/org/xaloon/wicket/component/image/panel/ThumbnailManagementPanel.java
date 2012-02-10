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

import javax.inject.Inject;

import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.ListModel;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileDescriptorDao;
import org.xaloon.core.api.storage.InputStreamContainerOptions;

/**
 * @author vytautas r.
 */
public class ThumbnailManagementPanel extends GenericPanel<FileDescriptor> {
	private static final long serialVersionUID = 1L;

	@Inject
	private FileDescriptorDao fileDescriptorDao;

	InputStreamContainerOptions options;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 * @param options
	 */
	public ThumbnailManagementPanel(String id, IModel<FileDescriptor> model, InputStreamContainerOptions options) {
		super(id, model);
		if (options == null) {
			throw new IllegalArgumentException("thumnail options must be provided");
		}
		this.options = options;
		setOutputMarkupId(true);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		FileDescriptor fileDescriptor = getModelObject();

		boolean thumbnailEmpty = fileDescriptor == null;

		// Add thumbnail display panel. disable visibility if there is a thumbnail already
		ThumbnailPanel thumbnailPanel = new ThumbnailPanel("thumnail", getModel()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected org.apache.wicket.Component getComponentToRefresh() {
				return ThumbnailManagementPanel.this;
			};
		};
		thumbnailPanel.setWidth(options.getWidth()).setHeight(options.getHeight());
		thumbnailPanel.setVisible(!thumbnailEmpty);
		addOrReplace(thumbnailPanel);

		// Add file upload panel

		List<FileDescriptor> files = new ArrayList<FileDescriptor>();
		if (fileDescriptor != null) {
			files.add(fileDescriptor);
		}
		ListModel<FileDescriptor> filesModel = new ListModel<FileDescriptor>(files);
		addOrReplace(new FileUploadPanel("file-upload", filesModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected FileDescriptor newFileDescriptor() {
				return fileDescriptorDao.newFileDescriptor();
			};

			@Override
			protected org.apache.wicket.Component getComponentToRefresh() {
				return ThumbnailManagementPanel.this;
			};

			@Override
			protected InputStreamContainerOptions getInputStreamContainerOptions() {
				return options;
			};

			@Override
			protected void onAfterFileUpload(List<FileDescriptor> items) {
				if (!items.isEmpty()) {
					ThumbnailManagementPanel.this.setModelObject(items.get(0));
				}
			};
		}.setMultiple(false).setVisible(thumbnailEmpty));
	}
}
