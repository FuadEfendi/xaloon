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
package org.xaloon.core.jpa.security.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.xaloon.core.api.security.UserDetails;
import org.xaloon.core.jpa.model.AbstractEntity;

/**
 * @author vytautas r.
 */
@Entity
@Table(name = "XAL_SECURITY_USER", uniqueConstraints = @UniqueConstraint(columnNames = { "USERNAME" }))
public class JpaUserDetails extends AbstractEntity implements UserDetails {
	private static final long serialVersionUID = 1L;

	@Column(name = "USERNAME", nullable = false)
	private String username;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@Column(name = "ACCOUNT_NON_EXPIRED", nullable = false)
	private boolean accountNonExpired;

	@Column(name = "ACCOUNT_NON_LOCKED", nullable = false)
	private boolean accountNonLocked;

	@Column(name = "CREDENTIALS_NON_EXPIRED", nullable = false)
	private boolean credentialsNonExpired;

	@Column(name = "ENABLED", nullable = false)
	private boolean enabled;

	@Column(name = "ACTIVATION_KEY")
	private String activationKey;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "userDetails", orphanRemoval = true)
	private List<JpaUserAlias> aliases = new ArrayList<JpaUserAlias>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "XAL_SECURITY_USER_AUTHORITIES", joinColumns = @JoinColumn(name = "USER_DETAILS_ID", referencedColumnName = "ID"), inverseJoinColumns = @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID"))
	private List<JpaAuthority> authorities = new ArrayList<JpaAuthority>();

	/**
	 * @see UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @see UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 * 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * {@link UserDetails#isAccountNonExpired()}
	 */
	@Override
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	/**
	 * @param accountNonExpired
	 * 
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @see UserDetails#isAccountNonLocked()
	 */
	@Override
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * @param accountNonLocked
	 * 
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @see UserDetails#isCredentialsNonExpired()
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * @param credentialsNonExpired
	 * 
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

	/**
	 * @see UserDetails#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled
	 * 
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @see UserDetails#getAuthorities()
	 */
	public List<JpaAuthority> getAuthorities() {
		return authorities;
	}

	/**
	 * @param authorities
	 */
	public void setAuthorities(List<JpaAuthority> authorities) {
		this.authorities = authorities;
	}

	/**
	 * @return activation key
	 */
	public String getActivationKey() {
		return activationKey;
	}

	/**
	 * @param activationKey
	 */
	public void setActivationKey(String activationKey) {
		this.activationKey = activationKey;
	}

	/**
	 * @return aliases
	 */
	public List<JpaUserAlias> getAliases() {
		return aliases;
	}

	/**
	 * @param aliases
	 */
	public void setAliases(List<JpaUserAlias> aliases) {
		this.aliases = aliases;
	}

	@Override
	public String toString() {
		return String.format("[%s] username=%s, enabled=%b, accountNonExpired=%b, accountNonLocked=%b", this.getClass().getSimpleName(),
			getUsername(), isEnabled(), isAccountNonExpired(), isAccountNonLocked());
	}
}