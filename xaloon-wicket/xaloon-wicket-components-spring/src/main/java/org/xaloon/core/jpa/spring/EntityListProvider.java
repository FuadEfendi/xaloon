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

/**
 * Simple Spring helper bean to register entities in Spring container
 * 
 * @author vytautas r.
 */
public class EntityListProvider {
	private String[] entities;

	/**
	 * @param entities
	 *            entity classes to register
	 */
	public void setEntities(String[] entities) {
		this.entities = entities;
	}

	/**
	 * @return registered list of entity classes
	 */
	public String[] getEntities() {
		return entities;
	}
}
