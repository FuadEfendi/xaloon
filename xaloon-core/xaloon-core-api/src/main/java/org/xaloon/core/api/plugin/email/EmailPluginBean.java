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
package org.xaloon.core.api.plugin.email;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.plugin.AbstractPluginBean;

/**
 * @author vytautas r.
 */
public class EmailPluginBean extends AbstractPluginBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean debug = false;

	private boolean requiresAuthentication = false;

	private boolean startTLS = false;

	private String host;

	private String username;

	private String password;

	private int port = 25;

	private String charset = "UTF-8";


	private String fromSubject;

	private String fromEmail;

	private String fromTitle;

	private String toSubject;

	private String toEmail;

	private String toTitle;

	/**
	 * @return true if tls is used
	 */
	public boolean isStartTLS() {
		return startTLS;
	}

	/**
	 * @param startTLS
	 */
	public void setStartTLS(boolean startTLS) {
		this.startTLS = startTLS;
	}

	/**
	 * @return sending emails in debug mode
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
	 * @return does mail host requires authentication
	 */
	public boolean isRequiresAuthentication() {
		return requiresAuthentication;
	}

	/**
	 * @param requiresAuthentication
	 */
	public void setRequiresAuthentication(boolean requiresAuthentication) {
		this.requiresAuthentication = requiresAuthentication;
	}

	/**
	 * @return host for sending emails
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return username when connecting to SMTP host
	 */
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
	 * @return password when connecting to SMTP host
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return port of SMTP host
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return charset of email
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return default email when sending emails from system
	 */
	public String getFromEmail() {
		return fromEmail;
	}

	/**
	 * @param fromEmail
	 */
	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	/**
	 * @return default title when sending emails from system
	 */
	public String getFromTitle() {
		return fromTitle;
	}

	/**
	 * @param fromTitle
	 */
	public void setFromTitle(String fromTitle) {
		this.fromTitle = fromTitle;
	}

	/**
	 * @return default email when sending emails to system
	 */
	public String getToEmail() {
		return toEmail;
	}

	/**
	 * @param toEmail
	 */
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	/**
	 * @return default title when sending emails to system
	 */
	public String getToTitle() {
		return toTitle;
	}

	/**
	 * @param toTitle
	 */
	public void setToTitle(String toTitle) {
		this.toTitle = toTitle;
	}

	/**
	 * @return default subject when sending emails from system
	 */
	public String getFromSubject() {
		return fromSubject;
	}

	/**
	 * @param fromSubject
	 */
	public void setFromSubject(String fromSubject) {
		this.fromSubject = fromSubject;
	}

	/**
	 * @return default subject when sending emails to system
	 */
	public String getToSubject() {
		return toSubject;
	}

	/**
	 * @param toSubject
	 */
	public void setToSubject(String toSubject) {
		this.toSubject = toSubject;
	}

	/**
	 * @return true if minimal requirements are not satisfied
	 */
	public boolean isEmpty() {
		return StringUtils.isEmpty(getHost()) || StringUtils.isEmpty(getToEmail());
	}
}
