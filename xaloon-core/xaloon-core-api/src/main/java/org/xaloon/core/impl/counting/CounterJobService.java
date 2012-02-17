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
package org.xaloon.core.impl.counting;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.asynchronous.RetryAction;
import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.counting.CounterDao;

/**
 * @author vytautas r.
 */
@Named("counterJobService")
public class CounterJobService implements ScheduledJobService<CounterJobParameters> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CounterJobService.class);

	@Inject
	@Named("counterDao")
	private CounterDao counterDao;

	@Override
	public <V> V execute(CounterJobParameters jobParameters, boolean isScheduled) {
		try {
			Boolean result = executeJob(jobParameters);
			if (LOGGER.isDebugEnabled()) {
				if (result == null) {
					LOGGER.debug(String.format("[%s] Could not increment/decrement. Giving up.", Thread.currentThread().getName()));
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return null;
	}


	private Boolean executeJob(final CounterJobParameters jobParameters) throws InterruptedException {
		return new RetryAction<Boolean, CounterJobParameters>(false) {
			private static final long serialVersionUID = 1L;

			@Override
			protected Boolean onPerform(CounterJobParameters parameters) {
				boolean success = false;
				if (jobParameters.isIncrement()) {
					success = counterDao.increment(jobParameters.getCounterGroup(), jobParameters.getCategoryId(), jobParameters.getEntityId());
				} else {
					success = counterDao.decrement(jobParameters.getCounterGroup(), jobParameters.getCategoryId(), jobParameters.getEntityId());
				}
				// In case of failure we return null. It means we retry action
				return (success) ? success : null;
			}

		}.setMillisecondsToSleep(10000).setRetryCount(10).setRandomTimeUsed(true).perform(jobParameters);
	}
}
