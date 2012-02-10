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

import org.apache.wicket.util.tester.WicketTester;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xaloon.core.jpa.storage.model.JpaFileDescriptor;
import org.xaloon.wicket.component.test.MockedApplication;

/**
 * @author vytautas r.
 */
public class FileDescriptorResourceTest {

	/**
	 * 
	 */
	@Before
	public void init() {
		MockedApplication app = new MockedApplication();
		WicketTester tester = new WicketTester(app);
	}


	/**
	 * @throws Exception
	 */
	@Test
	public void testResource1() throws Exception {
		FileDescriptorResource resource = new FileDescriptorResource(null);
		Assert.assertTrue(resource.isEmpty());
		Assert.assertNull(resource.getImageData(null));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testResource2() throws Exception {
		FileDescriptorResource resource = new FileDescriptorResource(new JpaFileDescriptor());
		Assert.assertFalse(resource.isEmpty());
		Assert.assertNull(resource.getImageData(null));
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void testResource3() throws Exception {
		JpaFileDescriptor fd = new JpaFileDescriptor();
		fd.setPath("http://www.test.com");
		FileDescriptorResource resource = new FileDescriptorResource(fd);
		Assert.assertFalse(resource.isEmpty());
	}
}
