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
package org.xaloon.wicket.component.classifier.panel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.classifier.Classifier;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.classifier.dao.ClassifierItemDao;
import org.xaloon.core.api.classifier.search.ClassifierItemSearchRequest;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.wicket.component.classifier.ClassifierDropDownChoice;

/**
 * @author vytautas r.
 */
public class ClassifierItemListPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private ClassifierItem parentClassifierItem;

	private boolean reloadedByParent = true;

	private ClassifierItem selectedClassifierItem;

	@Inject
	private ClassifierItemDao classifierItemDao;

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public ClassifierItemListPanel(String id, IModel<ClassifierItemOptions> model) {
		super(id, model);
		setOutputMarkupId(true);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		final ClassifierItemOptions classifierItemOptions = (ClassifierItemOptions)getDefaultModelObject();

		// Add drop down choice of classifier items
		final ClassifierDropDownChoice classifierDropDownChoice = addClassifierItemDropDownChoice(classifierItemOptions);

		// Add view to show selected classifier items
		addSelectedClassifierItemView(classifierItemOptions);

		// Create new classifier item
		addCreateNewClassifierItem(classifierItemOptions, classifierDropDownChoice);
	}

	protected void addCreateNewClassifierItem(final ClassifierItemOptions classifierItemOptions,
		final ClassifierDropDownChoice classifierDropDownChoice) {
		final ModalWindow modal2 = new CustomModalWindow("modal2") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(ClassifierItemListPanel.this);
			};
		};
		PageParameters params = new PageParameters();
		params.set(ClassifiersPanel.PARAM_CLASSIFIER_TYPE, classifierItemOptions.getClassifierType());
		params.set(ClassifierConstants.PARENT_ITEM, parentClassifierItem);
		modal2.setContent(new NewClassifierItemPanel<ClassifierItem, Classifier>(modal2, params));
		modal2.setVisible(securityFacade.hasAny(SecurityAuthorities.CLASSIFIER_EDIT));
		add(modal2);

		add(new AjaxLink<Void>("add-new-item") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				modal2.show(target);
			}
		}.setVisible(securityFacade.hasAny(SecurityAuthorities.CLASSIFIER_EDIT)));
	}

	private ClassifierDropDownChoice addClassifierItemDropDownChoice(final ClassifierItemOptions classifierItemOptions) {
		final ClassifierDropDownChoice classifierDropDownChoice = new ClassifierDropDownChoice("classifier-item", new PropertyModel<ClassifierItem>(
			this, "selectedClassifierItem"), getClassifierItems(classifierItemOptions));
		add(classifierDropDownChoice);

		// Add onchange listener for selected classifier item
		classifierDropDownChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@SuppressWarnings("unchecked")
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				List<ClassifierItem> desticationList = (List<ClassifierItem>)classifierItemOptions.getDestinationList();
				if (!desticationList.contains(selectedClassifierItem) &&
					(classifierItemOptions.getMaxItemCount() < 0 || (classifierItemOptions.getMaxItemCount() > 0 && desticationList.size() < classifierItemOptions.getMaxItemCount()))) {
					desticationList.add(getSelectedClassifierItem());
				}
				selectedClassifierItem = null;
				reloadedByParent = false;
				target.add(ClassifierItemListPanel.this);
			}
		});
		return classifierDropDownChoice;
	}

	private void addSelectedClassifierItemView(final ClassifierItemOptions classifierItemOptions) {
		ListView<ClassifierItem> view = new ListView<ClassifierItem>("item-list", classifierItemOptions.getDestinationList()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ClassifierItem> item) {
				final ClassifierItem classifierItem = item.getModelObject();
				item.add(new org.apache.wicket.markup.html.basic.Label("name", classifierItem.getName()));
				item.add(new AjaxLink<Void>("delete") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						classifierItemOptions.getDestinationList().remove(classifierItem);
						reloadedByParent = false;
						target.add(ClassifierItemListPanel.this);
					}
				});
			}
		};
		add(view);
	}

	private List<ClassifierItem> getClassifierItems(ClassifierItemOptions classifierItemOptions) {
		if (!classifierItemOptions.isMultilevelClassifier()) {
			// Search and return single level classifier items
			List<ClassifierItem> allClassifierItems = getClassifierItems(classifierItemOptions.getClassifierType(), null);
			return getAvailableItemSet(classifierItemOptions.getDestinationList(), allClassifierItems);
		} else if (classifierItemOptions.isMultilevelClassifier() && parentClassifierItem != null) {
			// Multiple level classifier items if parent classifier item is provided
			List<ClassifierItem> allClassifierItems = getClassifierItems(classifierItemOptions.getClassifierType(), parentClassifierItem);
			if (reloadedByParent) {
				return allClassifierItems;
			} else {
				return getAvailableItemSet(classifierItemOptions.getDestinationList(), allClassifierItems);
			}
		}
		return Collections.emptyList();
	}

	private List<ClassifierItem> getAvailableItemSet(List<? extends ClassifierItem> classifierItemsSubset, List<ClassifierItem> allClassifierItems) {
		Collection<ClassifierItem> allset = new ArrayList<ClassifierItem>(allClassifierItems);
		List<ClassifierItem> availableProviderCollection = new ArrayList<ClassifierItem>();
		for (ClassifierItem item : classifierItemsSubset) {
			if (allset.contains(item)) {
				allset.remove(item);
			}
		}
		if (!allset.isEmpty()) {
			availableProviderCollection.addAll(allset);
		}
		return availableProviderCollection;
	}

	private List<ClassifierItem> getClassifierItems(String classifierType, ClassifierItem parentClassifierItem) {
		ClassifierItemSearchRequest classifierItemSearchRequest = new ClassifierItemSearchRequest().setClassifierType(classifierType);
		if (parentClassifierItem != null) {
			classifierItemSearchRequest.setParentClassifierItemCode(parentClassifierItem.getCode());
		}
		return classifierItemDao.find(classifierItemSearchRequest);
	}

	/**
	 * @param parentClassifierItem
	 */
	public void setParentClassifierItem(ClassifierItem parentClassifierItem) {
		reloadedByParent = true;
		this.parentClassifierItem = parentClassifierItem;
	}

	/**
	 * Gets selectedClassifierItem.
	 * 
	 * @return selectedClassifierItem
	 */
	public ClassifierItem getSelectedClassifierItem() {
		return selectedClassifierItem;
	}

	/**
	 * Sets selectedClassifierItem.
	 * 
	 * @param selectedClassifierItem
	 *            selectedClassifierItem
	 */
	public void setSelectedClassifierItem(ClassifierItem selectedClassifierItem) {
		this.selectedClassifierItem = selectedClassifierItem;
	}
}
