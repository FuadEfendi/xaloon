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
package org.scribe.model;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.scribe.exceptions.OAuthException;
import org.scribe.utils.MapUtils;
import org.scribe.utils.URLUtils;

/**
 * This class will be replaced with scribe API when scribe will support Google OAuth 2.0
 * 
 * @author vytautas r.
 */
public class CustomRequest {

	private static final String CONTENT_LENGTH = "Content-Length";
	private static final String CONTENT_TYPE = "Content-Type";
	public static final String DEFAULT_CONTENT_TYPE = "application/x-www-form-urlencoded";

	private String url;
	private Verb verb;
	private Map<String, String> querystringParams;
	private Map<String, String> bodyParams;
	private Map<String, String> headers;
	private String payload = null;
	private HttpURLConnection connection;
	private String charset;
	private byte[] bytePayload = null;
	private boolean connectionKeepAlive = false;
	private Long connectTimeout = null;
	private Long readTimeout = null;

	/**
	 * Creates a new Http Request
	 * 
	 * @param verb
	 *            Http Verb (GET, POST, etc)
	 * @param url
	 *            url with optional querystring parameters.
	 */
	public CustomRequest(Verb verb, String url) {
		this.verb = verb;
		this.url = url;
		querystringParams = new HashMap<String, String>();
		bodyParams = new HashMap<String, String>();
		headers = new HashMap<String, String>();
	}

	/**
	 * Execute the request and return a {@link Response}
	 * 
	 * @return Http Response
	 * @throws RuntimeException
	 *             if the connection cannot be created.
	 */
	public Response send() {
		try {
			createConnection();
			return doSend();
		} catch (UnknownHostException uhe) {
			throw new OAuthException("Could not reach the desired host. Check your network connection.", uhe);
		} catch (IOException ioe) {
			throw new OAuthException("Problems while creating connection.", ioe);
		}
	}

	private void createConnection() throws IOException {

		String effectiveUrl = getVerb().equals(Verb.GET) ? URLUtils.appendParametersToQueryString(url, querystringParams) : url;
		if (connection == null) {
			System.setProperty("http.keepAlive", connectionKeepAlive ? "true" : "false");
			connection = (HttpURLConnection)new URL(effectiveUrl).openConnection();
		}
	}

	Response doSend() throws IOException {
		connection.setRequestMethod(verb.name());
		if (connectTimeout != null) {
			connection.setConnectTimeout(connectTimeout.intValue());
		}
		if (readTimeout != null) {
			connection.setReadTimeout(readTimeout.intValue());
		}
		addHeaders(connection);
		if (verb.equals(Verb.PUT) || verb.equals(Verb.POST)) {
			addBody(connection, getByteBodyContents());
		}
		return new Response(connection);
	}

	void addHeaders(HttpURLConnection conn) {
		for (String key : headers.keySet())
			conn.setRequestProperty(key, headers.get(key));
	}

	void addBody(HttpURLConnection conn, byte[] content) throws IOException {
		conn.setRequestProperty(CONTENT_LENGTH, String.valueOf(content.length));

		// Set default content type if none is set.
		if (conn.getRequestProperty(CONTENT_TYPE) == null) {
			conn.setRequestProperty(CONTENT_TYPE, DEFAULT_CONTENT_TYPE);
		}
		conn.setDoOutput(true);
		conn.getOutputStream().write(content);
		if (getVerb().equals(Verb.POST)) {
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(URLUtils.formURLEncodeMap(querystringParams));
			wr.flush();
		}
	}

	/**
	 * Add an HTTP Header to the Request
	 * 
	 * @param key
	 *            the header name
	 * @param value
	 *            the header value
	 */
	public void addHeader(String key, String value) {
		headers.put(key, value);
	}

	/**
	 * Add a body Parameter (for POST/ PUT Requests)
	 * 
	 * @param key
	 *            the parameter name
	 * @param value
	 *            the parameter value
	 */
	public void addBodyParameter(String key, String value) {
		bodyParams.put(key, value);
	}

	/**
	 * Add a QueryString parameter
	 * 
	 * @param key
	 *            the parameter name
	 * @param value
	 *            the parameter value
	 */
	public void addQuerystringParameter(String key, String value) {
		querystringParams.put(key, value);
	}

	/**
	 * Add body payload.
	 * 
	 * This method is used when the HTTP body is not a form-url-encoded string, but another thing. Like for example XML.
	 * 
	 * Note: The contents are not part of the OAuth signature
	 * 
	 * @param payload
	 *            the body of the request
	 */
	public void addPayload(String payload) {
		this.payload = payload;
	}

	/**
	 * Overloaded version for byte arrays
	 * 
	 * @param payload
	 */
	public void addPayload(byte[] payload) {
		bytePayload = payload;
	}

	/**
	 * Get a {@link Map} of the query string parameters.
	 * 
	 * @return a map containing the query string parameters
	 * @throws OAuthException
	 *             if the URL is not valid
	 */
	public Map<String, String> getQueryStringParams() {
		try {
			Map<String, String> params = new HashMap<String, String>();
			String queryString = new URL(url).getQuery();
			params.putAll(MapUtils.queryStringToMap(queryString));
			params.putAll(querystringParams);
			return params;
		} catch (MalformedURLException mue) {
			throw new OAuthException("Malformed URL", mue);
		}
	}

	/**
	 * Obtains a {@link Map} of the body parameters.
	 * 
	 * @return a map containing the body parameters.
	 */
	public Map<String, String> getBodyParams() {
		return bodyParams;
	}

	/**
	 * Obtains the URL of the HTTP Request.
	 * 
	 * @return the original URL of the HTTP Request
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the URL without the port and the query string part.
	 * 
	 * @return the OAuth-sanitized URL
	 */
	public String getSanitizedUrl() {
		return url.replaceAll("\\?.*", "").replace("\\:\\d{4}", "");
	}

	/**
	 * Returns the body of the request
	 * 
	 * @return form encoded string
	 * @throws OAuthException
	 *             if the charset chosen is not supported
	 */
	public String getBodyContents() {
		try {
			return new String(getByteBodyContents(), getCharset());
		} catch (UnsupportedEncodingException uee) {
			throw new OAuthException("Unsupported Charset: " + charset, uee);
		}
	}

	byte[] getByteBodyContents() {
		if (bytePayload != null)
			return bytePayload;
		String body = (payload != null) ? payload : URLUtils.formURLEncodeMap(bodyParams);
		try {
			return body.getBytes(getCharset());
		} catch (UnsupportedEncodingException uee) {
			throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
		}
	}

	/**
	 * Returns the HTTP Verb
	 * 
	 * @return the verb
	 */
	public Verb getVerb() {
		return verb;
	}

	/**
	 * Returns the connection headers as a {@link Map}
	 * 
	 * @return map of headers
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * Returns the connection charset. Defaults to {@link Charset} defaultCharset if not set
	 * 
	 * @return charset
	 */
	public String getCharset() {
		return charset == null ? Charset.defaultCharset().name() : charset;
	}

	/**
	 * Sets the connect timeout for the underlying {@link HttpURLConnection}
	 * 
	 * @param duration
	 *            duration of the timeout
	 * 
	 * @param unit
	 *            unit of time (milliseconds, seconds, etc)
	 */
	public void setConnectTimeout(int duration, TimeUnit unit) {
		connectTimeout = unit.toMillis(duration);
	}

	/**
	 * Sets the read timeout for the underlying {@link HttpURLConnection}
	 * 
	 * @param duration
	 *            duration of the timeout
	 * 
	 * @param unit
	 *            unit of time (milliseconds, seconds, etc)
	 */
	public void setReadTimeout(int duration, TimeUnit unit) {
		readTimeout = unit.toMillis(duration);
	}

	/**
	 * Set the charset of the body of the request
	 * 
	 * @param charsetName
	 *            name of the charset of the request
	 */
	public void setCharset(String charsetName) {
		charset = charsetName;
	}

	/**
	 * Sets wether the underlying Http Connection is persistent or not.
	 * 
	 * @see http://download.oracle.com/javase/1.5.0/docs/guide/net/http-keepalive.html
	 * @param connectionKeepAlive
	 */
	public void setConnectionKeepAlive(boolean connectionKeepAlive) {
		this.connectionKeepAlive = connectionKeepAlive;
	}

	/*
	 * We need this in order to stub the connection object for test cases
	 */
	void setConnection(HttpURLConnection connection) {
		this.connection = connection;
	}

	@Override
	public String toString() {
		return String.format("@Request(%s %s)", getVerb(), getUrl());
	}
}