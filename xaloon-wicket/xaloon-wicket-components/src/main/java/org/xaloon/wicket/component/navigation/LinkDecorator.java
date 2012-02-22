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
package org.xaloon.wicket.component.navigation;

import java.io.Serializable;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.list.LoopItem;

/**
 * @author vytautas r.
 */
public class LinkDecorator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private LoopItem loopItem;

	private int currentPage;

	private int startIndex;

	/**
	 * @param loopItem
	 * @return this instance
	 */
	public LinkDecorator withLoopItem(LoopItem loopItem) {
		this.loopItem = loopItem;
		return this;
	}

	/**
	 * @param currentPage
	 * @return this instance
	 */
	public LinkDecorator withCurrentPage(int currentPage) {
		this.currentPage = currentPage;
		return this;
	}

	/**
	 * @param startIndex
	 * @return this instance
	 */
	public LinkDecorator withStartIndex(int startIndex) {
		this.startIndex = startIndex;
		return this;
	}

	/**
	 * 
	 */
	public void decorate() {
		final int pageIndex = startIndex + loopItem.getIndex();

		Component component = loopItem.get("pageLink");
		if ((component != null) && (pageIndex == currentPage)) {
			component.add(AttributeModifier.replace("class", "nav_current_page"));
		}
	}
}
