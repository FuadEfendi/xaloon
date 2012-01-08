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

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.cookies.CookieUtils;
import org.xaloon.wicket.util.Link;

/**
 * @author vytautas r.
 * @param <T>
 */
public class DecoratedPagingNavigatorContainer<T> extends WebMarkupContainer {
	private static final long serialVersionUID = 1L;

	private Link currentLink;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param currentLink
	 */
	public DecoratedPagingNavigatorContainer(String id, Link currentLink) {
		super(id);
		setOutputMarkupId(true);
		this.currentLink = currentLink;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		// Ajax paginator should be added before render
		if (currentLink == null) {
			AbstractPageableView<T> dataView = (AbstractPageableView<T>)getDefaultModelObject();
			PagingNavigator pagingNavigator = new PagingNavigator("navigator", dataView);
			addOrReplace(pagingNavigator);
		}
	}

	/**
	 * @param dataView
	 */
	public void addAbstractPageableView(AbstractPageableView<T> dataView) {
		addAbstractPageableView(dataView, true);
	}

	/**
	 * @param dataView
	 * @param isNavigatorVisible
	 */
	public void addAbstractPageableView(AbstractPageableView<T> dataView, boolean isNavigatorVisible) {
		if (dataView == null) {
			setVisible(false);
			return;
		}
		// Get selected items page from cookie
		String cookieValue = new CookieUtils().load(DecoratedPagingNavigator.ITEMS_PER_PAGE_COOKIE);
		int defaultItemsPerPage = DecoratedPagingNavigator.ITEMS_PER_PAGE_COUNT_20;
		if (cookieValue != null && StringUtils.isNumeric(cookieValue)) {
			defaultItemsPerPage = new Integer(cookieValue);
		}
		dataView.setItemsPerPage(defaultItemsPerPage);
		setVisible(dataView.getItemCount() > 0);
		// Add data view
		setDefaultModel(new Model<AbstractPageableView<T>>(dataView));


		// Add bookmarkable navigator
		if (currentLink != null) {
			int currentPage;

			// Select current page
			currentPage = getCurrentPage();

			dataView.setCurrentPage(currentPage);

			BookmarkablePagingNavigator bookmarkablePagingNavigator = new BookmarkablePagingNavigator("navigator", dataView, currentLink);
			addOrReplace(bookmarkablePagingNavigator);
			bookmarkablePagingNavigator.setVisible(isNavigatorVisible);
		}
		add(dataView);
	}

	private int getCurrentPage() {
		PageParameters params = currentLink.getPageParameters();
		if (params.get(BookmarkablePagingNavigator.PAGE_QUERY_ID) != null && !params.get(BookmarkablePagingNavigator.PAGE_QUERY_ID).isEmpty()) {
			return params.get(BookmarkablePagingNavigator.PAGE_QUERY_ID).toInt() - BookmarkablePagingNavigator.START_INDEX_POSITION;
		}
		return 0;
	}
}
