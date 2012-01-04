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
package org.xaloon.core.api.image;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;

/**
 * @author vytautas r.
 */
public interface ImageResizer extends Serializable {
	/**
	 * @param is
	 * @param newWidth
	 * @param newHeight
	 * @param isFixed
	 * @return resized image as input stream
	 * @throws IOException
	 */
	InputStream resize(InputStream is, int newWidth, int newHeight, boolean isFixed) throws IOException;

	/**
	 * @param url
	 * @param newWidth
	 * @param newHeight
	 * @param isFixed
	 * @return resized image as input stream
	 * @throws IOException
	 */
	InputStream resize(URL url, int newWidth, int newHeight, boolean isFixed) throws IOException;
}
