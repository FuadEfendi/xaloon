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

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.InputStreamContainerOptions;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;
import org.xaloon.wicket.component.test.MockedApplication;

/**
 * @author vytautas r.
 */
public class ThumbnailManagementPanelTest {
	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNoThumbnail() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);
		Mockito.when(app.getFileDescriptorDao().newFileDescriptor()).thenReturn(new JpaFileDescriptor());

		IModel<FileDescriptor> model = new Model<FileDescriptor>(null);
		tester.startComponentInPage(new ThumbnailManagementPanel("id", model, new InputStreamContainerOptions()));
		Assert.assertNull(tester.getTagByWicketId("thumnail"));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload"));

		List<FileDescriptor> files = new ArrayList<FileDescriptor>();
		FormTester form = tester.newFormTester("id:file-upload:file-upload-form");
		form.setValue("external-file-path", "http://test.com/test.jpg");

		// Submit ajax form
		tester.executeAjaxEvent("id:file-upload:file-upload-form:submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();

		Assert.assertNotNull(model.getObject());

		FileDescriptor fd = model.getObject();
		Assert.assertEquals("http://test.com/test.jpg", fd.getPath());
		Assert.assertEquals("http://test.com/test.jpg", fd.getName());
		Assert.assertNotNull(fd.getImageInputStreamContainer());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNoThumbnailNothingUploaded() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);
		Mockito.when(app.getFileDescriptorDao().newFileDescriptor()).thenReturn(new JpaFileDescriptor());

		IModel<FileDescriptor> model = new Model<FileDescriptor>(null);
		tester.startComponentInPage(new ThumbnailManagementPanel("id", model, new InputStreamContainerOptions()));
		Assert.assertNull(tester.getTagByWicketId("thumnail"));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload"));

		List<FileDescriptor> files = new ArrayList<FileDescriptor>();
		FormTester form = tester.newFormTester("id:file-upload:file-upload-form");

		// Submit ajax form
		tester.executeAjaxEvent("id:file-upload:file-upload-form:submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();

		Assert.assertNull(model.getObject());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelThumbnailProvided() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		FileDescriptor fileDescriptor = Mockito.mock(FileDescriptor.class);

		tester.startComponentInPage(new ThumbnailManagementPanel("id", new Model<FileDescriptor>(fileDescriptor), new InputStreamContainerOptions()));
		Assert.assertNotNull(tester.getTagByWicketId("thumnail"));
		Assert.assertNull(tester.getTagByWicketId("file-upload"));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelThumbnailProvidedClickGetComponentToRefresh() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		FileDescriptor fileDescriptor = Mockito.mock(FileDescriptor.class);

		tester.startComponentInPage(new ThumbnailManagementPanel("id", new Model<FileDescriptor>(fileDescriptor), new InputStreamContainerOptions()));
		Assert.assertNotNull(tester.getTagByWicketId("thumnail"));
		Assert.assertNull(tester.getTagByWicketId("file-upload"));
		tester.clickLink("id:thumnail:delete-image-link", true);
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelThumbnailProvidedNoOptions() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		FileDescriptor fileDescriptor = Mockito.mock(FileDescriptor.class);

		try {
			tester.startComponentInPage(new ThumbnailManagementPanel("id", new Model<FileDescriptor>(fileDescriptor), null));
			Assert.fail();
		} catch (IllegalArgumentException e) {
			Assert.assertEquals("thumnail options must be provided", e.getMessage());
		}
	}
}
