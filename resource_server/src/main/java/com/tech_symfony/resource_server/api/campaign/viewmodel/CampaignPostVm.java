package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.campaign.CampaignsStatusEnum;
import com.tech_symfony.resource_server.api.categories.Category;
import com.tech_symfony.resource_server.api.role.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record CampaignPostVm(

        @Size(max =255)
        @NotEmpty(message = "Campaign name must not be empty")
        String name,

        @Size(max=255)
        @Unique()
        @NotEmpty(message = "Code must not be empty")
        String code,

        @NotEmpty(message = "Description must not be empty")
        String description,

        @NotNull(message = "Target amount cannot be null")
        BigDecimal targetAmount,

        @NotNull(message = "Start date cannot be null")
        LocalDate startDate,

        @NotNull(message = "End date cannot be null")
        LocalDate endDate,

        @Nullable
        Beneficiary beneficiary,

        @NotNull(message = "Category cannot be null")
        Category category,

        @NotNull(message = "Status cannot be null")
        CampaignsStatusEnum status,

        @NotNull(message = "Disabled cannot be null")
        boolean disabledAt)
{
}



