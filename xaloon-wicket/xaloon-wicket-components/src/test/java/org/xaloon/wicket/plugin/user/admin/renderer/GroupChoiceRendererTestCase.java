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
package org.xaloon.wicket.plugin.user.admin.renderer;

import org.junit.Assert;
import org.junit.Test;
import org.xaloon.core.api.security.SecurityGroup;
import org.xaloon.core.jpa.security.model.JpaGroup;

/**
 * @author vytautas r.
 */
public class GroupChoiceRendererTestCase {
	/**
	 * @throws Exception
	 */
	@Test
	public void testChoice() throws Exception {
		SecurityGroup group = new JpaGroup();
		group.setName("test");
		group.setId(1L);
		GroupChoiceRenderer choiceRenderer = new GroupChoiceRenderer();
		Assert.assertEquals("test", choiceRenderer.getDisplayValue(group));
		Assert.assertEquals("1", choiceRenderer.getIdValue(group, 0));
	}
}
