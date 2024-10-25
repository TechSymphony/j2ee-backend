package com.tech_symfony.resource_server.api.beneficiary.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.BeneficiaryStatusEnum;
import com.tech_symfony.resource_server.api.user.User;

import java.math.BigDecimal;

public record BeneficiaryDetailVm(Integer id, User user, String situationDetail, BigDecimal supportReceived, BeneficiaryStatusEnum verificationStatus) {
}
