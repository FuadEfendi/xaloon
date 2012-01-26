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

import java.util.Iterator;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.api.user.UserFacade;
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
		final DecoratedPagingNavigatorContainer<UserDetails> dataContainer = new DecoratedPagingNavigatorContainer<UserDetails>("container",
			getCurrentRedirectLink());
		dataContainer.setOutputMarkupId(true);
		addOrReplace(dataContainer);

		// Add blog list data view
		final DataView<UserDetails> securityGroupDataView = new DataView<UserDetails>("security-users", new JpaUserDetailsDataProvider()) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(Item<UserDetails> item) {
				UserDetails user = item.getModelObject();

				// Add link to user details
				PageParameters params = new PageParameters();
				params.add(UsersPage.PARAM_USER_ID, user.getUsername());
				BookmarkablePageLink<Void> userDetailsLink = new BookmarkablePageLink<Void>("details-link", UserSecurityPage.class, params);
				item.add(userDetailsLink);

				// Add username
				userDetailsLink.add(new Label("username", new Model<String>(userFacade.getFullNameForUser(user.getUsername()))));

				// Add checkbox if account enabled
				item.add(new CheckBox("accountEnabled", new Model<Boolean>(user.isEnabled())).setEnabled(false));

				// Add checkbox if account not expired
				item.add(new CheckBox("accountNonExpired", new Model<Boolean>(user.isAccountNonExpired())).setEnabled(false));

				// Add checkbox if account not locked
				item.add(new CheckBox("accountNonLocked", new Model<Boolean>(user.isAccountNonLocked())).setEnabled(false));

				// Add checkbox if credentials not expired
				item.add(new CheckBox("credentialsNonExpired", new Model<Boolean>(user.isCredentialsNonExpired())).setEnabled(false));

			}

		};
		dataContainer.addAbstractPageableView(securityGroupDataView);
	}

	protected Link getCurrentRedirectLink() {
		return new Link(UsersPage.class, getPageRequestParameters());
	}

	class JpaUserDetailsDataProvider implements IDataProvider<UserDetails> {
		private static final long serialVersionUID = 1L;

		@Override
		public void detach() {
		}

		@Override
		public Iterator<? extends UserDetails> iterator(int first, int count) {
			return userFacade.findUsers(first, count).iterator();
		}

		@Override
		public int size() {
			return userFacade.count();
		}

		@Override
		public IModel<UserDetails> model(UserDetails object) {
			return new Model<UserDetails>(object);
		}
	}
}
