package com.tech_symfony.resource_server.api.donation.viewmodel;


import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.validation.ValidDonationStatus;
import jakarta.validation.constraints.NotNull;

public record DonationVerifyPutVm( @NotNull @ValidDonationStatus DonationStatus donationStatus) {

}
