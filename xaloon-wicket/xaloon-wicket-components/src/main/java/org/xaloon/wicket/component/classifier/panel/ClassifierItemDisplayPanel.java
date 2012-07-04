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

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.classifier.dao.ClassifierItemDao;
import org.xaloon.core.api.classifier.search.ClassifierItemSearchRequest;

/**
 * @author vytautas r.
 */
public abstract class ClassifierItemDisplayPanel extends Panel {

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
	 * @param classifierType
	 */
	public ClassifierItemDisplayPanel(String id, String classifierType) {
		super(id, new Model<String>(classifierType));
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		String classifierType = getDefaultModelObjectAsString();

		// Add list of classifier items
		ClassifierItemSearchRequest classifierItemSearchRequest = new ClassifierItemSearchRequest();
		classifierItemSearchRequest.setClassifierType(classifierType);
		List<ClassifierItem> classifierItems = getClassifierItemDao().find(classifierItemSearchRequest);
		add(new ListView<ClassifierItem>("list-classifier-item", classifierItems) {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<ClassifierItem> item) {
				ClassifierItem classifierItem = item.getModelObject();

				PageParameters pageParameters = new PageParameters();
				pageParameters.add(ClassifierConstants.PARENT_ITEM, classifierItem.getCode());
				BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", getDisplayClassifierItemPageClass(), pageParameters);
				item.add(link);

				// Add name
				link.add(new Label("name", new Model<String>(classifierItem.getName())));
			}
		});
	}

	/**
	 * Page class to redirect by provided classifier item
	 * 
	 * @return page class
	 */
	protected abstract Class<? extends Page> getDisplayClassifierItemPageClass();

	protected ClassifierItemDao getClassifierItemDao() {
		return classifierItemDao;
	}
}
