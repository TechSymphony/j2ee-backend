package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BeneficiaryMapper {
    BeneficiaryDetailVm entityToBeneficiaryDetailVm (Beneficiary beneficiary);
    BeneficiaryListVm entityToBeneficiaryListVm(Beneficiary beneficiary);

    Beneficiary updateBeneficiaryFromDto(BeneficiaryPostVm beneficiaryPostVm, @MappingTarget Beneficiary beneficiary);
    Beneficiary beneficiaryPostVmToBeneficiary(BeneficiaryPostVm beneficiaryPostVm);
}
