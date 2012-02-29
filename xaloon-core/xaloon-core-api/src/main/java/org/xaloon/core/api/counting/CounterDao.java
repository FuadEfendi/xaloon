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
package org.xaloon.core.api.counting;

import java.io.Serializable;

/**
 * @author vytautas r.
 */
public interface CounterDao extends Serializable {

	/**
	 * @param counterGroup
	 *            refers to the type of update, such as view/count/other
	 * @param categoryId
	 *            refers to the table, such as blog entry, classifier/other
	 * @param entityId
	 *            refers to the concrete row to update
	 * @return true if action was performed successfully
	 */
	boolean increment(String counterGroup, Long categoryId, Long entityId);

	/**
	 * @param counterGroup
	 *            refers to the type of update, such as view/count/other
	 * @param categoryId
	 *            refers to the table, such as blog entry, classifier/other
	 * @param entityId
	 *            refers to the concrete row to update
	 * @return counter value for selected entry
	 */
	Long count(String counterGroup, Long categoryId, Long entityId);

	/**
	 * @param counterGroup
	 *            refers to the type of update, such as view/count/other
	 * @param categoryId
	 *            refers to the table, such as blog entry, classifier/other
	 * @param entityId
	 *            refers to the concrete row to update
	 * @return true if action was performed successfully
	 */
	boolean decrement(String counterGroup, Long categoryId, Long entityId);

}
