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

import java.util.List;

import javax.inject.Inject;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.plugin.comment.Comment;
import org.xaloon.core.api.plugin.comment.CommentDao;
import org.xaloon.core.api.plugin.comment.CommentPluginBean;
import org.xaloon.wicket.component.custom.ConfirmationAjaxLink;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.comment.CommentPlugin;

/**
 * @author vytautas r.
 */
public class WaitingCommentsForApprovalPanel extends AbstractPluginPanel<CommentPluginBean, CommentPlugin> {
	private static final long serialVersionUID = 1L;

	@Inject
	private CommentDao commentDao;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageRequestParameters
	 */
	public WaitingCommentsForApprovalPanel(String id, PageParameters pageRequestParameters) {
		super(id, pageRequestParameters);
		setOutputMarkupId(true);
	}

	@Override
	protected void onInitialize(CommentPlugin plugin, CommentPluginBean pluginBean) {
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		removeAll();
		List<Comment> waitingCommentsForApproval = commentDao.getWaitingCommentsForApproval();
		ListView<Comment> commentListView = new ListView<Comment>("comment-list-view", waitingCommentsForApproval) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Comment> item) {
				Comment comment = item.getModelObject();

				// Add create date
				item.add(new Label("create-date", new Model<String>(comment.getCreateDate().toString())));

				// Add author display name
				item.add(new Label("display-name", new Model<String>(comment.getFromUser().getDisplayName())));

				// Add comment body
				item.add(new Label("message", new Model<String>(comment.getMessage())));

				// Add path to commentable object
				item.add(new ExternalLink("link-to-object", new Model<String>(comment.getPath())));

				// Add accept comment link
				item.add(new AjaxLink<Comment>("accept", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Comment comment = getModelObject();
						commentDao.enable(comment);
						target.add(WaitingCommentsForApprovalPanel.this);
					}

				});

				// Add delete comment link
				item.add(new ConfirmationAjaxLink<Comment>("delete", item.getModel()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						Comment comment = getModelObject();
						commentDao.delete(comment);
						target.add(WaitingCommentsForApprovalPanel.this);
					}
				});
			}
		};
		commentListView.setVisible(!waitingCommentsForApproval.isEmpty());
		add(commentListView);

		// Add delete all link
		add(new ConfirmationAjaxLink<Void>("delete-all") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				commentDao.deleteWaitingCommentsForApproval();
				target.add(WaitingCommentsForApprovalPanel.this);
			}
		});
	}
}
