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

import org.apache.wicket.Component;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.wicket.component.test.MockedApplication;

/**
 * @author vytautas r.
 */
public class FileUploadPanelTest {
	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelExternal() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		List<FileDescriptor> files = new ArrayList<FileDescriptor>();

		tester.startComponentInPage(new FileUploadPanel("id", new ListModel<FileDescriptor>(files)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Component getComponentToRefresh() {
				return null;
			}
		});
		Assert.assertNotNull(tester.getTagByWicketId("file-upload-form"));
		Assert.assertNotNull(tester.getTagByWicketId("external-file-path"));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload-field"));

		FormTester form = tester.newFormTester("id:file-upload-form");
		form.setValue("external-file-path", "http://test.com/test.jpg");

		// Submit ajax form
		tester.executeAjaxEvent("id:file-upload-form:submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();

		Assert.assertEquals(1, files.size());
		FileDescriptor fd = files.get(0);
		Assert.assertEquals("http://test.com/test.jpg", fd.getPath());
		Assert.assertEquals("http://test.com/test.jpg", fd.getName());
		Assert.assertNotNull(fd.getImageInputStreamContainer());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelInternalMultiple() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		List<FileDescriptor> files = new ArrayList<FileDescriptor>();

		tester.startComponentInPage(new FileUploadPanel("id", new ListModel<FileDescriptor>(files)).setMultiple(true));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload-form"));
		Assert.assertNotNull(tester.getTagByWicketId("external-file-path"));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload-field"));


		File file = new File(this.getClass().getResource("test.properties").toURI());

		FormTester form = tester.newFormTester("id:file-upload-form");
		form.setFile("file-upload-field", file, "text/plain");

		// Submit ajax form
		tester.executeAjaxEvent("id:file-upload-form:submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();

		Assert.assertEquals(1, files.size());
		FileDescriptor fd = files.get(0);
		Assert.assertEquals("test.properties", fd.getPath());
		Assert.assertEquals("test.properties", fd.getName());
		Assert.assertEquals(Long.valueOf(4), fd.getSize());
		Assert.assertEquals("text/plain", fd.getMimeType());
		Assert.assertNotNull(fd.getImageInputStreamContainer());

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelNoFile() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		List<FileDescriptor> files = new ArrayList<FileDescriptor>();

		tester.startComponentInPage(new FileUploadPanel("id", new ListModel<FileDescriptor>(files)).setMultiple(true));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload-form"));
		Assert.assertNotNull(tester.getTagByWicketId("external-file-path"));
		Assert.assertNotNull(tester.getTagByWicketId("file-upload-field"));


		File file = new File(this.getClass().getResource("test.properties").toURI());

		FormTester form = tester.newFormTester("id:file-upload-form");

		// Submit ajax form
		tester.executeAjaxEvent("id:file-upload-form:submit", "onclick");

		// Validate result
		tester.assertNoErrorMessage();

		Assert.assertEquals(0, files.size());
	}
}
