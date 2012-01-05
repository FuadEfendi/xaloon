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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * Whole combination is means, 4 to 15 characters with any lower case character, digit or special symbol “_-” only. This is common username pattern
 * that’s widely use in different websites.
 * 
 * @author vytautas r.
 */
public class UsernamePatternValidator extends AbstractValidator<String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String USERNAME_PATTERN = "^[a-z0-9_-]{4,15}$";

	private static final String USERNAME_PATTERN_NOT_VALID = "USERNAME_PATTERN_NOT_VALID";

	private Pattern pattern;

	/**
	 * Construct.
	 */
	public UsernamePatternValidator() {
		pattern = Pattern.compile(USERNAME_PATTERN);
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {
		Matcher matcher = pattern.matcher(validatable.getValue());
		if (!matcher.matches()) {
			ValidationError error = new ValidationError();
			error.addMessageKey(USERNAME_PATTERN_NOT_VALID);
			validatable.error(error);
		}
	}

}
