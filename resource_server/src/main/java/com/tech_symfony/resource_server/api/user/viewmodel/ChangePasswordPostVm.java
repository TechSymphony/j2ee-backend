package com.tech_symfony.resource_server.api.user.viewmodel;

import com.tech_symfony.resource_server.api.role.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordPostVm(
        @NotEmpty(message = "Current password must not be empty")
        String currentPassword,

        @NotNull(message = "New password must not be empty")
        String newPassword,

        @NotEmpty(message = "Confirm password must not be empty")
        String confirmPassword
)
{
}
