package com.tech_symfony.resource_server.api.donation.validation;

import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DonationStatusValidator implements ConstraintValidator<ValidDonationStatus, DonationStatus> {

    @Override
    public boolean isValid(DonationStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;  // Since null values are handled by @NotNull
        }

        // Validation logic: donationStatus should not be IN_PROGRESS or HOLDING
        return value != DonationStatus.IN_PROGRESS && value != DonationStatus.HOLDING;
    }
}

