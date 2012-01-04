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
package org.xaloon.core.api.tree;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.impl.plugin.tree.GenericTreeNode;

/**
 * Simple tree node interface
 * 
 * @author vytautas r.
 * @param <T>
 */
public interface TreeNode<T extends Comparable<T>> extends Serializable {
	/**
	 * @return data object of tree node
	 */
	T getData();

	/**
	 * @param dataItem
	 */
	void setData(T dataItem);

	/**
	 * @return single parent node
	 */
	TreeNode<T> getParent();

	/**
	 * @param parentTreeNode
	 */
	void setParent(TreeNode<T> parentTreeNode);

	/**
	 * @return parent node list
	 */
	List<TreeNode<T>> getParents();

	/**
	 * @return children node list
	 */
	List<GenericTreeNode<T>> getChildren();

	/**
	 * Checks if children list has more than one item
	 * 
	 * @return true if children list is not empty and has more than one item
	 */
	boolean hasMoreThanOneChildren();
}
