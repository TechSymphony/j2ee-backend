package com.tech_symfony.resource_server.api.beneficiary.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.beneficiary.BeneficiaryStatusEnum;
import com.tech_symfony.resource_server.api.campaign.CampaignsStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BeneficiaryPostVm(
        @Size(max =255)
        @NotEmpty(message = "Situation-Detail must not be empty")
        String situationDetail,

        @NotNull(message = "supportReceived cannot be null")
        BigDecimal supportReceived,

        @NotNull(message = "Status cannot be null")
        BeneficiaryStatusEnum  verificationStatus)
{
}
