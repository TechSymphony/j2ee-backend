package com.tech_symfony.resource_server.api.beneficiary.viewmodel;

import java.math.BigDecimal;

public record BeneficiaryPostVm(String situationDetail, BigDecimal supportReceived) {
}
