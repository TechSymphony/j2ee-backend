package com.tech_symfony.resource_server.api.beneficiary.viewmodel;

import com.tech_symfony.resource_server.api.beneficiary.BeneficiaryStatusEnum;
import com.tech_symfony.resource_server.api.user.viewmodel.BasicUserDetailVm;

import java.math.BigDecimal;

public record BeneficiaryListVm(Integer id, BasicUserDetailVm user, String situationDetail, BigDecimal supportReceived, BeneficiaryStatusEnum verificationStatus) {
}
