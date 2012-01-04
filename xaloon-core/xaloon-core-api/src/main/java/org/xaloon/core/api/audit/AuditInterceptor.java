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
package org.xaloon.core.api.audit;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.audit.annotation.Audited;
import org.xaloon.core.api.interceptor.AbstractInterceptor;
import org.xaloon.core.api.interceptor.Signature;

/**
 * @author vytautas r.
 */
@Audited
@Interceptor
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class AuditInterceptor extends AbstractInterceptor<Audited> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AuditInterceptor.class);

	@Inject
	private AuditFacade auditFacade;

	@Override
	protected void beforeProceed(Signature signature, Audited annotation) {
		Object[] params = signature.getParameters();

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("Audit interceptor got class[%s], method [%s] with state [%s], parameter count [%d], first parameter [%s]",
				signature.getDeclaringTypeName(), signature.getMethodName(), annotation.state(), params.length, (params.length > 0) ? params[0] : ""));
		}
		if (params != null && params.length > 0) {
			auditFacade.audit(annotation.state(), params[0]);
		}
	}

	@Override
	protected Class<Audited> getAnnotationClass() {
		return Audited.class;
	}
}
