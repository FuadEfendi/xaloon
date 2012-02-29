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

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.Model;
import org.xaloon.wicket.component.classifier.panel.CustomModalWindow;

/**
 * @author vytautas r.
 * @param <T>
 */
public abstract class CheckboxMultipleChoiceManagement<T> extends WebMarkupContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<T> selectedEntities;
	private List<T> availableEntitiesToSelect;
	private IChoiceRenderer<T> choiceRenderer;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param selectedEntities
	 * @param availableEntitiesToSelect
	 * @param choiceRenderer
	 */
	public CheckboxMultipleChoiceManagement(String id, List<T> selectedEntities, List<T> availableEntitiesToSelect, IChoiceRenderer<T> choiceRenderer) {
		super(id);
		this.selectedEntities = selectedEntities;
		this.availableEntitiesToSelect = availableEntitiesToSelect;
		this.choiceRenderer = choiceRenderer;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		// Add the modal window
		final ModalWindow addGroupModalWindow = new CustomModalWindow("modal-assign-entities", "Assign something") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void addComponentsToRefresh(List<Component> components) {
				CheckboxMultipleChoiceManagement.this.addComponentsToRefresh(components);
			}
		};
		add(addGroupModalWindow);

		// Create and set form container panel
		FormContainerPanel formContainerPanel = new FormContainerPanel(addGroupModalWindow.getContentId());
		addGroupModalWindow.setContent(formContainerPanel);

		// Create and add form
		Form form = new Form("form");
		formContainerPanel.setForm(form);

		// Create and add selection choices to form
		CheckBoxMultipleChoice choices = new CheckBoxMultipleChoice("choices", Model.of(selectedEntities), availableEntitiesToSelect, choiceRenderer);
		form.add(choices);

		// Add submit button to form
		form.add(new AjaxButton("submit") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				onFormSubmit(target);
				addGroupModalWindow.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});

		// Add assign entities link
		add(new AjaxLink<Void>("link-assign-entities") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				addGroupModalWindow.show(target);
			}
		});
	}

	protected void addComponentsToRefresh(List<Component> components) {
	}

	abstract void onFormSubmit(AjaxRequestTarget target);
}
