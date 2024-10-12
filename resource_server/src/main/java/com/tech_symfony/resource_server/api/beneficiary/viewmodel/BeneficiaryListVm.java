package com.tech_symfony.resource_server.api.beneficiary.viewmodel;

import com.tech_symfony.resource_server.api.user.User;

import java.math.BigDecimal;

public record BeneficiaryListVm(Integer id, User user, String situationDetail, BigDecimal supportReceived, Boolean verificationStatus) {
}
