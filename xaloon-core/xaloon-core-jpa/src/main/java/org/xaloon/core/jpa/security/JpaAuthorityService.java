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
package org.xaloon.core.jpa.security;

import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.xaloon.core.api.persistence.PersistenceServices;
import org.xaloon.core.api.persistence.QueryBuilder;
import org.xaloon.core.api.plugin.Plugin;
import org.xaloon.core.api.security.Authority;
import org.xaloon.core.api.security.AuthorityService;
import org.xaloon.core.api.util.UrlUtil;
import org.xaloon.core.jpa.security.model.JpaAuthority;

/**
 * @author vytautas r.
 */
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class JpaAuthorityService implements AuthorityService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private PersistenceServices persistenceServices;

	@Override
	public void registerPermissions(Plugin plugin) {
		List<Authority> authorities = plugin.getSupportedAuthorities();
		for (Authority authority : authorities) {
			findOrCreateAuthority(authority.getName());
		}
	}

	public Authority findOrCreateAuthority(String permission) {
		Authority authority = findAuthority(permission);
		if (authority == null) {
			authority = newAuthority();
			authority.setName(permission);
			save(authority);
		}
		return authority;
	}

	@Override
	public Authority newAuthority() {
		return new JpaAuthority();
	}

	public Authority findAuthority(String authorityName) {
		QueryBuilder queryBuilder = new QueryBuilder("select a from " + JpaAuthority.class.getSimpleName() + " a");
		queryBuilder.addParameter("a.name", "_AUTHORITY_NAME", authorityName);
		return persistenceServices.executeQuerySingle(queryBuilder);
	}

	@Override
	public void save(Authority entity) {
		if (StringUtils.isEmpty(entity.getPath())) {
			entity.setPath(UrlUtil.encode(entity.getName()));
		}
		persistenceServices.create(entity);
	}
}
