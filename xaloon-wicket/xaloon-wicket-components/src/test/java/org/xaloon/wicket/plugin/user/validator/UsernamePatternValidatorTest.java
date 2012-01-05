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
package org.xaloon.wicket.plugin.user.validator;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.Validatable;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author vytautas r.
 */
public class UsernamePatternValidatorTest {
	private static UsernamePatternValidator usernameValidator;

	/**
	 * 
	 */
	@BeforeClass
	public static void initData() {
		usernameValidator = new UsernamePatternValidator();
	}


	/**
	 * @return list of valid usernames
	 */
	public String[] getValidUsernames() {
		return new String[] { "asweqg34", "asweqg_2002", "asweqg-2002", "mk3-4_asweqg" };
	}

	/**
	 * @return list of invalid usernames
	 */
	public String[] getInvalidUsernames() {
		return new String[] { "qwa", "mf@asweqg", "asweqg123456789_-", "aseD" };
	}

	/**
	 * 
	 */
	@Test
	public void testValidUsernameTest() {

		for (String temp : getValidUsernames()) {
			IValidatable<String> validatable = new Validatable<String>(temp);
			usernameValidator.onValidate(validatable);
			Assert.assertTrue(validatable.isValid());
		}

	}

	/**
	 * 
	 */
	@Test
	public void testInValidUsernameTest() {

		for (String temp : getInvalidUsernames()) {
			IValidatable<String> validatable = new Validatable<String>(temp);
			usernameValidator.onValidate(validatable);
			Assert.assertFalse(validatable.isValid());
		}

	}
}
