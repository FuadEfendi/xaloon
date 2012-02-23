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
package org.xaloon.wicket.plugin.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.config.Configuration;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.plugin.email.EmailFacade;
import org.xaloon.core.api.plugin.email.EmailService;
import org.xaloon.core.api.resource.StringResourceLoader;
import org.xaloon.core.api.security.LoginService;
import org.xaloon.core.api.security.model.Authority;
import org.xaloon.core.api.security.model.SecurityRole;
import org.xaloon.core.api.security.model.UserDetails;
import org.xaloon.core.api.storage.FileDescriptor;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.user.UserFacade;
import org.xaloon.core.api.user.UserSearchResult;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;
import org.xaloon.wicket.plugin.email.template.EmailContentTemplatePage;
import org.xaloon.wicket.plugin.user.template.ExternalRegistrationEmailTemplatePage;
import org.xaloon.wicket.plugin.user.template.GenerateNewPasswordEmailTemplatePage;
import org.xaloon.wicket.plugin.user.template.RegistrationEmailTemplatePage;

/**
 * @author vytautas r.
 */
@Named("userFacade")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefaultUserFacade implements UserFacade {
	private static final String SUBJECT = "SUBJECT";
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUserFacade.class);

	@Inject
	@Named("loginService")
	private LoginService loginService;

	@Inject
	@Named("userDao")
	private UserDao userDao;

	@Inject
	private EmailFacade emailFacade;

	@Inject
	private FileRepositoryFacade fileRepositoryFacade;

	@Inject
	private StringResourceLoader stringResourceLoader;

	@Override
	public <T extends User> String registerUser(T user, String password, boolean active, KeyValue<String, String> alias) {
		try {
			userDao.save(user);
		} catch (Exception e) {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn(String.format("User with username '%s' seems to be existing. Ignoring ...", user.getUsername()), e);
			}
			return null;
		}
		String activationKey = loginService.registerNewLogin(user.getUsername(), password, active, alias);

		if (emailFacade.isEnabled()) {
			EmailContentTemplatePage contentTemplate;
			if (!active) {
				contentTemplate = new RegistrationEmailTemplatePage(user.getUsername(), password, activationKey);
			} else {
				// If user registered as active then we do send email without activation key
				contentTemplate = new ExternalRegistrationEmailTemplatePage(user.getUsername(), password);
			}
			String subject = stringResourceLoader.getString(RegistrationEmailTemplatePage.class, SUBJECT);
			// TODO
			// 1. add expiry for activation key; //create scheduler with 1 week
			// 2. send activation email //create scheduler with 5 seconds
			emailFacade.sendMailFromSystem(contentTemplate.getSource(), subject, user.getEmail(), user.getDisplayName());
		} else {
			if (LOGGER.isWarnEnabled()) {
				LOGGER.warn("[" + user.getUsername() + "]Registration email could not be sent due to system configuration restrictions!");
			}
		}
		return activationKey;
	}

	@Override
	public <T extends User> T save(T user) {
		FileDescriptor thumbnail = user.getPhotoThumbnail();
		boolean storeThumbnail = thumbnail != null && thumbnail.getId() == null;
		if (storeThumbnail) {
			fileRepositoryFacade.storeFile(thumbnail, thumbnail.getImageInputStreamContainer());
		}
		return userDao.save(user);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T getUserByEmail(String email) {
		return (T)userDao.getUserByEmail(email);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T getUserByUsername(String username) {
		return (T)userDao.getUserByUsername(username);
	}

	@Override
	public boolean performLogin(String username, String password) {
		return false;
	}

	@Override
	public String registerNewLogin(String username, String password) {
		return null;
	}

	@Override
	public boolean activate(String activationKey, String userPassword) {
		return loginService.activate(activationKey, userPassword);
	}

	@Override
	public boolean isUsernameRegistered(String username) {
		return loginService.isUsernameRegistered(username);
	}

	@Override
	public String generateNewPassword(String username) {
		return loginService.generateNewPassword(username);
	}

	@Override
	public boolean isValidPassword(String username, String password) {
		return loginService.isValidPassword(username, password);
	}

	@Override
	public boolean changePassword(String username, String new_password) {
		return loginService.changePassword(username, new_password);
	}

	@Override
	public String registerNewLogin(String username, String password, boolean active, KeyValue<String, String> alias) {
		return null;
	}

	@Override
	public void addAlias(String username, KeyValue<String, String> alias) {
		loginService.addAlias(username, alias);
	}

	@Override
	public void removeAlias(String currentUsername, String loginType) {
		loginService.removeAlias(currentUsername, loginType);
	}

	@Override
	public UserDetails loadUserDetails(String username) {
		return loginService.loadUserDetails(username);
	}

	@Override
	public String sendNewPassword(String email) {
		User user = userDao.getUserByEmail(email);
		if (user != null) {
			String password = generateNewPassword(user.getUsername());
			if (!StringUtils.isEmpty(password)) {
				// send email
				EmailContentTemplatePage contentTemplate = new GenerateNewPasswordEmailTemplatePage(user.getUsername(), password);
				String subject = stringResourceLoader.getString(GenerateNewPasswordEmailTemplatePage.class, SUBJECT);
				if (emailFacade.sendMailFromSystem(contentTemplate.getSource(), subject, user.getEmail(), user.getDisplayName())) {
					return null;
				} else {
					return EmailService.EMAIL_PROPERTIES_NOT_CONFIGURED;
				}
			}
		}
		return EMAIL_VALIDATION_ERROR;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T newUser() {
		return (T)userDao.newUser();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T newAnonymousUser() {
		return (T)userDao.newAnonymousUser();
	}

	@Override
	public <T extends User> T newAnonymousUser(T currentUser) {
		return userDao.newAnonymousUser(currentUser);
	}

	@Override
	public int count(Map<String, String> filter) {
		return userDao.count(filter);
	}

	@Override
	public List<UserSearchResult> findCombinedUsers(Map<String, String> filter, int first, int count) {
		List<User> users = userDao.findUsers(filter, first, count);
		return transform(users);
	}

	private List<UserSearchResult> transform(List<User> users) {
		List<UserSearchResult> result = new ArrayList<UserSearchResult>();
		for (User user : users) {
			UserDetails details = loginService.loadUserDetails(user.getUsername());
			result.add(format(user, details));
		}
		return result;
	}

	private UserSearchResult format(User user, UserDetails details) {
		UserSearchResult entry = new UserSearchResult();
		entry.setAccountNonExpired(details.isAccountNonExpired());
		entry.setAccountNonLocked(details.isAccountNonLocked());
		entry.setCredentialsNonExpired(details.isCredentialsNonExpired());
		entry.setEnabled(details.isEnabled());
		entry.setFirstName(user.getFirstName());
		entry.setLastName(user.getLastName());
		entry.setUsername(user.getUsername());
		entry.setFullName(userDao.formatFullName(entry.getFirstName(), entry.getLastName()));
		return entry;
	}

	@Override
	public String getFullNameForUser(String username) {
		return userDao.getFullNameForUser(username);
	}

	@Override
	public List<Authority> getIndirectAuthoritiesForUsername(String username) {
		return loginService.getIndirectAuthoritiesForUsername(username);
	}

	@Override
	public List<SecurityRole> getIndirectRolesForUsername(String username) {
		return loginService.getIndirectRolesForUsername(username);
	}

	@Override
	public UserDetails modifyCredentialsNonExpired(String username, Boolean newPropertyValue) {
		return loginService.modifyCredentialsNonExpired(username, newPropertyValue);
	}

	@Override
	public UserDetails modifyAccountNonLocked(String username, Boolean newPropertyValue) {
		return loginService.modifyAccountNonLocked(username, newPropertyValue);
	}

	@Override
	public UserDetails modifyAccountNonExpired(String username, Boolean newPropertyValue) {
		return loginService.modifyAccountNonExpired(username, newPropertyValue);
	}

	@Override
	public UserDetails modifyAccountEnabled(String username, Boolean newPropertyValue) {
		return loginService.modifyAccountEnabled(username, newPropertyValue);
	}

	@Override
	public String formatFullName(String firstName, String lastName) {
		return userDao.formatFullName(firstName, lastName);
	}

	@Override
	public List<User> findUsers(Map<String, String> filter, int first, int count) {
		return userDao.findUsers(filter, first, count);
	}

	@Override
	public boolean deleteUser(String username) {
		Configuration.get().getUserListenerCollection().onBeforeDelete(username);
		if (userDao.deleteUser(username)) {
			return loginService.deleteUser(username);
		}
		return false;
	}
}
