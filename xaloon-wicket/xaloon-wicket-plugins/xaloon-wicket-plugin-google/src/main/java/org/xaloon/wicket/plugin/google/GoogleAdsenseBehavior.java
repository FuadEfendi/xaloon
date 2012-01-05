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
package org.xaloon.wicket.plugin.google;

import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * @author vytautas r.
 */
public class GoogleAdsenseBehavior extends AbstractDefaultAjaxBehavior {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int DEFAULT_WIDTH = 120;

	private static final int DEFAULT_HEIGHT = 240;

	private String adClient;

	private String slot;

	private int adWidth = DEFAULT_WIDTH;

	private int adHeight = DEFAULT_HEIGHT;

	private String createDate = "12/22/08";

	@Override
	protected void respond(AjaxRequestTarget target) {
		throw new UnsupportedOperationException("nothing to do");
	}

	private CharSequence getJavaScript() {
		StringBuilder result = new StringBuilder();
		result.append("<script type=\"text/javascript\"><!--\n");
		result.append("google_ad_client = \"");
		result.append(adClient);
		result.append("\";\n /* ");
		result.append(adWidth);
		result.append("x");
		result.append(adHeight);
		result.append(", created ");
		result.append(createDate);
		result.append(" */\n google_ad_slot = \"");
		result.append(slot);
		result.append("\";\n google_ad_width = ");
		result.append(adWidth);
		result.append(";\n");
		result.append("google_ad_height = ");
		result.append(adHeight);
		result.append(";\n //-->\n </script>\n <script type=\"text/javascript\"\n");
		result.append("src=\"http://pagead2.googlesyndication.com/pagead/show_ads.js\">\n </script>");
		return result.toString();
	}

	/**
	 * @return ad client id
	 */
	public String getAdClient() {
		return adClient;
	}

	/**
	 * @param adClient
	 */
	public void setAdClient(String adClient) {
		this.adClient = adClient;
	}

	/**
	 * @return slot id
	 */
	public String getSlot() {
		return slot;
	}

	/**
	 * @param slot
	 */
	public void setSlot(String slot) {
		this.slot = slot;
	}

	/**
	 * @return ad width
	 */
	public int getAdWidth() {
		return adWidth;
	}

	/**
	 * @param adWidth
	 */
	public void setAdWidth(int adWidth) {
		this.adWidth = adWidth;
	}

	/**
	 * @return ad height
	 */
	public int getAdHeight() {
		return adHeight;
	}

	/**
	 * @param adHeight
	 */
	public void setAdHeight(int adHeight) {
		this.adHeight = adHeight;
	}

	/**
	 * @return ad creation date taken from ad script
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
}
