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
package org.xaloon.wicket.component.classifier.panel;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.api.classifier.ClassifierItem;

/**
 * @author vytautas r.
 */
public class ClassifierItemOptions implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int maxItemCount = -1;

	private boolean multilevelClassifier;

	private String classifierType;

	private List<? extends ClassifierItem> destinationList;

	/**
	 * Gets multilevelClassifier.
	 * 
	 * @return multilevelClassifier
	 */
	public boolean isMultilevelClassifier() {
		return multilevelClassifier;
	}

	/**
	 * Sets multilevelClassifier.
	 * 
	 * @param multilevelClassifier
	 *            multilevelClassifier
	 * @return current instance
	 */
	public ClassifierItemOptions setMultilevelClassifier(boolean multilevelClassifier) {
		this.multilevelClassifier = multilevelClassifier;
		return this;
	}

	/**
	 * Gets classifierType.
	 * 
	 * @return classifierType
	 */
	public String getClassifierType() {
		return classifierType;
	}

	/**
	 * Sets classifierType.
	 * 
	 * @param classifierType
	 *            classifierType
	 * @return current instance
	 */
	public ClassifierItemOptions setClassifierType(String classifierType) {
		this.classifierType = classifierType;
		return this;
	}

	/**
	 * Gets destinationList.
	 * 
	 * @return destinationList
	 */
	public List<? extends ClassifierItem> getDestinationList() {
		return destinationList;
	}

	/**
	 * Sets destinationList.
	 * 
	 * @param destinationList
	 *            destinationList
	 * @return current instance
	 */
	public ClassifierItemOptions setDestinationList(List<? extends ClassifierItem> destinationList) {
		this.destinationList = destinationList;
		return this;
	}

	/**
	 * Gets maxItemCount.
	 * 
	 * @return maxItemCount
	 */
	public int getMaxItemCount() {
		return maxItemCount;
	}

	/**
	 * Sets maxItemCount.
	 * 
	 * @param maxItemCount
	 *            maxItemCount
	 * @return current instance
	 */
	public ClassifierItemOptions setMaxItemCount(int maxItemCount) {
		this.maxItemCount = maxItemCount;
		return this;
	}
}
