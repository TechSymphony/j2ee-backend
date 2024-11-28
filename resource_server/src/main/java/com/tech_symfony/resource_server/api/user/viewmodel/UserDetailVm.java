package com.tech_symfony.resource_server.api.user.viewmodel;

import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.api.role.permission.Permission;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;

import java.time.Instant;
import java.util.Set;

public record UserDetailVm(Integer id,
                           String fullName,
                           String email,
                           String phone,
                           Role role,
                           String username,
                           boolean isStudent,
                           Boolean enabled) {

}
