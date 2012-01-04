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

import java.util.ArrayList;
import java.util.List;

import org.xaloon.core.api.tree.TreeNode;

/**
 * Abstract n-tree node holding business data item
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.5
 * 
 * @param <T>
 *            comparable business data object which will be sorted on insertion
 */

public class GenericTreeNode<T extends Comparable<T>> implements TreeNode<T> {
	private static final long serialVersionUID = 1L;

	/**
	 * business data object in tree
	 */
	private T data;

	/**
	 * Parent tree node
	 */
	private TreeNode<T> parent;

	/**
	 * This part is useful when child has many parents
	 */
	private final List<TreeNode<T>> parents = new ArrayList<TreeNode<T>>();

	/**
	 * Children of current tree node
	 */
	private final List<GenericTreeNode<T>> children = new ArrayList<GenericTreeNode<T>>();

	/**
	 * Returns data object of current tree node
	 * 
	 * @return actual object which contains business properties
	 */
	public T getData() {
		return data;
	}

	/**
	 * Registers data object for current tree node
	 * 
	 * @param data
	 *            business data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

	/**
	 * Returns parent tree node
	 * 
	 * @return parent tree. null means that this is a root node
	 */
	public TreeNode<T> getParent() {
		return parent;
	}

	/**
	 * Registers parent tree node
	 * 
	 * @param parent
	 *            node to set. null if this node is a root one
	 */
	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}

	/**
	 * Returns all parents if there are any. Be careful: this should be used only when child has multiple parents
	 * 
	 * @return parent nodes or empty list if there are no any parents
	 */
	public List<TreeNode<T>> getParents() {
		return parents;
	}

	/**
	 * Returns all children of current tree node
	 * 
	 * @return children nodes or empty list if there are no children
	 */
	public List<GenericTreeNode<T>> getChildren() {
		return children;
	}

	/**
	 * Add child to current children list. If there are more than one child in the list then parameter is added at sorted index
	 * 
	 * @param child
	 *            tree node to add
	 */
	public void addChild(GenericTreeNode<T> child) {
		insert(child);
		child.setParent(this);
	}

	private void insert(GenericTreeNode<T> child) {
		if (children.isEmpty()) {
			children.add(child);
			return;
		}
		insertOrdered(child);
	}


	private void insertOrdered(GenericTreeNode<T> child) {
		int position = 0;
		// Get last position by order
		for (GenericTreeNode<T> item : children) {
			if (item.getData().compareTo(child.getData()) >= 0) {
				break;
			}
			position++;
		}
		children.add(position, child);
	}

	@Override
	public boolean hasMoreThanOneChildren() {
		return (!getChildren().isEmpty() && getChildren().size() > 1);
	}

	@Override
	public String toString() {
		return String.format("Parents: %d; Children: %d; Data: %s", parents.size(), children.size(), (data != null) ? data.toString() : "empty");
	}
}
