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
package org.xaloon.core.impl.plugin.tree;

import java.util.HashMap;
import java.util.Map;

import org.xaloon.core.api.plugin.AbstractPlugin;
import org.xaloon.core.api.plugin.AbstractPluginBean;


/**
 * Base tree plugin, which holds object hierarchy in memory while application is running. It should be used for dynamic menu panel
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 * 
 * @param <T>
 *            Concrete data object in tree, which contains business information.
 * @param <K>
 */

public abstract class AbstractTreePlugin<K extends AbstractPluginBean, T extends Comparable<T>> extends AbstractPlugin<K> {
	private static final long serialVersionUID = 1L;

	/**
	 * Whole tree of menu items
	 */
	private final GenericTreeNode<T> tree = new GenericTreeNode<T>();

	/**
	 * Tree nodes by concrete url
	 */
	private final Map<String, GenericTreeNode<T>> treeNodesByUrl = new HashMap<String, GenericTreeNode<T>>();

	/**
	 * Tree nodes by concrete context
	 */
	private final Map<String, GenericTreeNode<T>> treeNodesByContext = new HashMap<String, GenericTreeNode<T>>();

	/**
	 * Returns whole tree of business tree items
	 * 
	 * @return object of root tree node
	 */
	public GenericTreeNode<T> getTree() {
		return tree;
	}

	/**
	 * Returns all tree node map, which contains nodes by concrete url
	 * 
	 * @return map container of tree nodes
	 */
	public Map<String, GenericTreeNode<T>> getTreeNodesByUrl() {
		return treeNodesByUrl;
	}

	/**
	 * Returns all tree node map, which contains nodes by concrete context
	 * 
	 * @return map container of tree nodes
	 */
	public Map<String, GenericTreeNode<T>> getTreeNodesByContext() {
		return treeNodesByContext;
	}
}
