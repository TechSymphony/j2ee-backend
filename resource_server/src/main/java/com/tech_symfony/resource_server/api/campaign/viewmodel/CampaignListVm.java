package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.campaign.CampaignsStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
public record CampaignListVm(int id, String name, String description, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate startDate, LocalDate endDate, CampaignsStatusEnum status, String code , Beneficiary beneficiary, boolean isReachTarget) {
}
