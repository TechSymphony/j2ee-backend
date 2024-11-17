package com.tech_symfony.resource_server.api.donation.viewmodel;


import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignNameVm;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.constant.DonationsFrequencyEnum;
import com.tech_symfony.resource_server.api.user.viewmodel.BasicUserDetailVm;

import java.math.BigDecimal;
import java.time.Instant;

public record DonationDetailVm(int id, BasicUserDetailVm donor, CampaignNameVm campaign, BigDecimal amountBase,
                               BigDecimal amountTotal, Instant donationDate, DonationsFrequencyEnum frequency,
                               DonationStatus status) {

}
