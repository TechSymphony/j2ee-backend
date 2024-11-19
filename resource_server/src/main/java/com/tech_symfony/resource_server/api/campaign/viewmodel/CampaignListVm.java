package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.campaign.CampaignsStatusEnum;
import com.tech_symfony.resource_server.api.categories.Category;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public record CampaignListVm(int id, String name, String description, BigDecimal targetAmount, BigDecimal currentAmount,
                             LocalDate startDate, LocalDate endDate, CampaignsStatusEnum status, String code,
                             Beneficiary beneficiary, Category category, boolean disabledAt, boolean isReachTarget, Instant createAt,
                             Integer numberOfDonations) {
}
