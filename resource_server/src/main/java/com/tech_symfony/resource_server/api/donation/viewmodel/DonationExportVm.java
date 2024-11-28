package com.tech_symfony.resource_server.api.donation.viewmodel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DonationExportVm(
        @NotNull(message = "Campaign cannot be null")
        int campaign,

        @NotNull(message = "From date field cannot be null")
        LocalDate from,

        @NotNull(message = "To date field cannot be null")
        LocalDate to,

        @NotNull(message = "Student only field cannot be null")
        boolean studentOnly,

        @NotNull(message = "Is anonymous field cannot be null")
        boolean isAnonymous
) {
}
