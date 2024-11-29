// src/main/java/com/tech_symfony/resource_server/api/campaign/viewmodel/CampaignStatusUpdateVm.java
package com.tech_symfony.resource_server.api.campaign.viewmodel;

import com.tech_symfony.resource_server.api.campaign.CampaignsStatusEnum;
import jakarta.validation.constraints.NotNull;

public record CampaignStatusUpdateVm(
        @NotNull(message = "Status cannot be null")
        CampaignsStatusEnum status
) {
}