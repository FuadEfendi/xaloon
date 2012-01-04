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
package org.xaloon.core.impl.plugin.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * Simple menu item class which is used as default implementation to create category and dynamic menu items
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 */

public class MenuItem extends AbstractPluginBean implements Comparable<MenuItem>, Serializable {
	private static final long serialVersionUID = 1L;

	/** The unique identifier of menu item. Used for link name localization */
	private String key;

	/**
	 * Parameters of url
	 */
	private List<KeyValue<String, String>> parameters = new ArrayList<KeyValue<String, String>>();

	/**
	 * Concrete page class
	 */
	private Class<?> pageClass;

	/**
	 * Order of menu item in user interface
	 */
	private int order;


	/**
	 * @return order of this menu item
	 */
	public int getOrder() {
		return order;
	}

	/**
	 * @param order
	 */
	public void setOrder(int order) {
		this.order = order;
	}

	/**
	 * @param pageClass
	 */
	public void setPageClass(Class<?> pageClass) {
		this.pageClass = pageClass;
	}

	/**
	 * @return page class of this menu item
	 */
	public Class<?> getPageClass() {
		return pageClass;
	}

	/**
	 * Sorting current menu item by provided order property
	 * <p>
	 * If order property id is not provided then order by name
	 */
	public int compareTo(MenuItem o) {
		if (o == null) {
			return 1;
		}
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(getOrder(), o.getOrder());
		compareToBuilder.append(getKey(), o.getKey());

		return compareToBuilder.toComparison();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof MenuItem)) {
			return false;
		}
		MenuItem menuItem = (MenuItem)obj;

		EqualsBuilder equalsBuilder = new EqualsBuilder();
		equalsBuilder.append(getOrder(), menuItem.getOrder());
		return equalsBuilder.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		hashCodeBuilder.append(getOrder());
		return hashCodeBuilder.hashCode();
	}

	/**
	 * @param parameters
	 */
	public void setParameters(List<KeyValue<String, String>> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return parameters for this menu item
	 */
	public List<KeyValue<String, String>> getParameters() {
		return parameters;
	}

	/**
	 * @return unique identifier
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return String.format("Order:%d; PageClass: %s", order, pageClass.getName());
	}
}
