package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping
    public List<BeneficiaryListVm> getAllBeneficiaries() {
        return beneficiaryService.findAll();
    }

    @GetMapping("/{id}")
    public BeneficiaryDetailVm getBeneficiaryById(@PathVariable Integer id) {
        return beneficiaryService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BeneficiaryDetailVm createBeneficiary(@Valid @RequestBody BeneficiaryPostVm beneficiary) {
        return beneficiaryService.save(beneficiary);
    }
}
