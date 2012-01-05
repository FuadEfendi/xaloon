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
package org.xaloon.wicket.component.resource;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.xaloon.core.api.storage.FileRepositoryFacade;
import org.xaloon.core.api.util.HtmlElementEnum;
import org.xaloon.wicket.util.UrlUtils;

/**
 * http://www.xaloon.org
 * 
 * @author vytautas r.
 */
public class ImageLink extends WebMarkupContainer {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public static final String IMAGE_RESOURCE = "images";

	@Inject
	private FileRepositoryFacade fileRepository;

	private String title;

	private String alternativeText;

	private int width;

	private int height;

	/**
	 * Construct.
	 * 
	 * @param id
	 * @param absoluteImagePath
	 */
	public ImageLink(String id, String absoluteImagePath) {
		super(id, new Model<String>(absoluteImagePath));
		if (Injector.get() != null) {
			Injector.get().inject(this);
		}
		setVisible(false);
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		String url;
		String absoluteImagePath = getDefaultModelObjectAsString();
		if (StringUtils.isEmpty(absoluteImagePath)) {
			return;
		}
		if (!absoluteImagePath.startsWith(HtmlElementEnum.PROTOCOL_HTTP.value()) && !fileRepository.existsFile(absoluteImagePath)) {
			return;
		}
		url = UrlUtils.toAbsoluteImagePath(IMAGE_RESOURCE, absoluteImagePath);

		// Add image title
		if (!StringUtils.isEmpty(title)) {
			add(AttributeModifier.replace(HtmlElementEnum.TITLE.value(), title));
		}

		// check for alternative text
		if (StringUtils.isEmpty(alternativeText)) {
			alternativeText = title;
		}

		// if alternative text exists or title is provided
		if (!StringUtils.isEmpty(alternativeText)) {
			add(AttributeModifier.replace(HtmlElementEnum.ALT.value(), alternativeText));
		}

		// Add image src
		add(AttributeModifier.replace(HtmlElementEnum.SRC.value(), url));

		// Add image width
		if (width > 0) {
			add(AttributeModifier.replace(HtmlElementEnum.WIDTH.value(), String.valueOf(width)));
		}

		// Add image height
		if (height > 0) {
			add(AttributeModifier.replace(HtmlElementEnum.HEIGHT.value(), String.valueOf(height)));
		}

		// Set visibility
		setVisible(!StringUtils.isEmpty(url));
	}

	/**
	 * @param title
	 *            of the image
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets alternativeText.
	 * 
	 * @return alternativeText
	 */
	public String getAlternativeText() {
		return alternativeText;
	}

	/**
	 * Sets alternativeText.
	 * 
	 * @param alternativeText
	 *            alternativeText
	 */
	public void setAlternativeText(String alternativeText) {
		this.alternativeText = alternativeText;
	}
}
