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
package org.xaloon.wicket.component.rss;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.wicket.markup.MarkupType;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.Model;

/**
 * @author vytautas r.
 */
public abstract class RssFeed extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("EEE, dd MMMM yyyy HH:mm:ss z");

	/** A XML markup type for sitempa */
	private final static MarkupType XML_MARKUP_TYPE = new MarkupType("xml", "text/html");

	/**
	 * Construct.
	 */
	public RssFeed() {
		List<RssItem> rssItems = getRssItems();
		add(new ListView<RssItem>("item", rssItems) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<RssItem> item) {
				RssItem rssItem = item.getModelObject();
				// Add title
				item.add(new Label("title", new Model<String>(rssItem.getTitle())));

				// Add link
				item.add(new Label("link", new Model<String>(rssItem.getLink())));

				// Add description
				item.add(new Label("description", new Model<String>(rssItem.getDescription())).setEscapeModelStrings(false));

				Label date;
				if (rssItem.getPubDate() != null) {
					date = new Label("pubDate", new Model<String>(DATE_FORMAT.format(rssItem.getPubDate())));
				} else {
					date = new Label("pubDate");
					date.setVisible(false);
				}
				item.add(date);
			}
		});
	}

	/**
	 * Returns list of rss items to be displayed
	 * 
	 * @return list or rss items
	 */
	protected abstract List<RssItem> getRssItems();


	@Override
	public MarkupType getMarkupType() {
		return XML_MARKUP_TYPE;
	}
}
