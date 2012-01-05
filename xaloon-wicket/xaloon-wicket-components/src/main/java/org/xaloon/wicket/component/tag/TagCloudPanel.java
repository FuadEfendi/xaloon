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
package org.xaloon.wicket.component.tag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class TagCloudPanel<T extends KeyValue<String, String>> extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** parameter name to search by tag */
	public static final String QUERY_BY_TAG = "query_tag";

	private Class<? extends WebPage> pageClass;

	private List<T> allTagCloudList = new ArrayList<T>();

	private List<T> highlightTagCloudList;

	private int randomLinkCountToSelect = 10;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param key
	 */
	public TagCloudPanel(String id, String key) {
		super(id, new Model<String>(key));
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		if (highlightTagCloudList == null || highlightTagCloudList.isEmpty()) {
			setVisible(false);
			return;
		}
		// proceed only with filled values
		int maxCount = Math.abs(getRandomLinkCountToSelect() - highlightTagCloudList.size());
		if (maxCount < getRandomLinkCountToSelect()) {
			allTagCloudList = findRandomValues(getDefaultModelObjectAsString(), maxCount);
		}
		// do not include already existing ones
		for (T item : highlightTagCloudList) {
			if (!allTagCloudList.contains(item)) {
				allTagCloudList.add(item);
			}
		}
		// shuffle collection
		Collections.shuffle(allTagCloudList);

		add(new ListView<T>("tag-cloud-list", allTagCloudList) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<T> item) {
				T keyValue = item.getModelObject();

				// Add value link
				PageParameters pageParameters = new PageParameters();
				pageParameters.set(QUERY_BY_TAG, keyValue.getPath());
				BookmarkablePageLink<Void> tagValueLink = new BookmarkablePageLink<Void>("link-tag-value", pageClass, pageParameters);
				item.add(tagValueLink);

				// Add Value label
				Label tagValueLabel = new Label("tag-value", new Model<String>(keyValue.getValue()));
				tagValueLink.add(tagValueLabel);
				if (highlightTagCloudList != null && highlightTagCloudList.contains(keyValue)) {
					tagValueLabel.add(AttributeModifier.replace("class", "tag-cloud-big"));
				}
			}
		});
	}

	/**
	 * @param pageClass
	 */
	public void setPageClass(Class<? extends WebPage> pageClass) {
		this.pageClass = pageClass;
	}

	/**
	 * @param highlightTagCloudList
	 */
	public void setHighlightTagCloudList(List<T> highlightTagCloudList) {
		this.highlightTagCloudList = highlightTagCloudList;
	}

	/**
	 * @return how many values randomly select for provided key
	 */
	public int getRandomLinkCountToSelect() {
		return randomLinkCountToSelect;
	}

	/**
	 * @param randomLinkCountToSelect
	 */
	public void setRandomLinkCountToSelect(int randomLinkCountToSelect) {
		this.randomLinkCountToSelect = randomLinkCountToSelect;
	}

	protected abstract List<T> findRandomValues(String defaultModelObjectAsString, int maxCount);
}
