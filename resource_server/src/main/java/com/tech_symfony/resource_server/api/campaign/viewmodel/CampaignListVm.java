package com.tech_symfony.resource_server.api.campaign.viewmodel;

import java.time.LocalDate;
//import java.util.Date;

public record CampaignListVm(int id, String name, String description, float targetAmount, float currentAmount, LocalDate startDate, LocalDate endDate, boolean isApproved) {
}
