package com.tech_symfony.resource_server.api.donation.viewmodel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DonationExportVm(
        @NotNull(message = "Campaign cannot be null")
        int campaign,

        String type
) {
}
