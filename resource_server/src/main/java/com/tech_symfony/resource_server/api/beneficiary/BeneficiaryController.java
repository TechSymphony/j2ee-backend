package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/beneficiaries")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping
    public Page<BeneficiaryListVm> getAllBeneficiaries(@RequestParam Map<String, String> allParams) {
        return beneficiaryService.findAll(allParams);
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

    @PutMapping("/{id}")
    public BeneficiaryDetailVm updateBeneficiary(@PathVariable Integer id, @Valid @RequestBody BeneficiaryPostVm beneficiary) {
        return beneficiaryService.update(id, beneficiary);
    }
}
