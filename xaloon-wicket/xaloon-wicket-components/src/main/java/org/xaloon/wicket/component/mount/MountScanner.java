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
package org.xaloon.wicket.component.mount;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.protocol.http.WebApplication;
import org.xaloon.wicket.component.mount.annotation.MountPage;

/**
 * Abstract mounting class to scan packages for {@link MountPage} annotation
 * 
 * @author vytautas r.
 * 
 * @param <T>
 *            Web application object instance
 */
public abstract class MountScanner<T extends WebApplication> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final MountScannerListenerCollection mountScannerListenerCollection = new MountScannerListenerCollection();

	/**
	 * Mount page using {@link MountPage} annotation
	 * 
	 * @param application
	 *            Web application is used to mount Web pages
	 * @param packageName
	 *            Package name to be scanned for annotation
	 * @return list of mounted web page classes for further usage
	 */
	public abstract List<Class<?>> mountPackage(T application, String packageName);

	/**
	 * @param mountScannerListener
	 *            action listener when mount action is performed
	 */
	public void addMountScannerListener(MountScannerListener mountScannerListener) {
		if (mountScannerListener != null) {
			mountScannerListenerCollection.add(mountScannerListener);
		}
	}

	protected MountScannerListenerCollection getMountScannerListenerCollection() {
		return mountScannerListenerCollection;
	}
}
