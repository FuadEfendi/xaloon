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
package org.xaloon.core.api.security;

import java.io.Serializable;
import java.util.List;

import org.xaloon.core.api.persistence.Persistable;

/**
 * @author vytautas r.
 */
public interface RoleGroupService extends Serializable {

	int getGroupCount();

	List<SecurityGroup> getGroupList(int first, int count);

	List<SecurityRole> getRoleList(int first, int count);

	int getRoleCount();

	SecurityGroup newGroup();

	<T extends Persistable> void save(T entity);

	SecurityRole newRole();

	List<SecurityRole> getRolesByUsername(String username);

	List<SecurityGroup> getGroupsByUsername(String username);

	<T extends SecurityGroup> void assignGroups(UserDetails userDetails, List<T> selections);

	<T extends SecurityRole> void assignRoles(UserDetails userDetails, List<T> selections);

	List<Authority> getAuthorityList(int first, int count);

	<T extends Authority> void assignAuthorities(UserDetails userDetails, List<T> selections);

	UserDetails revokeAuthority(UserDetails userDetails, Authority authority);

	Authority findAuthority(String authorityName);

	Authority newAuthority();

	UserDetails revokeGroup(UserDetails userDetails, SecurityGroup group);

	UserDetails revokeRole(UserDetails userDetails, SecurityRole role);

	SecurityRole getRoleByPath(String path);

	SecurityRole assignAuthorities(SecurityRole role, List<Authority> selections);

	SecurityRole revokeAuthority(SecurityRole role, Authority authority);

	SecurityGroup getGroupByPath(String path);

	SecurityGroup revokeRoleFromGroup(SecurityGroup group, SecurityRole role);

	SecurityGroup assignRolesToGroup(SecurityGroup group, List<SecurityRole> selections);
}
