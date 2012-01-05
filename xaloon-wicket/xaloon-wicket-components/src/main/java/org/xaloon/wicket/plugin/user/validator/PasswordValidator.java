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

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class PasswordValidator extends AbstractValidator<String> {
	private static final long serialVersionUID = 1L;

	private static final String PASSWORDS_DO_NOT_MATCH = "PASSWORDS_DO_NOT_MATCH";

	private final TextField<String> passwordField;

	/**
	 * Construct.
	 * 
	 * @param passwordField
	 */
	public PasswordValidator(TextField<String> passwordField) {
		this.passwordField = passwordField;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {
		String password_value = passwordField.getRawInput();
		String repeat_password_value = validatable.getValue();
		if (StringUtils.isEmpty(password_value) || StringUtils.isEmpty(repeat_password_value) || !password_value.equals(repeat_password_value)) {
			ValidationError error = new ValidationError();
			error.addMessageKey(PASSWORDS_DO_NOT_MATCH);
			validatable.error(error);
		}
	}
}
