package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.role.permission.Permission;

import java.time.LocalDate;
import java.util.Set;

public record CampaignDetailVm(int id, String name, String description, float targetAmount, float currentAmount, LocalDate startDate, LocalDate endDate, boolean isApproved) {
}
