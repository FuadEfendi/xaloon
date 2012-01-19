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
package org.xaloon.core.impl.asynchronous;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Named;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xaloon.core.api.asynchronous.JobParameters;
import org.xaloon.core.api.asynchronous.ScheduledJobService;
import org.xaloon.core.api.asynchronous.SchedulerServices;

/**
 * @author vytautas r.
 */
@Singleton
@Named("schedulerServices")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DefaultSchedulerServices implements SchedulerServices {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSchedulerServices.class);

	private ExecutorService executor;

	private int fixedThreadCount = 20;

	@SuppressWarnings("unchecked")
	public <V, T extends JobParameters> java.util.concurrent.Future<V> runAsynchronous(final ScheduledJobService<T> scheduledJobService,
		final T jobParameters) {
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("%s.runAsynchronous() start", DefaultSchedulerServices.class.getName()));
		}
		Future<V> f = getExecutor().submit(new Callable<V>() {
			@Override
			public V call() throws Exception {
				return (V)scheduledJobService.execute(jobParameters, true);
			}
		});
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug(String.format("%s.runAsynchronous() end", DefaultSchedulerServices.class.getName()));
		}
		return f;
	};

	ExecutorService getExecutor() {
		if (executor == null) {
			executor = Executors.newFixedThreadPool(fixedThreadCount);
		}
		return executor;
	}

	public DefaultSchedulerServices setFixedThreadCount(int fixedThreadCount) {
		this.fixedThreadCount = fixedThreadCount;
		return this;
	}


}
