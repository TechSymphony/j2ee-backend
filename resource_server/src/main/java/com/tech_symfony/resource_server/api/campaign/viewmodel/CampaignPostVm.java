package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.role.permission.Permission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

import java.util.Set;

public record CampaignPostVm(

        @Size(max =255)
        @NotEmpty(message = "Campaign name must not be empty")
        String name,

        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        String description,

        @NotNull(message = "Target amount cannot be null")
        BigDecimal targetAmount,

        @NotNull(message = "Start date cannot be null")
        LocalDate startDate,

        @NotNull(message = "End date cannot be null")
        LocalDate endDate,

        @NotNull(message = "Permissions cannot be null")
        @Size(min = 1, message = "There must be at least one permission")
        Set<Permission> permissions) {
}


