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
package org.xaloon.wicket.component.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;

/**
 * @author vytautas r.
 */
public class TimezoneDropDownChoice extends DropDownChoice<String> {
	private static final String ZERO = "0";

	private static final String GMT = "GMT";

	private static final String MINUS = "-";

	private static final String PLUS = "+";

	private static final long serialVersionUID = 1L;

	private static final List<String> availableTimezoneList = new ArrayList<String>();

	static {
		for (int i = -12; i < 14; i++) {
			StringBuilder hour = new StringBuilder();
			hour.append((i > 0) ? PLUS : MINUS);

			if (Math.abs(i) < 10) {
				hour.append(ZERO);
			}
			hour.append(String.valueOf(Math.abs(i)));

			if (i == 0) {
				availableTimezoneList.add(GMT);
			} else {
				availableTimezoneList.add(String.format("GMT%s:00", hour));
			}
		}
	}


	/**
	 * Construct.
	 * 
	 * @param id
	 * @param model
	 */
	public TimezoneDropDownChoice(String id) {
		super(id, Collections.unmodifiableList(availableTimezoneList));
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		setChoiceRenderer(new IChoiceRenderer<String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(String object) {
				return object;
			}

			@Override
			public String getIdValue(String object, int index) {
				return object;
			}
		});
	}
}
