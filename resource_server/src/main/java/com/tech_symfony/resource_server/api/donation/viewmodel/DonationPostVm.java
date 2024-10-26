package com.tech_symfony.resource_server.api.donation.viewmodel;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DonationPostVm(

        @NotNull(message = "Total must not be empty")
        @Positive(message = "Total must greater than 0")
        BigDecimal amountTotal,

        @Size(max = 255, message = "Description cannot be longer than 255 characters")
        @NotNull
        String message,

        @NotNull
        Campaign campaign

) {
}


