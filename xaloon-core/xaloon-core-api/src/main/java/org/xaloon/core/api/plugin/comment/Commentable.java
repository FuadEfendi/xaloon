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
package org.xaloon.core.api.plugin.comment;

import org.xaloon.core.api.persistence.Persistable;

/**
 * Interface is used to identify the object which is commentable
 * 
 * @author vytautas r.
 */
public interface Commentable extends Persistable {
	/**
	 * Commentable object may have the same id if there are many implementations. Component + id should ensure uniqueness
	 * 
	 * @return unique identifier for the same group of commentable objects
	 */
	Long getComponentId();

	/**
	 * Returns the author username of commentable object. This might be required to check if current user is the same as author of commentable object.
	 * 
	 * @return username who created commentable object.
	 */
	String getOwnerUsername();
}
