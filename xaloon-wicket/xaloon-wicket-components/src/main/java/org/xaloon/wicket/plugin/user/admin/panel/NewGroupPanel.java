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

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.xaloon.core.api.security.RoleGroupService;
import org.xaloon.core.api.security.SecurityGroup;


/**
 * @author vytautas r.
 */
public class NewGroupPanel extends AbstractAdministrationPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private RoleGroupService roleGroupService;

	private ModalWindow modalWindow;

	/**
	 * Construct.
	 * 
	 * @param id
	 */
	public NewGroupPanel(String id) {
		super(id);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();

		Form<SecurityGroup> groupForm = new Form<SecurityGroup>("new-group", new CompoundPropertyModel<SecurityGroup>(roleGroupService.newGroup()));
		add(groupForm);

		// Add feedback panel
		final FeedbackPanel feedbackPanel = new FeedbackPanel("feedback");
		feedbackPanel.setOutputMarkupId(true);
		groupForm.add(feedbackPanel);

		// Add name input field
		groupForm.add(new RequiredTextField<String>("name"));

		// Add submit button
		groupForm.add(new AjaxButton("submit") {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				SecurityGroup group = (SecurityGroup)form.getModelObject();
				roleGroupService.saveGroup(group);
				modalWindow.close(target);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
			}

		});
	}

	/**
	 * Sets modalWindow.
	 * 
	 * @param modalWindow
	 *            modalWindow
	 * @return
	 */
	public NewGroupPanel setModalWindow(ModalWindow modalWindow) {
		this.modalWindow = modalWindow;
		return this;
	}
}
