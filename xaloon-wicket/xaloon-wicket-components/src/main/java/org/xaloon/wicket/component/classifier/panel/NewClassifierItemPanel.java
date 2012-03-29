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

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.xaloon.core.api.classifier.Classifier;
import org.xaloon.core.api.classifier.ClassifierItem;
import org.xaloon.core.api.classifier.dao.ClassifierDao;
import org.xaloon.core.api.classifier.dao.ClassifierItemDao;
import org.xaloon.core.api.classifier.search.ClassifierItemSearchRequest;

/**
 * @author vytautas r.
 * @param <T>
 * @param <K>
 */
public class NewClassifierItemPanel<T extends ClassifierItem, K extends Classifier> extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String ERROR_CLASSIFIER_ITEM_CODE_EXISTS = "ERROR_CLASSIFIER_ITEM_CODE_EXISTS";

	@Inject
	private ClassifierItemDao classifierItemDao;

	@Inject
	private ClassifierDao classifierDao;

	private ModalWindow addNewClassifierModalWindow;

	private String classifierType;
	private String parentClassifierItemCode;

	/**
	 * Construct.
	 * 
	 * @param addNewClassifierModalWindow
	 * @param pageParameters
	 */
	public NewClassifierItemPanel(ModalWindow addNewClassifierModalWindow, PageParameters pageParameters) {
		super(addNewClassifierModalWindow.getContentId());
		this.addNewClassifierModalWindow = addNewClassifierModalWindow;
		classifierType = pageParameters.get(ClassifiersPanel.PARAM_CLASSIFIER_TYPE).toString();
		if (pageParameters.get(ClassifierConstants.PARENT_ITEM) != null) {
			parentClassifierItemCode = pageParameters.get(ClassifierConstants.PARENT_ITEM).toString();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		T classifierItem = (T)classifierItemDao.newClassifierItem(classifierType, parentClassifierItemCode);

		// Add classifier form
		add(new NewClassifierItemForm("form-new-classifier-item", new CompoundPropertyModel<T>(classifierItem)));
	}

	private class NewClassifierItemForm extends Form<T> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NewClassifierItemForm(String id, IModel<T> model) {
			super(id, model);
			setMultiPart(true);
			setOutputMarkupId(true);

			// Add feedback panel
			final FeedbackPanel feedbackPanel = new FeedbackPanel("panel-feedback");
			feedbackPanel.setOutputMarkupId(true);
			add(feedbackPanel);

			// Add type
			RequiredTextField<String> typeField = new RequiredTextField<String>("code");
			typeField.add(new ClassifierItemCodeValidator());
			add(typeField);

			// Add name
			RequiredTextField<String> nameField = new RequiredTextField<String>("name");
			add(nameField);

			// Add submit
			add(new AjaxButton("submit", this) {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
					T item = NewClassifierItemForm.this.getModelObject();
					classifierItemDao.createClassifierItem(item);
					addNewClassifierModalWindow.close(target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
			});
		}
	}

	private class ClassifierItemCodeValidator extends AbstractValidator<String> {
		private static final long serialVersionUID = 1L;

		@Override
		protected void onValidate(IValidatable<String> validatable) {
			String value = validatable.getValue();
			if (!StringUtils.isEmpty(value)) {
				ClassifierItemSearchRequest classifierItemSearchRequest = new ClassifierItemSearchRequest();
				classifierItemSearchRequest.setClassifierType(classifierType);
				classifierItemSearchRequest.setClassifierItemCode(value.toUpperCase());
				if (classifierItemDao.getClassifierItem(classifierItemSearchRequest) != null) {
					error(validatable, ERROR_CLASSIFIER_ITEM_CODE_EXISTS);
				}
			}
		}
	}
}
