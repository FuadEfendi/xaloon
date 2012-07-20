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

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.wicket.util.Link;


/**
 * Bookmarkable paging navigator. This is used in combination with "Back" button.
 * 
 * @author vytautas r.
 * 
 */
public class BookmarkablePagingNavigator extends DecoratedPagingNavigator {

	/**
	 * 
	 */
	public static final String PAGE_QUERY_ID = "pn";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** display page number starting 1, not 0 **/
	public static final int START_INDEX_POSITION = 1;

	private Link pageableLink;
	private long currentPage;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param dataView
	 * @param currentLink
	 * @param pagePositionInParams
	 */
	public BookmarkablePagingNavigator(String id, AbstractPageableView<?> dataView, Link currentLink) {
		super(id, dataView);
		pageableLink = currentLink;
		currentPage = dataView.getCurrentPage();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, int pageNumber) {
		PageParameters params = newPageParameters(pageable, pageNumber);
		return new BookmarkablePageLink<Void>(id, (Class<? extends Page>)pageableLink.getPageClass(), params);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AbstractLink newPagingNavigationIncrementLink(String id, IPageable pageable, int increment) {
		PageParameters params = newPageParameters(pageable, getPageNumber(pageable, increment));
		return new BookmarkablePageLink<Void>(id, (Class<? extends Page>)pageableLink.getPageClass(), params);
	}


	@Override
	protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
		return new PagingNavigation(id, pageable, labelProvider) {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected AbstractLink newPagingNavigationLink(String id, IPageable pageable, long pageIndex) {
				PageParameters params = newPageParameters(pageable, pageIndex);
				return new BookmarkablePageLink<Void>(id, (Class<? extends Page>)pageableLink.getPageClass(), params);
			}

			@Override
			protected void populateItem(LoopItem loopItem) {
				super.populateItem(loopItem);
				new LinkDecorator().withLoopItem(loopItem).withCurrentPage(pageable.getCurrentPage()).withStartIndex(getStartIndex()).decorate();
			}
		};
	}

	private long getPageNumber(IPageable pageable, int increment) {
		// Determine the page number based on the current
		// PageableListView page and the increment
		long idx = currentPage + increment;

		// make sure the index lies between 0 and the last page
		return Math.max(0, Math.min(pageable.getPageCount() - 1, idx));
	}

	private PageParameters newPageParameters(IPageable pageable, long pageNumber) {
		PageParameters params = new PageParameters(pageableLink.getPageParameters());
		if (pageNumber == -1) {
			params.set(PAGE_QUERY_ID, pageable.getPageCount());
		} else {
			params.set(PAGE_QUERY_ID, pageNumber + START_INDEX_POSITION);
		}
		return params;
	}
}
