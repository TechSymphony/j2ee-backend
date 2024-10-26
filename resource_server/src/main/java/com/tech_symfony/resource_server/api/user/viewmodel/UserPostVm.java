package com.tech_symfony.resource_server.api.user.viewmodel;

import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.api.role.permission.Permission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record UserPostVm(
        @NotEmpty(message = "Full name must not be empty")
        String fullName,

        @NotNull(message = "Email must not be empty")
        String email,

        @NotEmpty(message = "Phone must not be empty")
        String phone,

        @NotNull(message = "Role cannot be null")
        Role role,

        @NotEmpty(message = "Username must not be empty")
        String username
)
{
}
