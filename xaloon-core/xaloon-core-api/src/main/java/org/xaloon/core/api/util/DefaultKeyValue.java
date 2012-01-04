/*
 *    xaloon - http://www.xaloon.org
 *    Copyright (C) 2008-2011 vytautas r.
 *
 *    This file is part of xaloon.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xaloon.core.api.util;

import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * Simple class to store key and value
 * 
 * @author vytautas r.
 * 
 * @param <T>
 *            key parameter type
 * @param <K>
 *            value parameter type
 */
public class DefaultKeyValue<T, K> implements KeyValue<T, K> {
	private static final long serialVersionUID = 1L;

	/** pair key property **/
	private T key;

	/** pair value property **/
	private K value;

	/** bookmarkable representation of value. optional **/
	private String path;

	/**
	 * Constructor with initial parameters passed
	 * 
	 * @param key
	 *            key parameter to set
	 * @param value
	 *            value parameter to set
	 */
	public DefaultKeyValue(T key, K value) {
		this.key = key;
		this.value = value;
		if (value != null && value instanceof String) {
			this.path = UrlUtil.encode(value.toString());
		}
	}

	/**
	 * Default constructor when parameters are filled later.
	 */
	public DefaultKeyValue() {
	}

	public T getKey() {
		return key;
	}

	/**
	 * @param key
	 *            key of type T to set
	 */
	public void setKey(T key) {
		this.key = key;
	}

	public K getValue() {
		return value;
	}

	/**
	 * @param value
	 *            value of type K to set
	 */
	public void setValue(K value) {
		this.value = value;
	}

	public boolean isEmpty() {
		return (key == null) && (value == null);
	}

	public String getPath() {
		return path;
	}

	/**
	 * @param path
	 *            bookmarkable value
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
