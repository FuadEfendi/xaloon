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
package org.xaloon.wicket.plugin.user.admin.panel;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class AuthorityManagementContainer<T> extends WebMarkupContainer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IChoiceRenderer<T> choiceRenderer;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public AuthorityManagementContainer(String id) {
		super(id);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		addInformation();
	}

	private void addInformation() {
		// Initialise selections and available item lists
		final List<T> selections = new ArrayList<T>();
		List<T> providedSelections = getProvidedSelections();
		List<T> availableItemsForSelection = getAvailableItemsForSelection();
		availableItemsForSelection.removeAll(providedSelections);

		add(new CheckboxMultipleChoiceManagement<T>("choice-management", selections, availableItemsForSelection, choiceRenderer) {
			private static final long serialVersionUID = 1L;

			@Override
			void onFormSubmit(AjaxRequestTarget target) {
				AuthorityManagementContainer.this.onAssign(selections);
				refreshComponents(target);
			}

			private void refreshComponents(AjaxRequestTarget target) {
				List<Component> components = new ArrayList<Component>();
				addComponentsToRefresh(components);
				if (components != null && !components.isEmpty()) {
					for (Component component : components) {
						if (component != null) {
							target.add(component);
						}
					}
				}
			}

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				AuthorityManagementContainer.this.addComponentsToRefresh(components);
			};
		}.setVisible(!availableItemsForSelection.isEmpty()));

		add(new ListView<T>("current-view", providedSelections) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<T> item) {
				AuthorityManagementContainer.this.onItemAddedToView(item);
			}
		});
	}

	protected abstract List<T> getAvailableItemsForSelection();

	protected abstract List<T> getProvidedSelections();

	protected abstract void onItemAddedToView(ListItem<T> item);

	protected abstract void onAssign(List<T> selections);

	protected void addComponentsToRefresh(List<Component> components) {
	}

	/**
	 * Sets choiceRenderer.
	 * 
	 * @param choiceRenderer
	 *            choiceRenderer
	 * @return this instance
	 */
	public AuthorityManagementContainer<T> setChoiceRenderer(IChoiceRenderer<T> choiceRenderer) {
		this.choiceRenderer = choiceRenderer;
		return this;
	}
}
