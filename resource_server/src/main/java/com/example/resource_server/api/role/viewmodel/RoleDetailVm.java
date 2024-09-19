package com.example.resource_server.api.role.viewmodel;

import com.example.resource_server.api.role.permission.Permission;

import java.util.Set;

public record RoleDetailVm(int id, String name, String description, Set<Permission> permissions) {
}
