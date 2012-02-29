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

import org.apache.wicket.Component;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Test;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;
import org.xaloon.wicket.component.test.MockedApplication;

/**
 * @author vytautas r.
 */
public class ThumbnailPanelTest {
	/**
	 * @throws Exception
	 */
	@Test
	public void testPanel() throws Exception {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);

		Model<FileDescriptor> model = new Model<FileDescriptor>(new JpaFileDescriptor());
		Assert.assertNotNull(model.getObject());

		tester.startComponentInPage(new ThumbnailPanel("id", model));
		tester.assertNoErrorMessage();
		tester.clickLink("id:delete-image-link", true);
		Assert.assertNull(model.getObject());
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testPanelComponentToRefresh() throws Exception {
		MockedApplication app = new MockedApplication();
		final WicketTester tester = new WicketTester(app);

		Model<FileDescriptor> model = new Model<FileDescriptor>(new JpaFileDescriptor());
		Assert.assertNotNull(model.getObject());

		tester.startComponentInPage(new ThumbnailPanel("id", model) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Component getComponentToRefresh() {
				return this;
			}
		}.setWidth(10).setHeight(10).setOutputMarkupId(true));
		tester.assertNoErrorMessage();
		tester.clickLink("id:delete-image-link", true);
		Assert.assertNull(model.getObject());
	}
}
