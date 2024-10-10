package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.campaign.beneficiary.Beneficiary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
//import java.util.Date;

public record CampaignListVm(int id, String name, String description, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate startDate, LocalDate endDate, boolean isApproved,
                             Set<Beneficiary> beneficiaries, String code ) {
}
