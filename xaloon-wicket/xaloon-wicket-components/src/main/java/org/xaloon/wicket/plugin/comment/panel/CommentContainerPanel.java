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
package org.xaloon.wicket.plugin.comment.panel;

import org.apache.wicket.model.IModel;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.plugin.comment.CommentPluginBean;
import org.xaloon.core.api.plugin.comment.Commentable;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.comment.CommentPlugin;

/**
 * @author vytautas r.
 */
public class CommentContainerPanel extends AbstractPluginPanel<CommentPluginBean, CommentPlugin> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Class<? extends IRequestablePage> commentPageClass;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 * @param pageRequestParameters
	 */
	public CommentContainerPanel(String id, IModel<Commentable> model, PageParameters pageRequestParameters) {
		super(id, model, pageRequestParameters);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onInitialize(CommentPlugin plugin, CommentPluginBean pluginBean) {
		// Add sign-in via external provider to leave a comment
		add(new SignInToCommentPanel("sign-in-external"));

		// Add new comment panel
		add(new NewCommentPanel("new-comment-panel", (IModel<Commentable>)getDefaultModel(), getPage().getPageParameters()));

		// Add list existing comments panel
		add(new CommentListPanel("comment-list-panel", (IModel<Commentable>)getDefaultModel(), getPage().getPageParameters()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Class<? extends IRequestablePage> getCommentPageClass() {
				if (commentPageClass == null) {
					throw new RuntimeException("commentPageClass property not provided!");
				}
				return commentPageClass;
			}

		});
	}

	/**
	 * @param commentPageClass
	 * @return this instance
	 */
	public CommentContainerPanel setCommentPageClass(Class<? extends IRequestablePage> commentPageClass) {
		this.commentPageClass = commentPageClass;
		return this;
	}
}
