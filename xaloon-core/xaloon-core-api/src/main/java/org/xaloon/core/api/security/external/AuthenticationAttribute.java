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
package org.xaloon.core.api.security.external;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * @author vytautas r.
 */
public class AuthenticationAttribute implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String name;
	private final String type;
	private boolean required = false;
	private int count = 1;
	private final List<String> values;

	/**
	 * Construct.
	 * 
	 * @param name
	 * @param value
	 */
	public AuthenticationAttribute(String name, String value) {
		this.name = name;
		type = null;

		if (!StringUtils.isEmpty(value)) {
			values = new ArrayList<String>();
			values.add(value);
		} else {
			values = null;
		}

	}

	/**
	 * Construct.
	 * 
	 * @param name
	 * @param type
	 * @param values
	 */
	public AuthenticationAttribute(String name, String type, List<String> values) {
		this.name = name;
		this.type = type;
		this.values = values;
	}

	/**
	 * @return true if attribute is required
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * @param required
	 */
	public void setRequired(boolean required) {
		this.required = required;
	}

	/**
	 * @return count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count
	 */
	public void setCount(int count) {
		this.count = count;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return type identifier
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return first value
	 */
	public String getValue() {
		if (values != null && !values.isEmpty()) {
			return values.get(0);
		}
		return null;
	}

	/**
	 * @return all values
	 */
	public List<String> getValues() {
		return values;
	}
}
