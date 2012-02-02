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
package org.xaloon.core.api.security;

/**
 * Default system roles
 * 
 * @author vytautas r.
 * 
 */
public interface SecurityRoles {
	/**
	 * Default security role for login purposes
	 */
	String AUTHENTICATED_USER = "AUTHENTICATED_USER";

	/**
	 * User is able to create blog entries when having this role
	 */
	String BLOG_CREATOR = "BLOG_CREATOR";

	/**
	 * User is able to create/update/delete classifiers/classifier items
	 */
	String CLASSIFIER_MANAGER = "CLASSIFIER_MANAGER";

	/**
	 * System administrator role
	 */
	String SYSTEM_ADMINISTRATOR = "SYSTEM_ADMINISTRATOR";
}
