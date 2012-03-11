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
package org.xaloon.core.api.classifier.dao;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.classifier.search.ClassifierItemSearchRequest;

/**
 * @author vytautas r.
 */
public interface ClassifierItemDao extends Serializable {

	/**
	 * Persists instance of classifier item into storage.
	 * 
	 * @param <T>
	 * 
	 * @param item
	 *            instance to persist.
	 * @return created item
	 */
	<T extends ClassifierItem> T createClassifierItem(T item);

	/**
	 * Counts how many classifier items are in storage by provided parameters
	 * 
	 * @param classifierItemSearchRequest
	 *            parameters used in search query
	 * @return total count of classifier items by provided parameters
	 */
	Long count(ClassifierItemSearchRequest classifierItemSearchRequest);

	/**
	 * @param <T>
	 * @param classifierItemSearchRequest
	 *            parameters used in search query
	 * @return list of classifier items. empty list is returned if no classifier items found.
	 */
	<T extends ClassifierItem> List<T> find(ClassifierItemSearchRequest classifierItemSearchRequest);

	/**
	 * Loads classifier item by it's primary key
	 * 
	 * @param <T>
	 * 
	 * @param id
	 *            unique key to load by
	 * @return classifier item instance
	 */
	<T extends ClassifierItem> T loadClassifierItemById(Long id);

	/**
	 * Creates new empty instance of classifier item
	 * 
	 * @param <T>
	 * 
	 * @param classifierType
	 *            classifier path to search for classifier
	 * @param parentClassifierItemCode
	 *            parent classifier item will be set if present
	 * @return instance of classifier item
	 */
	<T extends ClassifierItem> T newClassifierItem(String classifierType, String parentClassifierItemCode);

	/**
	 * Searches storage for classifier by provided classifier item code
	 * 
	 * @param <T>
	 * 
	 * @param classifierItemSearchRequest
	 *            parameters used in search query
	 * 
	 * @return classifier item instance found in storage. null is returned if not found.
	 */
	<T extends ClassifierItem> T getClassifierItem(ClassifierItemSearchRequest classifierItemSearchRequest);

	/**
	 * Returns classifier items of 2nd level and any lower levels if any
	 * 
	 * @param classifierType
	 * @return list of classifier items
	 */
	<T extends ClassifierItem> List<T> findLowerLevelItems(String classifierType);
}
