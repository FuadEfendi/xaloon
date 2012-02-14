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

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.plugin.comment.Comment;
import org.xaloon.core.api.plugin.comment.CommentDao;
import org.xaloon.core.api.plugin.comment.CommentPluginBean;
import org.xaloon.core.api.plugin.comment.Commentable;
import org.xaloon.core.api.plugin.email.EmailFacade;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.wicket.component.text.MessageForm;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.comment.CommentPlugin;
import org.xaloon.wicket.plugin.comment.template.CommentEmailTemplatePage;
import org.xaloon.wicket.util.UrlUtils;

/**
 * @author vytautas r.
 */
public class NewCommentPanel extends AbstractPluginPanel<CommentPluginBean, CommentPlugin> {
	private static final long serialVersionUID = 1L;

	@Inject
	private CommentDao commentDao;

	@Inject
	private SecurityFacade securityFacade;

	@Inject
	private EmailFacade emailFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param pageParameters
	 * @param commentableModel
	 */
	public NewCommentPanel(String id, IModel<Commentable> commentableModel, PageParameters pageParameters) {
		super(id, commentableModel, pageParameters);
		if (!getPluginBean().isAllowPostForAnonymous()) {
			setVisible(isVisible() && securityFacade.isLoggedIn());
		}
	}

	@Override
	protected void onInitialize(CommentPlugin plugin, CommentPluginBean pluginBean) {
		Commentable commentable = (Commentable)getDefaultModelObject();
		Comment comment = commentDao.newComment();
		comment.setEntityId(commentable.getId());
		comment.setCategoryId(commentable.getTrackingCategoryId());
		NewCommentForm newCommentForm = new NewCommentForm("new-comment-form", new CompoundPropertyModel<Comment>(comment));
		newCommentForm.setAuthorUsername(commentable.getOwnerUsername());
		add(newCommentForm);

		add(new FeedbackPanel("feedback"));
	}

	class NewCommentForm extends MessageForm<Comment> {
		private static final String COMMENT_WAITING_APPROVAL = "COMMENT_WAITING_APPROVAL";
		private static final long serialVersionUID = 1L;

		private String authorUsername;

		public NewCommentForm(String id, IModel<Comment> model) {
			super(id, model);
		}

		@Override
		protected void onInitialize() {
			setEmailVisible(false);
			setCaptchaVisible(getPluginBean().isEnableCaptcha());
			super.onInitialize();
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void onSubmit() {
			final PageParameters pageParameters = getPage().getPageParameters();

			Comment comment = getModelObject();
			String absolutePath = UrlUtils.toAbsolutePath((Class<? extends Page>)getParentPageClass(), pageParameters);
			comment.setPath(absolutePath);
			if (!getPluginBean().isApplyByAdministrator() || securityFacade.isOwnerOfObject(authorUsername)) {
				comment.setEnabled(true);
			} else {
				getSession().info(getString(COMMENT_WAITING_APPROVAL));
			}
			if (!StringUtils.isEmpty(comment.getMessage())) {
				commentDao.save(comment);
			}

			if (getPluginBean().isSendEmail()) {
				CommentEmailTemplatePage commentMessage = new CommentEmailTemplatePage(absolutePath, comment.getFromUser().getDisplayName(),
					comment.getMessage());
				emailFacade.sendMailToSystem(commentMessage.getSource(), comment.getFromUser().getEmail(), comment.getFromUser().getDisplayName());
			}
			setResponsePage(getParentPageClass(), pageParameters);
		}

		/**
		 * Gets authorUsername.
		 * 
		 * @return authorUsername
		 */
		public String getAuthorUsername() {
			return authorUsername;
		}

		/**
		 * Sets authorUsername.
		 * 
		 * @param authorUsername
		 *            authorUsername
		 */
		public void setAuthorUsername(String authorUsername) {
			this.authorUsername = authorUsername;
		}
	}
}
