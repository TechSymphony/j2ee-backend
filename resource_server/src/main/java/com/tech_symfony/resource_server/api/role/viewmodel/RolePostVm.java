package com.tech_symfony.resource_server.api.role.viewmodel;

import com.tech_symfony.resource_server.api.role.permission.Permission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record RolePostVm(
        @NotEmpty(message = "Role name must not be empty")
        String name,

        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        String description,

        @NotNull(message = "Permissions cannot be null")
        @Size(min = 1, message = "There must be at least one permission")
        Set<Permission> permissions) {
}
