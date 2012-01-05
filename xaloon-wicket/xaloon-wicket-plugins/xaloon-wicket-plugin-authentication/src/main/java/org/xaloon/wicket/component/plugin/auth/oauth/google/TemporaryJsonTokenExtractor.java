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
package org.xaloon.wicket.component.plugin.auth.oauth.google;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.exceptions.OAuthException;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.Token;
import org.scribe.utils.Preconditions;

/**
 * This class will be replaced with scribe API when scribe will support Google OAuth 2.0
 * 
 * @author vytautas r.
 */
public class TemporaryJsonTokenExtractor extends JsonTokenExtractor {
	private Pattern accessTokenPattern = Pattern.compile("\"access_token\"\\s*:\\s*\"(\\S*?)\"");

	@Override
	public Token extract(String response) {
		Preconditions.checkEmptyString(response, "Cannot extract a token from a null or empty String");
		Matcher matcher = accessTokenPattern.matcher(response);
		if (matcher.find()) {
			return new Token(matcher.group(1), "", response);
		} else {
			throw new OAuthException("Cannot extract an acces token. Response was: " + response);
		}
	}
}
