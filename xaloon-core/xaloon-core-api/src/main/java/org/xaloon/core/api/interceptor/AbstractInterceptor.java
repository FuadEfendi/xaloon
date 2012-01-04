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
package org.xaloon.core.api.interceptor;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.util.ClassUtil;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class AbstractInterceptor<T extends Annotation> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractInterceptor.class);

	/**
	 * @param ic
	 * @return
	 * @throws Exception
	 */
	@AroundInvoke
	public Object aroundInvoke(InvocationContext ic) throws Exception {
		Method method = ic.getMethod();
		if (method.getDeclaringClass().isInterface()) {
			method = ClassUtil.getMethodIfAvailable(ic.getTarget().getClass(), method.getName(), method.getParameterTypes());
		}
		if (method == null) {
			throw new RuntimeException("Method not found!");
		}
		Signature signature = new Signature().setMethodName(method.getName())
			.setDeclaringType(method.getDeclaringClass())
			.setDeclaringTypeName(method.getDeclaringClass().getName())
			.setParameters(ic.getParameters());
		try {
			beforeProceed(signature, method.getAnnotation(getAnnotationClass()));
		} catch (Exception e) {
			LOGGER.error("Could not perform action.", e);
		}
		return ic.proceed();
	}

	/**
	 * @param pjp
	 * @return
	 * @throws Throwable
	 */
	public Object aroundInvokeAspectJ(ProceedingJoinPoint pjp) throws Throwable {
		org.aspectj.lang.Signature s = pjp.getSignature();
		final MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
		Method method = methodSignature.getMethod();
		if (method.getDeclaringClass().isInterface()) {
			method = ClassUtil.getMethodIfAvailable(pjp.getTarget().getClass(), method.getName(), method.getParameterTypes());
		}
		if (method == null) {
			throw new RuntimeException("Method not found!");
		}
		Signature signature = new Signature().setMethodName(s.getName())
			.setDeclaringType(s.getDeclaringType())
			.setDeclaringTypeName(s.getDeclaringTypeName())
			.setParameters(pjp.getArgs());
		beforeProceed(signature, method.getAnnotation(getAnnotationClass()));
		return pjp.proceed();
	}

	protected abstract Class<T> getAnnotationClass();

	protected abstract void beforeProceed(Signature signature, T annotation);
}
