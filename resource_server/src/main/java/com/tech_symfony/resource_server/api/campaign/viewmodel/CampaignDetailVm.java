package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.campaign.CampaignsStatusEnum;
import com.tech_symfony.resource_server.api.categories.Category;


import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Set;

public record CampaignDetailVm(int id, String name, String description, BigDecimal targetAmount, BigDecimal currentAmount,
                               LocalDate startDate, LocalDate endDate, CampaignsStatusEnum status,
                               Beneficiary beneficiary, Category category, String code,boolean disabledAt,
                               boolean isReachTarget, boolean isExpired, boolean isCampaignStarted,
                               Integer numberOfDonations, String image, String shortDescription) {
}
