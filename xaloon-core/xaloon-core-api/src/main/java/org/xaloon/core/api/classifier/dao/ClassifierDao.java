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

import org.xaloon.core.api.classifier.Classifier;

/**
 * @author vytautas r.
 */
public interface ClassifierDao extends Serializable {

	/**
	 * @return total count of existing classifiers
	 */
	Long getCount();

	/**
	 * @param <T>
	 * @param first
	 *            start position to search for classifiers. -1,0 - search from start position
	 * @param count
	 *            maximum count of classifiers to return. -1 - return all
	 * @return list of classifier instances. empty list is returned if no classifiers found.
	 */
	<T extends Classifier> List<T> findClassifiers(long first, long count);

	/**
	 * Loads classifier by it's primary key
	 * 
	 * @param <T>
	 * 
	 * @param id
	 *            unique key to load by
	 * @return classifier instance
	 */
	<T extends Classifier> T loadClassifierById(Long id);

	/**
	 * Persists instance of classifier into storage.
	 * 
	 * @param <T>
	 * 
	 * @param item
	 *            instance to persist.
	 */
	<T extends Classifier> void createClassifier(T item);

	/**
	 * Creates new empty instance of classifier
	 * 
	 * @param <T>
	 * 
	 * @return instance of classifier
	 */
	<T extends Classifier> T newClassifier();

	/**
	 * Searches storage for classifier by provided type
	 * 
	 * @param <T>
	 * 
	 * @param type
	 *            string representation of classifier type used in query to search for classifier
	 * @return classifier instance found in storage. null is returned if not found.
	 */
	<T extends Classifier> T findClassifierByType(String type);
}
