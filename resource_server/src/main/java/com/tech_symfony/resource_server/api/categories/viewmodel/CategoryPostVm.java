package com.tech_symfony.resource_server.api.categories.viewmodel;

import com.tech_symfony.resource_server.api.categories.Category;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.lang.Nullable;

public record CategoryPostVm(
        @Size(max =255)
        @NotEmpty(message = "Campaign name must not be empty")
        String name,

        @Nullable
        Category parent
) {
}


