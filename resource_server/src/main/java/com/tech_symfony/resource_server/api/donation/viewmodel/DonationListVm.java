package com.tech_symfony.resource_server.api.donation.viewmodel;


import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignNameVm;
import com.tech_symfony.resource_server.api.donation.DonationsFrequencyEnum;
import com.tech_symfony.resource_server.api.user.User;

import java.math.BigDecimal;
import java.time.Instant;

public record DonationListVm(int id, User donor, CampaignNameVm campaign, BigDecimal amountBase, BigDecimal amountTotal, Instant donationDate, DonationsFrequencyEnum frequency) {

}
