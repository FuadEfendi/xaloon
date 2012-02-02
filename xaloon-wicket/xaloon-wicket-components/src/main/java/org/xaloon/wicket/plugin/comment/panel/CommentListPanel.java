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

import java.util.Iterator;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.date.DateService;
import org.xaloon.core.api.plugin.comment.Comment;
import org.xaloon.core.api.plugin.comment.CommentDao;
import org.xaloon.core.api.plugin.comment.CommentPluginBean;
import org.xaloon.core.api.plugin.comment.Commentable;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.util.TextUtil;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.component.resource.ImageLink;
import org.xaloon.wicket.plugin.AbstractPluginPanel;
import org.xaloon.wicket.plugin.comment.CommentDetachableModel;
import org.xaloon.wicket.plugin.comment.CommentPlugin;
import org.xaloon.wicket.util.Link;

/**
 * @author vytautas r.
 */
public abstract class CommentListPanel extends AbstractPluginPanel<CommentPluginBean, CommentPlugin> {
	private static final long serialVersionUID = 1L;

	@Inject
	private CommentDao commentDao;

	@Inject
	private DateService dateService;

	@Inject
	private SecurityFacade securityFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param commentableModel
	 * @param pageRequestParameters
	 */
	public CommentListPanel(String id, IModel<Commentable> commentableModel, PageParameters pageRequestParameters) {
		super(id, commentableModel, pageRequestParameters);
		setOutputMarkupId(true);
	}

	@Override
	protected void onInitialize(CommentPlugin plugin, CommentPluginBean pluginBean) {
		Commentable commentable = (Commentable)getDefaultModelObject();
		final DecoratedPagingNavigatorContainer<Comment> dataContainer = new DecoratedPagingNavigatorContainer<Comment>("container",
			getCurrentRedirectLink());
		add(dataContainer);

		final DataView<Comment> commentListDataView = new DataView<Comment>("comment-list", new CommentListDataProvider(commentable)) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<Comment> item) {
				final Comment comment = item.getModelObject();
				WebMarkupContainer externalLink;
				if (getPluginBean().isWebsiteVisible() && !StringUtils.isEmpty(comment.getFromUser().getWebsite())) {
					externalLink = new ExternalLink("external-link", comment.getFromUser().getWebsite());
				} else {
					externalLink = new WebMarkupContainer("external-link");
				}
				item.add(externalLink);
				FileDescriptor authorPhoto = comment.getFromUser().getPhotoThumbnail();

				item.add(new ImageLink("image-link", (authorPhoto != null) ? authorPhoto.getPath() : null).setVisible(authorPhoto != null));
				externalLink.add(new Label("displayName", new Model<String>(comment.getFromUser().getDisplayName())));
				item.add(new Label("message", new Model<String>(TextUtil.prepareStringForHTML(comment.getMessage()))));
				item.add(new Label("comment-timestamp", new Model<String>(dateService.formatWithLongDate(comment.getCreateDate()))));
				item.add(new AjaxLink<Void>("delete-comment") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						commentDao.delete(comment);
						target.add(CommentListPanel.this);
					}

					@Override
					protected IAjaxCallDecorator getAjaxCallDecorator() {
						return new AjaxCallDecorator() {
							private static final long serialVersionUID = 1L;

							@Override
							public CharSequence decorateScript(Component c, CharSequence script) {
								return "if(!confirm('" + CommentListPanel.this.getString(DELETE_CONFIRMATION) + "')) return false;" + script;
							}
						};
					}
				}.setVisible(getSecurityFacade().isAdministrator()));

				// Add inappropriate flag
				item.add(new AjaxLink<Void>("inappropriateFlag") {

					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						commentDao.markAsInappropriate(comment, true);
						target.add(CommentListPanel.this);
					}
				}.setVisible(securityFacade.isLoggedIn() && !comment.isInappropriate()));
			}
		};
		dataContainer.addAbstractPageableView(commentListDataView);
	}

	protected abstract Class<? extends IRequestablePage> getCommentPageClass();

	protected Link getCurrentRedirectLink() {
		return new Link(getCommentPageClass(), getPageRequestParameters());
	}

	private class CommentListDataProvider implements IDataProvider<Comment> {
		private static final long serialVersionUID = 1L;

		private Commentable commentable;

		public CommentListDataProvider(Commentable commentable) {
			this.commentable = commentable;
		}

		@Override
		public void detach() {
		}

		@Override
		public Iterator<? extends Comment> iterator(int arg0, int arg1) {
			return commentDao.getComments(commentable, arg0, arg1).iterator();
		}

		@Override
		public IModel<Comment> model(Comment arg0) {
			return new CommentDetachableModel(arg0.getId());
		}

		@Override
		public int size() {
			return commentDao.count(commentable).intValue();
		}

	}
}
