package com.tech_symfony.resource_server.api.donation.viewmodel;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.api.role.permission.Permission;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.Set;

public record DonationPostVm(

        @NotEmpty(message = "Total name must not be empty")
        BigDecimal amountTotal,

        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        String message,

        @NotNull
        Campaign campaign

) {
}


