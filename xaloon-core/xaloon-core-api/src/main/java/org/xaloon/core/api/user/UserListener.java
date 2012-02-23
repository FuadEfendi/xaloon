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
package org.xaloon.core.api.user;

import java.io.Serializable;

import org.xaloon.core.api.user.model.User;

/**
 * Default actions should be execute related to user acticity
 * 
 * @author vytautas r.
 */
public interface UserListener extends Serializable {
	/**
	 * This method is invoked before user is removed from the system. One should register it's own listenere if there are some actions to be taken
	 * before final user removal, for example, clean up user comments, blogs, etc.
	 * 
	 * @param userToBeDeleted
	 */
	void onBeforeDelete(User userToBeDeleted);
}
