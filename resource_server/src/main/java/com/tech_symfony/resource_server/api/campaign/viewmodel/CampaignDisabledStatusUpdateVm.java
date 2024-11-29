package com.tech_symfony.resource_server.api.campaign.viewmodel;

import jakarta.validation.constraints.NotNull;

public record CampaignDisabledStatusUpdateVm(
        @NotNull(message = "Status cannot be null")
        Boolean disabledAt
) {
}