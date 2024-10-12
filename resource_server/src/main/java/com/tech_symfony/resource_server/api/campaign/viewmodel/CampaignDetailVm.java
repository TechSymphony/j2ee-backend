package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Set;

public record CampaignDetailVm(int id, String name, String description, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate startDate, LocalDate endDate, boolean isApproved,
                               Beneficiary beneficiary, String code) {
}
