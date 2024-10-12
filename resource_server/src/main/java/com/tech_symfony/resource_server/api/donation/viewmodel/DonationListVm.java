package com.tech_symfony.resource_server.api.donation.viewmodel;


import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.api.donation.DonationsFrequencyEnum;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

public record DonationListVm(int id, User donor, Campaign campaign, BigDecimal amountBase, BigDecimal amountTotal, Instant donationDate, DonationsFrequencyEnum frequency) {

}
