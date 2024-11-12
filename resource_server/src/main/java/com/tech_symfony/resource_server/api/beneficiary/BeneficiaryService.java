package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryClientPostVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.DonationPage;
import com.tech_symfony.resource_server.api.user.AuthService;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.pagination.GenericPaginationCommand;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface BeneficiaryService {

    BeneficiaryPage<BeneficiaryListVm> findAll(Map<String, String> params);

    Set<BeneficiaryListVm> findAllByUser(Integer userId);

    BeneficiaryDetailVm findById(Integer id);

    BeneficiaryDetailVm save(BeneficiaryClientPostVm beneficiary);

    BeneficiaryDetailVm update(Integer id, BeneficiaryPostVm beneficiary);

    Boolean delete(Integer id);
}

@Service
@RequiredArgsConstructor
class DefaultBeneficiaryService implements BeneficiaryService {

    private final BeneficiaryMapper beneficiaryMapper;
    private final BeneficiaryRepository beneficiaryRepository;
    private final AuthService authService;
    private final SpecificationBuilderPagination<Beneficiary> specificationBuilder;
    private final PaginationCommand<Beneficiary, BeneficiaryListVm> paginationCommand;

//    public BeneficiaryPage<BeneficiaryListVm> findAll(Map<String, String> params) {
//        BigDecimal amountTotal = beneficiaryRepository.s sum(specificationBuilder.buildSpecificationFromParams(params), Donation.class, BigDecimal.class, "amountTotal");
//
////        return paginationCommand.execute(params, beneficiaryRepository, beneficiaryMapper, specificationBuilder);
//        return new BeneficiaryPage<>(paginationCommand.execute(params, beneficiaryRepository, beneficiaryMapper, specificationBuilder));
//
//    }
    public BeneficiaryPage<BeneficiaryListVm> findAll(Map<String, String> params) {

//        return paginationCommand.execute(params, beneficiaryRepository, beneficiaryMapper, specificationBuilder);
        return new BeneficiaryPage<>(paginationCommand.execute(params, beneficiaryRepository, beneficiaryMapper, specificationBuilder));

    }

    @Override
    public Set<BeneficiaryListVm> findAllByUser(Integer userId) {
        return beneficiaryRepository.findAllByUserId(userId)
                .stream()
                .map(beneficiaryMapper::entityToBeneficiaryListVm)
                .collect(Collectors.toSet());
    }

    public BeneficiaryDetailVm findById(Integer id) {
        return beneficiaryRepository.findById(id)
                .map(beneficiaryMapper::entityToBeneficiaryDetailVm)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public BeneficiaryDetailVm save(BeneficiaryClientPostVm beneficiary) {
        Beneficiary newBeneficiary = beneficiaryMapper.beneficiaryClientPostVmToBeneficiary(beneficiary);
        newBeneficiary.setUser(authService.getCurrentUserAuthenticated());
        Beneficiary savedBeneficiary = beneficiaryRepository.save(newBeneficiary);
        return beneficiaryMapper.entityToBeneficiaryDetailVm(savedBeneficiary);
    }

    @Override
    @Transactional
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