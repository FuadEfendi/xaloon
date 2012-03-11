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
package org.xaloon.core.api.classifier.search;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.search.SearchRequest;

/**
 * @author vytautas r.
 */
public class ClassifierItemSearchRequest extends SearchRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String classifierType;

	private String classifierItemCode;

	private String classifierItemName;

	private String parentClassifierItemCode;

	/**
	 * @return classifier type
	 */
	public String getClassifierType() {
		return classifierType;
	}

	/**
	 * @param classifierType
	 * @return current instance
	 */
	public ClassifierItemSearchRequest setClassifierType(String classifierType) {
		this.classifierType = classifierType;
		return this;
	}

	/**
	 * @return classifier item code
	 */
	public String getClassifierItemCode() {
		return classifierItemCode;
	}

	/**
	 * @param classifierItemCode
	 * @return current instance
	 */
	public ClassifierItemSearchRequest setClassifierItemCode(String classifierItemCode) {
		this.classifierItemCode = classifierItemCode;
		return this;
	}

	/**
	 * @return parent classifier item code
	 */
	public String getParentClassifierItemCode() {
		return parentClassifierItemCode;
	}

	/**
	 * @param parentClassifierItemCode
	 * @return current instance
	 */
	public ClassifierItemSearchRequest setParentClassifierItemCode(String parentClassifierItemCode) {
		this.parentClassifierItemCode = parentClassifierItemCode;
		return this;
	}

	/**
	 * @return true if no parameters are selected
	 */
	public boolean isParentSelection() {
		return StringUtils.isEmpty(parentClassifierItemCode) && StringUtils.isEmpty(classifierItemCode);
	}

	/**
	 * Gets classifierItemName.
	 * 
	 * @return classifierItemName
	 */
	public String getClassifierItemName() {
		return classifierItemName;
	}

	/**
	 * Sets classifierItemName.
	 * 
	 * @param classifierItemName
	 *            classifierItemName
	 */
	public void setClassifierItemName(String classifierItemName) {
		this.classifierItemName = classifierItemName;
	}
}
