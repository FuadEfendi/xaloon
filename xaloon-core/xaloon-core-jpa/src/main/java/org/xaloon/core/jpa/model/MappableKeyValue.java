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
package org.xaloon.core.jpa.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * @author vytautas r.
 */
@MappedSuperclass
public class MappableKeyValue extends BookmarkableEntity implements KeyValue<String, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "KEY_", nullable = false)
	private String key;

	@Column(name = "VALUE_", nullable = false)
	private String value;

	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean isEmpty() {
		return (getKey() == null) && (getValue() == null);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MappableKeyValue)) {
			return false;
		}
		MappableKeyValue jpaKeyValue = (MappableKeyValue)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getKey(), jpaKeyValue.getKey());
		equalsBuilder.append(getValue(), jpaKeyValue.getValue());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getKey());
		hashCodeBuilder.append(getValue());
		return hashCodeBuilder.hashCode();
	}

	@Override
	public String toString() {
		return "Key: " + getKey() + ", Value: " + getValue();
	}
}
