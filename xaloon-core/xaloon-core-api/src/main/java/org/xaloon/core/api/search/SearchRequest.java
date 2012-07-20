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
package org.xaloon.core.api.search;

import java.io.Serializable;

/**
 * @author vytautas r.
 */
public abstract class SearchRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long firstRow = -1;

	private long maxRowCount = -1;

	/**
	 * @return start position to search for items. -1,0 - search from start position
	 */
	public long getFirstRow() {
		return firstRow;
	}

	/**
	 * @param firstRow
	 */
	public void setFirstRow(long firstRow) {
		this.firstRow = firstRow;
	}

	/**
	 * @return maximum count of items to return. -1 - return all
	 */
	public long getMaxRowCount() {
		return maxRowCount;
	}

	/**
	 * @param maxRowCount
	 */
	public void setMaxRowCount(long maxRowCount) {
		this.maxRowCount = maxRowCount;
	}
}
