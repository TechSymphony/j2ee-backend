package com.tech_symfony.resource_server.api.user.viewmodel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;


public record BasicUserPostVm(
        @NotEmpty(message = "Full name must not be empty")
        String fullName,

        @NotNull(message = "Email must not be empty")
        String email,

        @NotEmpty(message = "Phone must not be empty")
        String phone
)
{
}
