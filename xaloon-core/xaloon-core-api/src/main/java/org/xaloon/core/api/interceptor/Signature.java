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

/**
 * @author vytautas r.
 */
public class Signature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String methodName;

	@SuppressWarnings("rawtypes")
	private Class declaringType;

	private String declaringTypeName;

	private Object[] parameters;

	/**
	 * Gets parameters.
	 * 
	 * @return parameters
	 */
	public Object[] getParameters() {
		return parameters;
	}

	/**
	 * Sets parameters.
	 * 
	 * @param parameters
	 *            parameters
	 * @return
	 */
	public Signature setParameters(Object[] parameters) {
		this.parameters = parameters;
		return this;
	}

	/**
	 * Gets declaringType.
	 * 
	 * @return declaringType
	 */
	@SuppressWarnings("rawtypes")
	public Class getDeclaringType() {
		return declaringType;
	}

	/**
	 * Sets declaringType.
	 * 
	 * @param declaringType
	 *            declaringType
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Signature setDeclaringType(Class declaringType) {
		this.declaringType = declaringType;
		return this;
	}

	/**
	 * Gets declaringTypeName.
	 * 
	 * @return declaringTypeName
	 */
	public String getDeclaringTypeName() {
		return declaringTypeName;
	}

	/**
	 * Sets declaringTypeName.
	 * 
	 * @param declaringTypeName
	 *            declaringTypeName
	 * @return
	 */
	public Signature setDeclaringTypeName(String declaringTypeName) {
		this.declaringTypeName = declaringTypeName;
		return this;
	}

	/**
	 * Gets methodName.
	 * 
	 * @return methodName
	 */
	public String getMethodName() {
		return methodName;
	}

	/**
	 * Sets methodName.
	 * 
	 * @param methodName
	 *            methodName
	 * @return
	 */
	public Signature setMethodName(String methodName) {
		this.methodName = methodName;
		return this;
	}
}
