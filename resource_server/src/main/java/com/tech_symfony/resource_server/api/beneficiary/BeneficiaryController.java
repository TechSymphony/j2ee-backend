package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeneficiaryDetailVm createBeneficiary(@Valid @RequestBody BeneficiaryPostVm beneficiary, Principal principal) {
        return beneficiaryService.save(beneficiary);
    }
}
