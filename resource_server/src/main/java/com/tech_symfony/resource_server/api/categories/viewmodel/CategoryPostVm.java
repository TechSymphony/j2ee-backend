package com.tech_symfony.resource_server.api.categories.viewmodel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CategoryPostVm(

        @Size(max =255)
        @NotEmpty(message = "Campaign name must not be empty")
        String name
) {
}


