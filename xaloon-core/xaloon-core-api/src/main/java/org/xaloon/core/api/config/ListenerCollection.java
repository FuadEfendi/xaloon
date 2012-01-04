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
package org.xaloon.core.api.config;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a collection of listeners. Facilitates invocation of events on each listener.
 * <p>
 * Kindly borrowed from Apache Wicket implementation.
 * 
 * @author vytautas r.
 * 
 * @param <T>
 *            type of listeners
 */
public abstract class ListenerCollection<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/** list of listeners */
	private final List<T> listeners = new CopyOnWriteArrayList<T>();

	protected static interface Notifier<T> {
		void notify(T listener);
	}

	/**
	 * Notifies each listener in this
	 * 
	 * @param notifier
	 *            notifier used to notify each listener
	 */
	protected void notify(Notifier<T> notifier) {
		for (T listener : listeners) {
			notifier.notify(listener);
		}
	}

	/**
	 * Adds a listener to this set of listeners.
	 * 
	 * @param listener
	 *            The listener to add
	 * @return {@code true} if the listener was added
	 */
	public boolean add(final T listener) {
		if (listener == null) {
			return false;
		}
		if (listeners.contains(listener)) {
			return false;
		}
		listeners.add(listener);
		return true;
	}
}
