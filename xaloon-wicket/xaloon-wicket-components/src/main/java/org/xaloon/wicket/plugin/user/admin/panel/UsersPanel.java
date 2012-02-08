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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.value.ValueMap;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.UserSearchResult;
import org.xaloon.wicket.component.navigation.DecoratedPagingNavigatorContainer;
import org.xaloon.wicket.plugin.user.admin.page.UserSecurityPage;
import org.xaloon.wicket.plugin.user.admin.page.UsersPage;
import org.xaloon.wicket.util.Link;

/**
 * @author vytautas r.
 */
public class UsersPanel extends AbstractAdministrationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String QUERY_PARAM = "q";

	final ValueMap properties = new ValueMap();


	@Inject
	@Named("userFacade")
	private UserFacade userFacade;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param parameters
	 */
	public UsersPanel(String id, PageParameters parameters) {
		super(id, parameters);
	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();

		removeAll();

		// Add paging navigation container with navigation toolbar
		final DecoratedPagingNavigatorContainer<UserSearchResult> dataContainer = new DecoratedPagingNavigatorContainer<UserSearchResult>(
			"container", getCurrentRedirectLink());
		dataContainer.setOutputMarkupId(true);
		addOrReplace(dataContainer);

		// Add blog list data view
		final DataView<UserSearchResult> securityGroupDataView = new DataView<UserSearchResult>("security-users", new JpaUserDetailsDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<UserSearchResult> item) {
				final UserSearchResult user = item.getModelObject();

				// Add link to user details
				PageParameters params = new PageParameters();
				params.add(UsersPage.PARAM_USER_ID, user.getUsername());
				BookmarkablePageLink<Void> userDetailsLink = new BookmarkablePageLink<Void>("details-link", UserSecurityPage.class, params);
				item.add(userDetailsLink);

				// Add username
				userDetailsLink.add(new Label("username", new Model<String>(userFacade.getFullNameForUser(user.getUsername()))));

				// Add checkbox if account enabled
				item.add(new AjaxCheckBox("accountEnabled", new Model<Boolean>(user.isEnabled())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						userFacade.modifyAccountEnabled(user.getUsername(), getModelObject());
						target.add(dataContainer);
					}
				});

				// Add checkbox if account not expired
				item.add(new AjaxCheckBox("accountNonExpired", new Model<Boolean>(user.isAccountNonExpired())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						userFacade.modifyAccountNonExpired(user.getUsername(), getModelObject());
						target.add(dataContainer);
					}
				});

				// Add checkbox if account not locked
				item.add(new AjaxCheckBox("accountNonLocked", new Model<Boolean>(user.isAccountNonLocked())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						userFacade.modifyAccountNonLocked(user.getUsername(), getModelObject());
						target.add(dataContainer);
					}
				});

				// Add checkbox if credentials not expired
				item.add(new AjaxCheckBox("credentialsNonExpired", new Model<Boolean>(user.isCredentialsNonExpired())) {
					private static final long serialVersionUID = 1L;

					@Override
					protected void onUpdate(AjaxRequestTarget target) {
						userFacade.modifyCredentialsNonExpired(user.getUsername(), getModelObject());
						target.add(dataContainer);
					}
				});

			}

		};
		dataContainer.addAbstractPageableView(securityGroupDataView);

		// Add query form
		Form<String> searchForm = new Form<String>("search");
		searchForm.add(new TextField<String>(QUERY_PARAM, new PropertyModel<String>(properties, QUERY_PARAM)));
		searchForm.add(new IndicatingAjaxButton("submit") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				target.add(dataContainer);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
			}
		});
		add(searchForm);
	}

	protected Link getCurrentRedirectLink() {
		return new Link(UsersPage.class, getPageRequestParameters());
	}

	class JpaUserDetailsDataProvider implements IDataProvider<UserSearchResult> {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
		}

		@Override
		public Iterator<UserSearchResult> iterator(int first, int count) {
			String userSearchProperty = properties.getString(QUERY_PARAM);
			Map<String, String> filter = new HashMap<String, String>();
			filter.put(QUERY_PARAM, userSearchProperty);
			return userFacade.findCombinedUsers(filter, first, count).iterator();
		}

		@Override
		public int size() {
			String userSearchProperty = properties.getString(QUERY_PARAM);
			Map<String, String> filter = new HashMap<String, String>();
			filter.put(QUERY_PARAM, userSearchProperty);
			return userFacade.count(filter);
		}

		@Override
		public IModel<UserSearchResult> model(UserSearchResult object) {
			return new Model<UserSearchResult>(object);
		}
	}
}
