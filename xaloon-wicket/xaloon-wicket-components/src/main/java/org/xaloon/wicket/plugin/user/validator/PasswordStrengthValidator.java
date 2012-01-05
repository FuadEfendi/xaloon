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

import org.apache.wicket.validation.validator.PatternValidator;

/**
 * Kindly borrowed from http://www.mkyong.com/regular-expressions/how-to-validate-password-with-regular-expression/
 * 
 * @author vytautas r.
 */
public class PasswordStrengthValidator extends PatternValidator {
	private static final long serialVersionUID = 1L;

	/** singleton instance */
	private static final PasswordStrengthValidator INSTANCE = new PasswordStrengthValidator();

	/**
	 * Retrieves the singleton instance of <code>EmailAddressValidator</code>.
	 * 
	 * @return the singleton instance of <code>EmailAddressValidator</code>
	 */
	public static PasswordStrengthValidator getInstance() {
		return INSTANCE;
	}

	/**
	 * Protected constructor to force use of static singleton accessor. Override this constructor to implement resourceKey(Component).
	 */
	protected PasswordStrengthValidator() {
		super("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})");
	}
}
