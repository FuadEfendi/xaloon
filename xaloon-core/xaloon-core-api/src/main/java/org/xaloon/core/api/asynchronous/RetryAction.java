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

	private boolean exceptionErrorLevel = false;

	/**
	 * Construct.
	 * 
	 * @param sleepFirst
	 */
	public RetryAction(boolean sleepFirst) {
		this.sleepFirst = sleepFirst;
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
			timeToSleep = new Random().nextInt(timeToSleep) + timeToSleep;
		}
		while (result == null && i++ < retryCount) {
			if (sleepFirst) {
				if (LOGGER.isWarnEnabled()) {
					LOGGER.warn(String.format("[%s]: [%d] Sleeping for %s s(%s)", Thread.currentThread().getName(), i,
						DateFormatUtils.format(timeToSleep, "ss:SSS"), parameters));
				}
				Thread.sleep(timeToSleep);
			}
			try {
				result = onPerform(parameters);
			} catch (Exception e) {
				if (exceptionErrorLevel) {
					LOGGER.error("Action thrown an exception!", e);
				} else {
					LOGGER.debug("Action thrown an exception!", e);
				}
			}
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

	/**
	 * Sets sleepFirst.
	 * 
	 * @param sleepFirst
	 *            sleepFirst
	 * @return this instance
	 */
	public RetryAction<T, Z> setSleepFirst(boolean sleepFirst) {
		this.sleepFirst = sleepFirst;
		return this;
	}

	/**
	 * Sets millisecondsToSleep.
	 * 
	 * @param millisecondsToSleep
	 *            millisecondsToSleep
	 * @return this instance
	 */
	public RetryAction<T, Z> setMillisecondsToSleep(int millisecondsToSleep) {
		this.millisecondsToSleep = millisecondsToSleep;
		return this;
	}

	/**
	 * Sets retryCount.
	 * 
	 * @param retryCount
	 *            retryCount
	 * @return this instance
	 */
	public RetryAction<T, Z> setRetryCount(int retryCount) {
		this.retryCount = retryCount;
		return this;
	}

	/**
	 * Sets randomTimeUsed.
	 * 
	 * @param randomTimeUsed
	 *            randomTimeUsed
	 * @return this instance
	 */
	public RetryAction<T, Z> setRandomTimeUsed(boolean randomTimeUsed) {
		this.randomTimeUsed = randomTimeUsed;
		return this;
	}

	/**
	 * Sets exceptionErrorLevel.
	 * 
	 * @param exceptionErrorLevel
	 *            exceptionErrorLevel
	 * @return this instance
	 */
	public RetryAction<T, Z> setExceptionErrorLevel(boolean exceptionErrorLevel) {
		this.exceptionErrorLevel = exceptionErrorLevel;
		return this;
	}
}
