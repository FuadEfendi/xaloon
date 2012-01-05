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
import org.xaloon.core.api.classifier.dao.ClassifierDao;

/**
 * @author vytautas r.
 * @param <T>
 */
public class NewClassifierPanel<T extends Classifier> extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String ERROR_CLASSIFIER_TYPE_EXISTS = "ERROR_CLASSIFIER_TYPE_EXISTS";

	@Inject
	private ClassifierDao classifierDao;

	private ModalWindow addNewClassifierModalWindow;

	/**
	 * Construct.
	 * 
	 * @param addNewClassifierModalWindow
	 * @param pageParameters
	 */
	public NewClassifierPanel(ModalWindow addNewClassifierModalWindow, PageParameters pageParameters) {
		super(addNewClassifierModalWindow.getContentId());
		this.addNewClassifierModalWindow = addNewClassifierModalWindow;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		T classifier = (T)classifierDao.newClassifier();

		// Add classifier form
		add(new NewClassifierForm("form-new-classifier", new CompoundPropertyModel<T>(classifier)));
	}

	private class NewClassifierForm extends Form<T> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public NewClassifierForm(String id, IModel<T> model) {
			super(id, model);
			setMultiPart(true);
			setOutputMarkupId(true);

			// Add feedback panel
			final FeedbackPanel feedbackPanel = new FeedbackPanel("panel-feedback");
			feedbackPanel.setOutputMarkupId(true);
			add(feedbackPanel);

			// Add type
			RequiredTextField<String> typeField = new RequiredTextField<String>("type");
			typeField.add(new ClassifierTypeValidator());
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
					T item = NewClassifierForm.this.getModelObject();
					classifierDao.createClassifier(item);
					addNewClassifierModalWindow.close(target);
				}

				@Override
				protected void onError(AjaxRequestTarget target, Form<?> form) {
					target.add(feedbackPanel);
				}
			});
		}
	}

	private class ClassifierTypeValidator extends AbstractValidator<String> {
		private static final long serialVersionUID = 1L;

		@Override
		protected void onValidate(IValidatable<String> validatable) {
			String value = validatable.getValue();
			if (classifierDao.findClassifierByType(value) != null) {
				error(validatable, ERROR_CLASSIFIER_TYPE_EXISTS);
			}
		}
	}
}
