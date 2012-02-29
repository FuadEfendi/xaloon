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
package org.xaloon.core.api.persistence;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Local;


/**
 * Core JPA services providing query/saving/deleting/updating operations
 * 
 * @author vytautas r.
 * @since 1.3
 */
@Local
public interface PersistenceServices extends Serializable {
	/**
	 * Default persistence unit name
	 */
	String DEFAULT_UNIT_NAME = "default-persistence-unit";

	/**
	 * @param <T>
	 * @param o
	 */
	<T> void remove(T o);

	/**
	 * @param <T>
	 * @param clazz
	 * @param id
	 */
	<T> void remove(Class<T> clazz, Long id);

	/**
	 * @param <T>
	 * @param clazz
	 * @param id
	 * @return the found entity instance or null if the entity does not exist
	 */
	<T> T find(Class<T> clazz, Object id);

	/**
	 * @param <T>
	 * @param entity
	 * @return the managed instance that the state was merged to
	 */
	<T extends Persistable> T createOrEdit(T entity);

	/**
	 * Return result for selected query
	 * 
	 * @param <T>
	 * @param query
	 * @return a list of the results
	 */
	<T> List<T> executeQuery(QueryBuilder query);

	/**
	 * Return single row for selected query
	 * 
	 * @param <T>
	 * @param query
	 * @return single result
	 */
	<T> T executeQuerySingle(QueryBuilder query);

	/**
	 * Returns total count for selected entity in database
	 * 
	 * @param <T>
	 * 
	 * @param classForCount
	 *            entity class to select count
	 * @return count of entities in database
	 */
	<T> Long getCount(Class<T> classForCount);

	/**
	 * Uses em.merge method for entity
	 * 
	 * @param <T>
	 * @param entity
	 * @return merged entity
	 */
	<T> T edit(T entity);

	/**
	 * Persists provided entity
	 * 
	 * @param <T>
	 * @param entity
	 * @return the same entity instance, which provided as input
	 */
	<T> T create(T entity);

	/**
	 * @param queryBuilder
	 * @return the number of entities updated or deleted
	 */
	int executeUpdate(QueryBuilder queryBuilder);

	int executeNativeUpdate(String query);

	/**
	 * Executes native SQL query
	 * 
	 * @param <T>
	 * @param query
	 * @return result list
	 */
	<T> List<T> executeNativeQuery(String query);

	/**
	 * @param <T>
	 * @param query
	 * @return concrete single object
	 */
	<T> T executeNativeQuerySingle(String query);

	/**
	 * Synchronize the persistence context to the underlying database.
	 */
	void flush();
}
