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

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.navigation.paging.IPageable;
import org.apache.wicket.markup.html.navigation.paging.IPagingLabelProvider;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigation;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.classifier.Classifier;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.classifier.dao.ClassifierItemDao;
import org.xaloon.core.api.classifier.search.ClassifierItemSearchRequest;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.EmptyPlugin;
import org.xaloon.wicket.component.classifier.ldm.ClassifierItemLoadableModel;
import org.xaloon.wicket.component.classifier.page.ClassifiersItemPage;
import org.xaloon.wicket.component.classifier.page.ClassifiersPage;
import org.xaloon.wicket.util.Link;


/**
 * @author vytautas r.
 */
public class ClassifiersItemPanel extends AbstractClassifiersPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private ClassifierItemDao classifierItemDao;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public ClassifiersItemPanel(String id, PageParameters pageParameters) {
		super(id, pageParameters);
	}

	/**
	 * @return classifier dao instance
	 */
	private ClassifierItemDao getClassifierItemDao() {
		return classifierItemDao;
	}

	@Override
	protected void onInitialize(EmptyPlugin plugin, AbstractPluginBean pluginBean) {
		PageParameters params = getPageRequestParameters();
		if (params.isEmpty()) {
			setVisible(false);
			setResponsePage(ClassifiersPage.class);
			return;
		}

		final String classifierType = params.get(ClassifiersPanel.PARAM_CLASSIFIER_TYPE).toString();
		String parentClassifierItem = null;
		if (params.get(ClassifierConstants.PARENT_ITEM) != null) {
			parentClassifierItem = params.get(ClassifierConstants.PARENT_ITEM).toString();
		}

		final WebMarkupContainer dataContainer = new WebMarkupContainer("container");
		dataContainer.setOutputMarkupId(true);
		add(dataContainer);


		// Add data view
		final DataView<ClassifierItem> classifierDataView = new DataView<ClassifierItem>("item-list", new ClassifierDataProvider(classifierType,
			parentClassifierItem)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<ClassifierItem> item) {
				final ClassifierItem classifierItem = item.getModelObject();

				// Add link
				PageParameters pageParameters = new PageParameters();
				pageParameters.set(ClassifiersPanel.PARAM_CLASSIFIER_TYPE, classifierType);
				pageParameters.add(ClassifierConstants.PARENT_ITEM, classifierItem.getCode());
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link-item", ClassifiersItemPage.class, pageParameters);
				item.add(link);
				link.add(new Label("code", new Model<String>(classifierItem.getCode())));

				// Add name
				item.add(new Label("name", new Model<String>(classifierItem.getName())));

				item.add(new AjaxLink<Void>("delete-item") {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						classifierItemDao.deleteClassifierItem(classifierItem);
						target.add(dataContainer);
					}
				});
			}
		};
		dataContainer.add(classifierDataView);
		AjaxPagingNavigator navigator = new AjaxPagingNavigator("navigator", classifierDataView) {
			private static final long serialVersionUID = 1L;

			@Override
			protected PagingNavigation newNavigation(String id, IPageable pageable, IPagingLabelProvider labelProvider) {
				PagingNavigation nav = super.newNavigation(id, pageable, labelProvider);
				nav.setViewSize(10);
				return nav;
			}
		};
		dataContainer.add(navigator);
		dataContainer.setVisible(true);
		classifierDataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());

		// Create modal window to create new classifier
		final ModalWindow addNewClassifierModalWindow = new CustomModalWindow("modal-new-item", "title") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(dataContainer);
			};
		};
		addNewClassifierModalWindow.setContent(new NewClassifierItemPanel<ClassifierItem, Classifier>(addNewClassifierModalWindow, params));

		add(addNewClassifierModalWindow);

		// add create new classifier link
		add(new AjaxLink<Void>("link-add-new-item") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				addNewClassifierModalWindow.show(target);
			}
		});
	}

	protected Link getCurrentRedirectLink() {
		return new Link(ClassifiersItemPage.class, getPageRequestParameters());
	}

	private class ClassifierDataProvider implements IDataProvider<ClassifierItem> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private ClassifierItemSearchRequest classifierItemSearchRequest = new ClassifierItemSearchRequest();

		public ClassifierDataProvider(String classifierType, String parentClassifierItemCode) {
			classifierItemSearchRequest.setClassifierType(classifierType);
			classifierItemSearchRequest.setParentClassifierItemCode(parentClassifierItemCode);
		}

		@Override
		public void detach() {
		}

		@Override
		public Iterator<? extends ClassifierItem> iterator(long first, long count) {
			classifierItemSearchRequest.setFirstRow(first);
			classifierItemSearchRequest.setMaxRowCount(count);
			List<ClassifierItem> classifierItems = getClassifierItemDao().find(classifierItemSearchRequest);
			return classifierItems.iterator();
		}

		@Override
		public long size() {
			return getClassifierItemDao().count(classifierItemSearchRequest);
		}

		@Override
		public IModel<ClassifierItem> model(ClassifierItem object) {
			return new ClassifierItemLoadableModel<ClassifierItem>(object.getId());
		}

	}
}
