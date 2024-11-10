package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryDetailVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryPostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/beneficiaries")
public class MyBeneficiaryController {
    private final BeneficiaryService beneficiaryService;
    private final AuthService authService;
    @GetMapping
    public BeneficiaryPage<BeneficiaryListVm> getAllBeneficiaries(@RequestParam Map<String, String> params) {
        params.put("user.id", authService.getCurrentUserAuthenticated().getId().toString());
        return beneficiaryService.findAll(params);
    }

    @PutMapping("/{id}")
    public BeneficiaryDetailVm updateMyBeneficiary(@PathVariable Integer id, @Valid @RequestBody BeneficiaryPostVm beneficiary) {
        return beneficiaryService.update(id,beneficiary);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyBeneficiary(@PathVariable Integer id) {
        beneficiaryService.delete(id);
    }

}