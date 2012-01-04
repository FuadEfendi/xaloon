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
package org.xaloon.core.jcr.storage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.StringTokenizer;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Repository;
import javax.jcr.Session;

import org.apache.commons.io.IOUtils;
import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.xaloon.core.api.util.UrlUtil;

/**
 * Jackrabbit helper to create repository
 * 
 * @author vytautas r.
 * @version 1.1, 09/28/10
 * @since 1.3
 */
public class ContentHelper {
	private static final String SEPARATOR = "/";

	/**
	 * Returns node by plugin
	 * 
	 * @param session
	 * @param path
	 * @param nodeKey
	 * @param pluginId
	 * @param create
	 * @return JCR node object
	 * @throws Exception
	 */
	public static Node getNode(Session session, String path, String nodeKey, boolean create) throws Exception {
		Node rootNode = session.getRootNode();
		String fullPath = UrlUtil.mergeIntoUrl(path, nodeKey);

		String[] split = fullPath.split("/");
		Node folderNode = ContentHelper.findOrCreateFolder(session, split, rootNode);

		Node node = null;
		if (folderNode.hasNode(nodeKey)) {
			node = folderNode.getNode(nodeKey);
		} else if (create) {
			node = folderNode.addNode(nodeKey);
		}
		return node;
	}

	/**
	 * Creates JCR repository into provided directory
	 * 
	 * @param url
	 * @return
	 */
	public static Repository createRepository(String url) {
		try {
			final File home = new File(url);
			mkdirs(home);

			File cfg = new File(home, "repository.xml");
			if (!cfg.exists()) {
				copyClassResourceToFile("/org/xaloon/wicket/plugin/jcr/storage/repository.xml", cfg);
			}

			InputStream configStream = new FileInputStream(cfg);
			RepositoryConfig config = RepositoryConfig.create(configStream, home.getAbsolutePath());
			return RepositoryImpl.create(config);
		} catch (Exception e) {
			throw new RuntimeException("Could not create repository: " + url, e);
		}
	}

	/**
	 * Creates folder in JCR repository
	 * 
	 * @param session
	 * @param path
	 * @param rootNode
	 * @return
	 * @throws Exception
	 */
	public static synchronized Node createFolder(Session session, String path, Node rootNode) throws Exception {
		StringTokenizer stringTokenizer = new StringTokenizer(path, SEPARATOR);
		Node result = rootNode;
		while (stringTokenizer.hasMoreTokens()) {
			String nodeStr = stringTokenizer.nextToken();
			if (result.hasNode(nodeStr)) {
				result = result.getNode(nodeStr);
			} else {
				result = result.addNode(nodeStr);
			}
		}
		session.save();
		return result;
	}

	/**
	 * Find or create new path in repository
	 * 
	 * @param session
	 * @param path
	 * @param name
	 * @param rootNode
	 * @return
	 * @throws Exception
	 */
	public static Node findOrCreateFolder(Session session, String[] path, Node rootNode) throws Exception {
		Node result = rootNode;

		for (int i = 0; i < path.length; i++) {
			if (result.hasNode(path[i])) {
				result = result.getNode(path[i]);
			} else {
				result = createFolder(session, path[i], result);
			}
		}
		return result;
	}

	private static void mkdirs(File file) {
		if (!file.exists()) {
			if (!file.mkdirs()) {
				throw new RuntimeException("Could not create directory: " + file.getAbsolutePath());
			}
		}
	}

	private static void copyClassResourceToFile(String source, File destination) {
		final InputStream in = ContentHelper.class.getResourceAsStream(source);
		if (in == null) {
			throw new RuntimeException("Class resource: " + source + " does not exist");
		}

		try {
			final FileOutputStream fos = new FileOutputStream(destination);
			IOUtils.copy(in, fos);
			fos.close();
			in.close();
		} catch (IOException e) {
			throw new RuntimeException("Could not copy class resource: " + source + " to destination: " + destination.getAbsolutePath());
		}
	}

	/**
	 * @param node
	 * @param key
	 * @return
	 */
	public static Property getProperty(Node node, String key) {
		try {
			if (node.hasProperty(key)) {
				return node.getProperty(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param node
	 * @param key
	 * @return
	 */
	public static String getStringProperty(Node node, String key) {
		try {
			Property property = getProperty(node, key);
			if (property != null) {
				return property.getString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param entry
	 * @param key
	 * @return
	 */
	public static Date getDateProperty(Node entry, String key) {
		Property property = getProperty(entry, key);
		try {
			if (property != null) {
				return property.getDate().getTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
