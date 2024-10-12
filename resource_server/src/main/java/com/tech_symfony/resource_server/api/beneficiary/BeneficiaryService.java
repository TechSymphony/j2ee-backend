package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

public interface BeneficiaryService {

    List<BeneficiaryListVm> findAll();

    BeneficiaryDetailVm findById(Integer id);

    BeneficiaryDetailVm save(BeneficiaryPostVm beneficiary);

    BeneficiaryDetailVm update(Integer id, BeneficiaryPostVm beneficiary);

    Boolean delete(Integer id);
}

@Service
@RequiredArgsConstructor
class DefaultBeneficiaryService implements BeneficiaryService {

    private final BeneficiaryMapper beneficiaryMapper;
    private final BeneficiaryRepository beneficiaryRepository;
    private final AuthService authService;

    @Override
    public List<BeneficiaryListVm> findAll() {
        return beneficiaryRepository.findAll().stream()
                .map(beneficiaryMapper::entityToBeneficiaryListVm)
                .collect(Collectors.toList());
    }

    public BeneficiaryDetailVm findById(Integer id) {
        return beneficiaryRepository.findById(id)
                .map(beneficiaryMapper::entityToBeneficiaryDetailVm)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public BeneficiaryDetailVm save(BeneficiaryPostVm beneficiary) {
        Beneficiary newBeneficiary = beneficiaryMapper.beneficiaryPostVmToBeneficiary(beneficiary);
        newBeneficiary.setUser(authService.getCurrentUserAuthenticated());
        Beneficiary savedBeneficiary = beneficiaryRepository.save(newBeneficiary);
        return beneficiaryMapper.entityToBeneficiaryDetailVm(savedBeneficiary);
    }

    @Override
    public BeneficiaryDetailVm update(Integer id, BeneficiaryPostVm beneficiary) {
        Beneficiary existingBeneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
        Beneficiary beneficiary1 = beneficiaryMapper.updateBeneficiaryFromDto(beneficiary, existingBeneficiary);
        Beneficiary updatedBeneficiary = beneficiaryRepository.save(beneficiary1);
        return beneficiaryMapper.entityToBeneficiaryDetailVm(updatedBeneficiary);
    }

    @Override
    public Boolean delete(Integer id) {
        Beneficiary beneficiary = beneficiaryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
        beneficiaryRepository.delete(beneficiary);
        return !beneficiaryRepository.existsById(id);
    }
}