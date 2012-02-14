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
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.ReuseIfModelsEqualStrategy;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.classifier.Classifier;
import org.xaloon.core.api.classifier.dao.ClassifierDao;
import org.xaloon.core.api.plugin.AbstractPluginBean;
import org.xaloon.core.api.plugin.EmptyPlugin;
import org.xaloon.wicket.component.classifier.ldm.ClassifierLoadableModel;
import org.xaloon.wicket.component.classifier.page.ClassifiersItemPage;
import org.xaloon.wicket.component.classifier.page.ClassifiersPage;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.util.Link;


/**
 * @author vytautas r.
 */
public class ClassifiersPanel extends AbstractClassifiersPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The page parameter
	 */
	public static final String PARAM_CLASSIFIER_TYPE = "classifier_type";

	@Inject
	private ClassifierDao classifierDao;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 */
	public ClassifiersPanel(String id, PageParameters pageParameters) {
		super(id, pageParameters);
		setOutputMarkupId(true);
	}

	/**
	 * @return classifier dao instance
	 */
	private ClassifierDao getClassifierDao() {
		return classifierDao;
	}

	@Override
	protected void onInitialize(EmptyPlugin plugin, AbstractPluginBean pluginBean) {
		// Add data container
		final DecoratedPagingNavigatorContainer<Classifier> dataContainer = new DecoratedPagingNavigatorContainer<Classifier>("container",
			getCurrentRedirectLink());
		add(dataContainer);

		// Add data view
		final DataView<Classifier> classifierDataView = new DataView<Classifier>("item-list", new ClassifierDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Classifier> item) {
				Classifier classifier = item.getModelObject();

				// Add link
				PageParameters pageParameters = new PageParameters();
				pageParameters.set(PARAM_CLASSIFIER_TYPE, classifier.getType());
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link-item", ClassifiersItemPage.class, pageParameters);
				item.add(link);
				link.add(new Label("type", new Model<String>(classifier.getType())));

				// Add name
				item.add(new Label("name", new Model<String>(classifier.getName())));
			}
		};
		dataContainer.addAbstractPageableView(classifierDataView);
		classifierDataView.setItemReuseStrategy(ReuseIfModelsEqualStrategy.getInstance());
		dataContainer.setVisible(true);

		// Create modal window to create new classifier
		final ModalWindow addNewClassifierModalWindow = new CustomModalWindow("modal-new-item", "title") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void addComponentsToRefresh(java.util.List<Component> components) {
				components.add(dataContainer);
			};
		};
		addNewClassifierModalWindow.setContent(new NewClassifierPanel<Classifier>(addNewClassifierModalWindow, getPage().getPageParameters()));
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
		return new Link(ClassifiersPage.class, getPage().getPageParameters());
	}

	private class ClassifierDataProvider implements IDataProvider<Classifier> {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
		}

		@Override
		public Iterator<Classifier> iterator(int first, int count) {
			List<Classifier> items = getClassifierDao().findClassifiers(first, count);
			return items.iterator();
		}

		@Override
		public int size() {
			return getClassifierDao().getCount().intValue();
		}

		@Override
		public IModel<Classifier> model(Classifier object) {
			return new ClassifierLoadableModel<Classifier>(object.getId());
		}
	}
}
