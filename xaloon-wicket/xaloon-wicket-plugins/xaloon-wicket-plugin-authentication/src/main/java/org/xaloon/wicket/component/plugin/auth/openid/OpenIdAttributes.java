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
package org.xaloon.wicket.component.plugin.auth.openid;

import java.io.Serializable;


public interface OpenIdAttributes extends Serializable {
	
	String SCHEMA_EMAIL = "http://axschema.org/contact/email";
	
	String SCHEMA_FIRST_NAME = "http://axschema.org/namePerson/first";
	
	String SCHEMA_LAST_NAME = "http://axschema.org/namePerson/last";
	
	String SCHEMA_PICTURE_SMALL = "http://axschema.org/media/image/default";
	
	String SCHEMA_PICTURE_BIG = "http://axschema.org/media/image/aspect11";
	
}
