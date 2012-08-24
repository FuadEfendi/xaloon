package org.xaloon.core.security.shiro;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.xaloon.core.api.keyvalue.KeyValue;
import org.xaloon.core.api.security.SecurityAuthorities;
import org.xaloon.core.api.security.SecurityFacade;
import org.xaloon.core.api.security.external.AuthenticationAttribute;
import org.xaloon.core.api.security.external.AuthenticationToken;
import org.xaloon.core.api.security.model.UserDetails;
import org.xaloon.core.api.user.dao.UserDao;
import org.xaloon.core.api.user.model.User;
import org.xaloon.core.api.util.DefaultKeyValue;
import org.xaloon.core.security.shiro.util.PrincipalResolver;

/**
 * @author vytautas.r
 */
@Named
public class ShiroSecurityFacade implements SecurityFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("userDao")
	private UserDao userDao;
	
	@Override
	public AuthenticationToken authenticate(AuthenticationToken token) {
		ShiroAuthenticationToken externalTokenToAuthenticate = new ShiroAuthenticationToken(token);
		return authenticateInternal(externalTokenToAuthenticate);
	}

	@Override
	public AuthenticationToken authenticate(String username, String password) {
		final UsernamePasswordToken token = new UsernamePasswordToken(username, password, false);
		return authenticateInternal(token);
	}

	private AuthenticationToken authenticateInternal(org.apache.shiro.authc.AuthenticationToken token) {
		boolean authenticated = false;
		String errorMessage = null;
		String username = null;
		Object o = token.getPrincipal();
		if (o instanceof AuthenticationToken) {
			username = ((AuthenticationToken)o).getName();
		} else {
			username = (String)token.getPrincipal();
		}
		final Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
			authenticated = true;
			UserDetails details = getUserDetails();
			if (details != null) {
				username = details.getUsername();
			}
		} catch (final IncorrectCredentialsException ice) {
			errorMessage = INVALID_USERNAME_PASSWORD;
		} catch (final UnknownAccountException uae) {
			errorMessage = NO_ACCOUNT_FOR_USERNAME;
		} catch (final AuthenticationException ae) {
			if (ae.getCause() == null) {
				errorMessage = ae.getMessage();
			} else {
				errorMessage = ae.getCause().getMessage();
			}
		} catch (final Exception ex) {
			errorMessage = LOGIN_FAILED;
		}
		if (authenticated) {
			AuthenticationToken authentication = new AuthenticationToken(username, new ArrayList<AuthenticationAttribute>());
			authentication.setDetails(userDao.getUserByUsername(authentication.getName()));
			return authentication;
		}
		return new AuthenticationToken(username, errorMessage);
	}

	@Override
	public KeyValue<String, String> getAlias() {
		final Subject currentUser = SecurityUtils.getSubject();
		Object token = currentUser.getPrincipal();

		if (token instanceof AuthenticationToken) {
			AuthenticationToken externalToken = (AuthenticationToken)token;
			return new DefaultKeyValue<String, String>(externalToken.getLoginType(), externalToken.getName());
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends User> T getCurrentUser() {
		final Subject currentUser = SecurityUtils.getSubject();
		if (currentUser == null) {
			return null;
		}
		AuthenticationToken authenticationToken = PrincipalResolver.resolvePrincipal(currentUser.getPrincipal(), AuthenticationToken.class);
		if (authenticationToken != null) {
			return (T)authenticationToken.getDetails();
		}
		return (T)PrincipalResolver.resolvePrincipal(currentUser.getPrincipal(), User.class);
	}

	@Override
	public String getCurrentUserDisplayName() {
		if (getCurrentUser() != null) {
			return getCurrentUser().getDisplayName();
		}
		return null;
	}

	@Override
	public String getCurrentUserEmail() {
		if (getCurrentUser() != null) {
			return getCurrentUser().getEmail();
		}
		return null;
	}

	@Override
	public String getCurrentUsername() {
		UserDetails userDetails = getUserDetails();
		if (userDetails != null) {
			return userDetails.getUsername();
		}
		return null;
	}

	@Override
	public boolean hasAny(String... roles) {
		boolean authorized = false;

		final Subject currentUser = SecurityUtils.getSubject();
		if (currentUser == null) {
			return false;
		}
		for (String role : roles) {
			if (currentUser.hasRole(role)) {
				authorized = true;
				break;
			}
		}
		return authorized;
	}

	@Override
	public boolean isAdministrator() {
		return hasAny(SecurityAuthorities.SYSTEM_ADMINISTRATOR);
	}

	@Override
	public boolean isLoggedIn() {
		final Subject currentUser = SecurityUtils.getSubject();
		return (currentUser != null && currentUser.isAuthenticated());
	}

	@Override
	public boolean isOwnerOfObject(String username) {
		return username.equalsIgnoreCase(getCurrentUsername());
	}

	@Override
	public boolean isRegistered() {
		final Subject currentUser = SecurityUtils.getSubject();
		if (currentUser == null) {
			return false;
		}
		Object principal = currentUser.getPrincipal();
		if (principal instanceof AuthenticationToken) {
			return false;
		}
		return true;
	}

	@Override
	public void logout() {
		SecurityUtils.getSubject().logout();
	}

	@Override
	public List<? extends KeyValue<String, String>> getAliases() {
		UserDetails details = getUserDetails();
		if (details != null) {
			return details.getAliases();
		}
		return new ArrayList<KeyValue<String, String>>();
	}


	private UserDetails getUserDetails() {
		final Subject currentUser = SecurityUtils.getSubject();
		if (currentUser == null) {
			return null;
		}
		return PrincipalResolver.resolvePrincipal(currentUser.getPrincipal(), UserDetails.class);
	}

	@Override
	public void removeAlias(KeyValue<String, String> alias) {
		getAliases().remove(alias);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addAlias(KeyValue<String, String> alias) {
		if (alias == null) {
			return;
		}
		List<KeyValue<String, String>> tmp = (List<KeyValue<String, String>>)getAliases();
		if (tmp != null) {
			tmp.add(alias);
		}
	}
}
