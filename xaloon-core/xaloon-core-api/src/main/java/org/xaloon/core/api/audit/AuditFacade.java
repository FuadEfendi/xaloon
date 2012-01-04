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
package org.xaloon.core.api.audit;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.api.audit.model.AuditEntity;
import org.xaloon.core.api.audit.model.AuditState;

/**
 * Interface is used to audit object via it's annotations
 * 
 * @author vytautas r.
 * 
 */
public interface AuditFacade extends Serializable {
	/**
	 * Read if audit object has required annotation and then generate audit record for that object.
	 * 
	 * @param state
	 *            {@link AuditState} what kind of state auditable object is in
	 * @param auditObject
	 *            auditable object to parse
	 */
	void audit(AuditState state, Object auditObject);

	List<AuditEntity> search(List<String> objectNames, int first, int count);
}
