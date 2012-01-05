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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.keyvalue.KeyValue;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class StringTokensPanel<T extends KeyValue<String, String>> extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	protected static final Logger LOGGER = LoggerFactory.getLogger(StringTokensPanel.class);

	private static final String ITEM_LIST = "item-list";

	private static final String DELIMITER = ",";


	private String selectedStringValue;

	private int maxCount = -1;

	private boolean persist = true;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param key
	 * @param valueList
	 */
	public StringTokensPanel(String id, final String key, final List<T> valueList) {
		super(id, Model.ofList(valueList));
		setOutputMarkupId(true);

		// Add input text field
		final TextField<String> stringValueField = new TextField<String>("input-token", new PropertyModel<String>(this, "selectedStringValue"));
		add(stringValueField);
		stringValueField.add(new AjaxFormComponentUpdatingBehavior("onBlur") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				parseIfValid(key, valueList, target);
			}
		});

		// Add submit ajax link in case onBlur will not work
		add(new AjaxLink<Void>("link-add-selected") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				parseIfValid(key, valueList, target);
			}
		});
	}


	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		if (get(ITEM_LIST) != null) {
			remove(ITEM_LIST);
		}
		final IModel<List<T>> valueListModel = (IModel<List<T>>)getDefaultModel();
		ListView<T> view = new ListView<T>(ITEM_LIST, valueListModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<T> item) {
				final T keyValue = item.getModelObject();
				item.add(new Label("name", new Model<String>(keyValue.getValue())));
				item.add(new AjaxLink<Void>("delete") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						valueListModel.getObject().remove(keyValue);
						target.add(StringTokensPanel.this);
					}
				});
			}
		};
		add(view);
	}

	private void parseIfValid(final String key, final List<T> valueList, AjaxRequestTarget target) {
		if (!StringUtils.isEmpty(getSelectedStringValue())) {
			parseStringValues(key, getSelectedStringValue(), valueList);
			setSelectedStringValue(null);
			target.add(StringTokensPanel.this);
		}
	}

	protected void parseStringValues(String key, String inputString, List<T> valueList) {
		String[] parsedValues = inputString.split(DELIMITER);
		if (parsedValues != null && parsedValues.length > 0) {
			int count = valueList.size();
			int totalCount = (maxCount > 0) ? maxCount : (parsedValues.length + count);

			for (String parsedValue : parsedValues) {
				if (count++ >= totalCount) {
					break;
				}
				parsedValue = parsedValue.trim();
				if (!StringUtils.isEmpty(parsedValue)) {
					// Do not duplicate the same value
					T entry = findInStorage(key, parsedValue);
					if (entry == null) {
						entry = newKeyValue(key, parsedValue, persist);
					}
					if (!valueList.contains(entry)) {
						valueList.add(entry);
					}
				}
			}
		}
	}

	protected abstract T findInStorage(String key, String parsedValue);


	protected abstract T newKeyValue(String key, String parsedValue, boolean persist2);


	/**
	 * @return user input string to process
	 */
	public String getSelectedStringValue() {
		return selectedStringValue;
	}

	/**
	 * @param selectedStringValue
	 */
	public void setSelectedStringValue(String selectedStringValue) {
		this.selectedStringValue = selectedStringValue;
	}


	/**
	 * How many tokens should be processed max
	 * 
	 * @param maxCount
	 */
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}


	/**
	 * @param persist
	 *            true - key and value will be persisted into storage directly
	 */
	public void setPersist(boolean persist) {
		this.persist = persist;
	}
}
