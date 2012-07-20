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

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.LoopItem;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.AbstractPageableView;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.flow.RedirectToUrlException;
import org.apache.wicket.util.cookies.CookieUtils;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class DecoratedPagingNavigator extends PagingNavigator {

	private static final String WICKET_ID_NAVIGATION = "navigation";

	private static final String WICKET_ID_SELECT_ITEMS = "selectItems";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** show 5 items per page **/
	public static final Long ITEMS_PER_PAGE_COUNT_5 = 5L;

	/** show 10 items per page **/
	public static final Long ITEMS_PER_PAGE_COUNT_10 = 10L;

	/** show 20 items per page **/
	public static final Long ITEMS_PER_PAGE_COUNT_20 = 20L;

	/** show 50 items per page **/
	public static final Long ITEMS_PER_PAGE_COUNT_50 = 50L;

	/** show 100 items per page **/
	public static final Long ITEMS_PER_PAGE_COUNT_100 = 100L;

	private static final List<Long> ITEMS_PER_PAGE_LIST = new ArrayList<Long>();

	/** cookie containing user selection - how many items per page to show **/
	public static final String ITEMS_PER_PAGE_COOKIE = "items_per_page";

	private Long selectedItemsPerPage = ITEMS_PER_PAGE_COUNT_20;

	static {
		ITEMS_PER_PAGE_LIST.add(ITEMS_PER_PAGE_COUNT_5);
		ITEMS_PER_PAGE_LIST.add(ITEMS_PER_PAGE_COUNT_10);
		ITEMS_PER_PAGE_LIST.add(ITEMS_PER_PAGE_COUNT_20);
		ITEMS_PER_PAGE_LIST.add(ITEMS_PER_PAGE_COUNT_50);
		ITEMS_PER_PAGE_LIST.add(ITEMS_PER_PAGE_COUNT_100);
	}

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param dataView
	 */
	public DecoratedPagingNavigator(String id, AbstractPageableView<?> dataView) {
		super(id, dataView);
		selectedItemsPerPage = dataView.getItemsPerPage();
	}

	/**
	 * Constructor.
	 * 
	 * @param id
	 *            See Component
	 * @param dataView
	 *            The pageable component the page links are referring to.
	 * @param labelProvider
	 *            The label provider for the link text.
	 */
	public DecoratedPagingNavigator(final String id, final AbstractPageableView<?> dataView, final IPagingLabelProvider labelProvider) {
		super(id, dataView, labelProvider);
		selectedItemsPerPage = dataView.getItemsPerPage();
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		refreshNavigationComponents();
	}

	@Override
	protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
		return new PagingNavigation(id, pageable, labelProvider) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(LoopItem loopItem) {
				super.populateItem(loopItem);
				new LinkDecorator().withLoopItem(loopItem).withCurrentPage(pageable.getCurrentPage()).withStartIndex(getStartIndex()).decorate();
			}
		};
	}

	/**
	 * @return how many items should be shown per page
	 */
	public Long getSelectedItemsPerPage() {
		return selectedItemsPerPage;
	}

	/**
	 * @param selectedItemsPerPage
	 */
	public void setSelectedItemsPerPage(Long selectedItemsPerPage) {
		this.selectedItemsPerPage = selectedItemsPerPage;
	}

	protected void refreshNavigationComponents() {
		WebMarkupContainer currentNavigator = this;
		long currentPage = getPageable().getCurrentPage();
		long pageCount = getPageable().getPageCount();

		if (get(WICKET_ID_SELECT_ITEMS) != null) {
			remove(WICKET_ID_SELECT_ITEMS);
		}
		Form<Void> form = new StatelessForm<Void>(WICKET_ID_SELECT_ITEMS) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if ((selectedItemsPerPage != null) && (getParent() != null)) {
					((AbstractPageableView<?>)getPageable()).setItemsPerPage(selectedItemsPerPage);
					new CookieUtils().save(ITEMS_PER_PAGE_COOKIE, selectedItemsPerPage.toString());
					throw new RedirectToUrlException(UrlUtils.toAbsolutePath(getPage().getClass(), getPage().getPageParameters()).toString());
				}
			}
		};
		add(form);

		refreshFirstPreviousComponents(currentNavigator, form, currentPage);

		refreshLastNextComponents(currentNavigator, form, currentPage, pageCount);

		refreshDropDownItemsPerPage(currentNavigator, form);
	}

	private void refreshDropDownItemsPerPage(WebMarkupContainer currentNavigator, Form<Void> form) {
		if (currentNavigator.get(WICKET_ID_NAVIGATION) != null) {
			Component navigation = currentNavigator.get(WICKET_ID_NAVIGATION);
			currentNavigator.remove(navigation);
			form.add(navigation);
		}
		if (form.get("items-per-page") == null) {
			List<Long> itemsPerPageList = getItemsPerPageAsList();
			DropDownChoice<Long> dc = new DropDownChoice<Long>("items-per-page", new PropertyModel<Long>(this, "selectedItemsPerPage"),
				itemsPerPageList);

			form.add(dc);
		}
	}

	protected List<Long> getItemsPerPageAsList() {
		return ITEMS_PER_PAGE_LIST;
	}

	private void refreshLastNextComponents(WebMarkupContainer currentNavigator, Form<Void> form, long currentPage, long pageCount) {
		Component nextPage = currentNavigator.get("next");
		if (nextPage != null) {
			currentNavigator.remove("next");
			form.add(nextPage);
		}
		Component lastPage = currentNavigator.get("last");
		if (lastPage != null) {
			currentNavigator.remove("last");
			form.add(lastPage);
		}
		if ((nextPage != null) && (lastPage != null)) {
			if (pageCount == 0 || currentPage == pageCount - 1) {
				nextPage.setVisible(false);
				lastPage.setVisible(false);
			} else {
				nextPage.setVisible(true);
				lastPage.setVisible(true);
			}
		}
	}

	private void refreshFirstPreviousComponents(WebMarkupContainer currentNavigator, Form<Void> form, long currentPage) {
		Component previousPage = currentNavigator.get("prev");
		if (previousPage != null) {
			currentNavigator.remove("prev");
			form.add(previousPage);
		}

		Component firstPage = currentNavigator.get("first");
		if (firstPage != null) {
			currentNavigator.remove("first");
			form.add(firstPage);
		}
		if ((previousPage != null) && (firstPage != null)) {
			if (currentPage == 0) {
				previousPage.setVisible(false);
				firstPage.setVisible(false);
			} else {
				previousPage.setVisible(true);
				firstPage.setVisible(true);
			}
		}
	}
}
