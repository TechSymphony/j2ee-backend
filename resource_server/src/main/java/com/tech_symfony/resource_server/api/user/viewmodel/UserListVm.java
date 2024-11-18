package com.tech_symfony.resource_server.api.user.viewmodel;

import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.api.role.viewmodel.RoleListVm;

import java.time.Instant;

public record UserListVm(Integer id, String fullName, String email, String phone, Role role, String username, boolean isStudent) {

}
