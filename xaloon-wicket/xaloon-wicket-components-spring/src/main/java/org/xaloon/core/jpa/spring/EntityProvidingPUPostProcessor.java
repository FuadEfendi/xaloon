/*
 *    xaloon - http://www.xaloon.org
 *    Copyright (C) 2008-2011 vytautas r.
 *
 *    This file is part of xaloon.
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xaloon.core.jpa.spring;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;


/**
 * Spring entity post processor to add entity classes to persistence unit into
 * 
 * @author vytautas r.
 */
public class EntityProvidingPUPostProcessor implements PersistenceUnitPostProcessor, ApplicationContextAware {

	private ApplicationContext applicationContext;
	private static final Logger LOGGER = LoggerFactory.getLogger(EntityProvidingPUPostProcessor.class);

	/**
	 * Construct.
	 */
	public EntityProvidingPUPostProcessor() {
		LOGGER.info("EntityManagingPersistenceUnitPostProcessor was created ");
	}

	public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo mutablePersistenceUnitInfo) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("Processing persistence unit " + mutablePersistenceUnitInfo);
		}

		for (Map.Entry<String, EntityListProvider> entry : applicationContext.getBeansOfType(EntityListProvider.class).entrySet()) {
			EntityListProvider provider = entry.getValue();
			for (String entityClass : provider.getEntities()) {
				if (mutablePersistenceUnitInfo.getManagedClassNames().contains(entityClass)) {
					LOGGER.info("Entity " + entityClass + " is already registered. Duplicate resides in bean named " + entry.getKey());
				} else {
					LOGGER.info("Adding entity " + entityClass + " from bean named " + entry.getKey());
					mutablePersistenceUnitInfo.addManagedClassName(entityClass);
				}
			}
		}

	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
