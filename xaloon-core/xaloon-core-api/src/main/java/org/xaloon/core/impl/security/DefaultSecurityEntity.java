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
package org.xaloon.core.impl.security;

import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.impl.persistence.DefaultPersistentObject;

/**
 * Default authority in memory
 * 
 * @author vytautas r.
 */
public class DefaultSecurityEntity extends DefaultPersistentObject implements Authority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public DefaultSecurityEntity(String name) {
		this.name = name;
		setPath(UrlUtil.encode(name));
	}

	/**
	 * Gets name.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * 
	 * @param name
	 *            name
	 */
	public void setName(String name) {
		this.name = name;
	}
}
