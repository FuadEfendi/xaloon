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
package org.xaloon.core.api.asynchronous;

import java.io.Serializable;
import java.util.Random;

import org.apache.commons.lang.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author vytautas r.
 * @param <T>
 * @param <Z>
 */
public abstract class RetryAction<T, Z> implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RetryAction.class);

	private boolean sleepFirst;

	private int millisecondsToSleep = 10000;

	private int retryCount = 3;

	private boolean randomTimeUsed;

	/**
	 * Construct.
	 * 
	 * @param sleepFirst
	 */
	public RetryAction(boolean sleepFirst) {
		this.sleepFirst = sleepFirst;
	}

	/**
	 * Construct.
	 * 
	 * @param sleepFirst
	 * @param millisecondsToSleep
	 * @param retryCount
	 * @param randomTimeUsed
	 */
	public RetryAction(boolean sleepFirst, int millisecondsToSleep, int retryCount, boolean randomTimeUsed) {
		this.sleepFirst = sleepFirst;
		this.millisecondsToSleep = millisecondsToSleep;
		this.retryCount = retryCount;
		this.randomTimeUsed = randomTimeUsed;
	}

	/**
	 * @param parameters
	 * @return expected result type
	 * @throws InterruptedException
	 */
	public T perform(Z parameters) throws InterruptedException {
		int i = 0;
		T result = null;
		int timeToSleep = millisecondsToSleep;
		if (randomTimeUsed) {
			timeToSleep = new Random().nextInt(timeToSleep);
		}
		while (result == null && i++ < retryCount) {
			if (sleepFirst) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn(String.format("[%s]: [%d] Sleeping for %s s(%s)", Thread.currentThread().getName(), i,
						DateFormatUtils.format(timeToSleep, "ss:SSS"), parameters));
				}
				Thread.sleep(timeToSleep);
			}
			result = onPerform(parameters);
			if (result == null && !sleepFirst) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn(String.format("[%s]: [%d] Sleeping for %s s(%s)", Thread.currentThread().getName(), i,
						DateFormatUtils.format(timeToSleep, "ss:SSS"), parameters));
				}
				Thread.sleep(millisecondsToSleep);
			}
		}
		return result;
	}

	protected abstract T onPerform(Z parameters);
}
