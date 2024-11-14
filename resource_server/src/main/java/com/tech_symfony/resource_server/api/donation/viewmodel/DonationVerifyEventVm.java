package com.tech_symfony.resource_server.api.donation.viewmodel;


import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignNameVm;
import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.constant.DonationsFrequencyEnum;

import java.math.BigDecimal;
import java.time.Instant;

public record DonationVerifyEventVm(Integer donationId, Integer campaignId) {

}
