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

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.xaloon.core.api.user.UserFacade;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class EmailUsageValidator extends AbstractValidator<String> {
	private static final long serialVersionUID = 1L;

	private static final String EMAIL_IN_USE = "EMAIL_IN_USE";

	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	private String oldEmail;

	private boolean persisted;

	/**
	 * Construct.
	 * 
	 * @param persisted
	 * @param oldEmail
	 */
	public EmailUsageValidator(boolean persisted, String oldEmail) {
		Injector.get().inject(this);
		this.oldEmail = oldEmail;
		this.persisted = persisted;
	}

	@Override
	protected void onValidate(IValidatable<String> validatable) {
		if (StringUtils.isEmpty(oldEmail) || !persisted) {
			processDefaultValidation(validatable);
		} else if (!oldEmail.equals(validatable.getValue())) {
			processDefaultValidation(validatable);
		}
	}

	private void processDefaultValidation(IValidatable<String> validatable) {
		if (userFacade.getUserByEmail(validatable.getValue()) != null) {
			ValidationError error = new ValidationError();
			error.addMessageKey(EMAIL_IN_USE);
			validatable.error(error);
		}
	}

}
