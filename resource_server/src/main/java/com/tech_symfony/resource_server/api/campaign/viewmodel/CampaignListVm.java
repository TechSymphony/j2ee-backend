package com.tech_symfony.resource_server.api.campaign.viewmodel;

import java.math.BigDecimal;
import java.time.LocalDate;
public record CampaignListVm(int id, String name, String description, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate startDate, LocalDate endDate, boolean isApproved, String code ) {
}
