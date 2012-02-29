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

import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.asynchronous.SchedulerServices;
import org.xaloon.core.api.counting.CounterDao;
import org.xaloon.core.api.counting.CounterFacade;
import org.xaloon.core.api.inject.ServiceLocator;

/**
 * @author vytautas r.
 */
@Named("counterFacade")
public class DefaultCounterFacade implements CounterFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	@Named("counterDao")
	private CounterDao counterDao;

	@Inject
	@Named("counterJobService")
	private ScheduledJobService<CounterJobParameters> scheduledJobService;

	/**
	 * Manually injected service. This allows to check if exists {@link SchedulerServices} implementation.
	 */
	private SchedulerServices schedulerServices;

	private SchedulerServices getSchedulerServices() {
		if (schedulerServices == null) {
			schedulerServices = ServiceLocator.get().getInstance(SchedulerServices.class);
		}
		return schedulerServices;
	}

	@Override
	public boolean increment(String counterGroup, Long categoryId, Long entityId) {
		// Schedule and start asynchronous job to increment value
		CounterJobParameters params = new CounterJobParameters();
		params.setCounterGroup(counterGroup);
		params.setCategoryId(categoryId);
		params.setEntityId(entityId);

		getSchedulerServices().runAsynchronous(scheduledJobService, params);
		return true;
	}

	@Override
	public Long count(String counterGroup, Long categoryId, Long entityId) {
		return counterDao.count(counterGroup, categoryId, entityId);
	}

	@Override
	public boolean decrement(String counterGroup, Long categoryId, Long entityId) {
		CounterJobParameters params = new CounterJobParameters();
		params.setCounterGroup(counterGroup);
		params.setCategoryId(categoryId);
		params.setEntityId(entityId);
		params.setIncrement(false);
		getSchedulerServices().runAsynchronous(scheduledJobService, params);
		return true;
	}
}
