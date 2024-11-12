package com.tech_symfony.resource_server.api.beneficiary.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.BeneficiaryStatusEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record BeneficiaryClientPostVm(
        @Size(max =255)
        @NotEmpty(message = "Situation-Detail must not be empty")
        String situationDetail,

        @NotNull(message = "supportReceived cannot be null")
        BigDecimal supportReceived
)
{
}
