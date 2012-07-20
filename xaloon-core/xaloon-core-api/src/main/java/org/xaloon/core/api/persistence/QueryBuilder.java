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
package org.xaloon.core.api.persistence;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple query helper to build JPA/SQL queries
 * 
 * @author vytautas r.
 * @since 1.3
 * 
 */
public class QueryBuilder {
	private static final Logger logger = LoggerFactory.getLogger(QueryBuilder.class);

	/**
	 * Inner join string
	 */
	public static final String INNER_JOIN = " INNER JOIN ";

	/**
	 * outer join string representation
	 */
	public static final String OUTER_JOIN = " LEFT OUTER JOIN ";

	/**
	 * @author vytautas r.
	 */
	public enum Condition {
		/**
		 * And condition
		 */
		AND,

		/**
		 * Or condition
		 */
		OR
	}

	/**
	 * Row to start from. -1 means start from beginning
	 */
	private long firstRow = -1;

	/**
	 * How many rows to select. -1 means select all rows
	 */
	private long count = -1;

	/**
	 * Should cacheable flag be used
	 */
	private boolean cacheable = true;

	private StringBuilder query = new StringBuilder();

	private StringBuilder filter = new StringBuilder();

	private StringBuilder orderBy = new StringBuilder();

	private StringBuilder groupBy = new StringBuilder();

	private Map<String, Object> parameters = new HashMap<String, Object>();

	/**
	 * Construct.
	 * 
	 * @param sql
	 *            initial sql to construct
	 */
	public QueryBuilder(String sql) {
		query.append(sql);
	}

	/**
	 * Add simple parameter
	 * 
	 * @param name
	 *            name of query field
	 * @param parameterName
	 *            parameter name
	 * @param value
	 *            parameter value
	 */
	public void addParameter(String name, String parameterName, Object value) {
		addParameter(name, parameterName, value, Condition.AND, false, false);
	}

	/**
	 * @param name
	 *            name of query field
	 * @param parameterName
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @param isLike
	 *            should parameter be used as LIKE statement
	 */
	public void addParameter(String name, String parameterName, Object value, boolean isLike) {
		addParameter(name, parameterName, value, Condition.AND, isLike, isLike);
	}

	/**
	 * @param name
	 *            name of query field
	 * @param parameterName
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @param isLike
	 *            should parameter be used as LIKE statement
	 * @param isUpper
	 *            if true - parameters will be used in UPPER case
	 */
	public void addParameter(String name, String parameterName, Object value, boolean isLike, boolean isUpper) {
		addParameter(name, parameterName, value, Condition.AND, isLike, isUpper);
	}

	/**
	 * @param name
	 *            name of query field
	 * @param parameterName
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @param condition
	 *            OR/AND condition
	 * @param isLike
	 *            should parameter be used as LIKE statement
	 * @param isUpper
	 *            if true - parameters will be used in UPPER case
	 */
	public void addParameter(String name, String parameterName, Object value, Condition condition, boolean isLike, boolean isUpper) {
		// do not support null values yet
		if (isEmptyValue(value)) {
			return;
		}
		registerParameter(parameterName, value, isLike, isUpper);
		if (filter.length() != 0) {
			filter.append(' ').append(condition.name()).append(' ');
		}
		filter.append('(');
		if (isUpper) {
			filter.append("UPPER(");
		}
		filter.append(name);
		if (isUpper) {
			filter.append(')');
		}
		if (isLike) {
			filter.append(" LIKE ");
		} else {
			filter.append('=');
		}
		if (isUpper) {
			filter.append("UPPER(");
		}
		filter.append(':');
		filter.append(parameterName);
		if (isUpper) {
			filter.append(')');
		}
		filter.append(')');
	}

	private void registerParameter(String parameterName, Object value, boolean isLike, boolean isUpper) {
		if (value instanceof String) {
			StringBuilder parsedValue = new StringBuilder(((String)value).trim());
			if (isLike) {
				parsedValue.insert(0, '%');
				parsedValue.append('%');
			}
			parameters.put(parameterName, parsedValue.toString());
		} else {
			parameters.put(parameterName, value);
		}
	}

	private boolean isEmptyValue(Object value) {
		if (value instanceof String) {
			return StringUtils.isEmpty((String)value);
		}
		return value == null;
	}

	/**
	 * 
	 * @return mapped parameters
	 * 
	 */
	public Map<String, Object> getParameters() {
		return parameters;
	}

	/**
	 * 
	 * @return full generated query
	 */
	public String getQuery() {
		StringBuilder fullQuery = new StringBuilder();
		fullQuery.append(query);
		if (filter.length() != 0) {
			fullQuery.append(" WHERE ");
			fullQuery.append(filter.toString());
		}
		if (groupBy.length() != 0) {
			fullQuery.append(' ');
			fullQuery.append(groupBy);
			fullQuery.append(' ');
		}
		if (orderBy.length() != 0) {
			fullQuery.append(' ');
			fullQuery.append(orderBy);
			fullQuery.append(' ');
		}
		String result = fullQuery.toString();
		if (logger.isDebugEnabled()) {
			logger.debug(result);
			if (!parameters.isEmpty()) {
				StringBuilder params = new StringBuilder();
				for (Map.Entry<String, Object> o : parameters.entrySet()) {
					if (params.length() > 0) {
						params.append(',');
					}
					params.append(o.getKey());
					params.append('=');
					params.append(o.getValue());
				}
				logger.debug("Parameters: " + params);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return getQuery();
	}

	/**
	 * 
	 * @param parameters
	 *            how to order result
	 */
	public void addOrderBy(String parameters) {
		orderBy.append(" ORDER BY ");
		orderBy.append(parameters);
	}

	/**
	 * Add join to main query string
	 * 
	 * @param joinType
	 *            join type - inner join, left outer join
	 * @param joinQuery
	 *            what to join
	 */
	public void addJoin(String joinType, String joinQuery) {
		if (query.indexOf(joinQuery) < 0) {
			query.append(joinType);
			query.append(joinQuery);
		}
	}

	/**
	 * @param expression
	 *            HQL expression to use in HQL statement
	 * @param parameterName
	 *            parameter name
	 * @param value
	 *            parameter value
	 */
	public void addExpression(String expression, String parameterName, Object value) {
		addExpression(expression, parameterName, Condition.AND, value);
	}

	/**
	 * @param expression
	 *            HQL expression to use in HQL statement
	 */
	public void addExpression(String expression) {
		registerExpression(expression, Condition.AND);
	}

	/**
	 * @param expression
	 *            HQL expression to use in HQL statement
	 * @param condition
	 */
	public void addExpression(String expression, Condition condition) {
		registerExpression(expression, condition);
	}

	/**
	 * @param expression
	 *            HQL expression to use in HQL statement
	 * @param parameterName
	 *            parameter name
	 * @param condition
	 *            conditional operation
	 * @param value
	 *            parameter value
	 */
	public void addExpression(String expression, String parameterName, Condition condition, Object value) {
		// do not support null values yet
		if (isEmptyValue(value)) {
			return;
		}
		registerParameter(parameterName, value, false, false);
		registerExpression(expression, condition);
	}

	private void registerExpression(String expression, Condition condition) {
		if (filter.length() != 0) {
			filter.append(' ').append(condition.name()).append(' ');
		}
		filter.append('(');
		filter.append(expression);
		filter.append(')');
	}

	/**
	 * Check if query contains filter parameters
	 * 
	 * @return true if no parameters are set to query
	 */
	public boolean isEmpty() {
		return filter.length() < 1 && parameters.isEmpty();
	}

	/**
	 * @param expression
	 *            HQL expression to use in HQL statement
	 * @param parameterNames
	 *            list of parameter names
	 * @param values
	 *            list of parameter values
	 */
	public void addExpression(String expression, String[] parameterNames, Object[] values) {
		if (values.length < 0) {
			return;
		}
		int i = 0;
		for (String parameterName : parameterNames) {
			registerParameter(parameterName, values[i++], false, false);
		}
		registerExpression(expression, Condition.AND);
	}

	/**
	 * @return startPosition position of the first result, numbered from 0
	 */
	public long getFirstRow() {
		return firstRow;
	}

	/**
	 * 
	 * @param firstRow
	 *            startPosition position of the first result, numbered from 0
	 */
	public void setFirstRow(long firstRow) {
		this.firstRow = firstRow;
	}

	/**
	 * 
	 * @return maximum number of results to retrieve
	 */
	public long getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 *            maximum number of results to retrieve
	 */
	public void setCount(long count) {
		this.count = count;
	}

	/**
	 * @return true if cacheable flag should be used for query
	 */
	public boolean isCacheable() {
		return cacheable;
	}

	/**
	 * 
	 * @param cacheable
	 *            should we use or not cache for this query. default is no.
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	public void addGroup(String parameters) {
		orderBy.append(" GROUP BY ");
		orderBy.append(parameters);
	}
}
