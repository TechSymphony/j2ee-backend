package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/me/donations")
public class MyDonationController {

    private final DonationService donationService;
    private final AuthService authService;
    @GetMapping
    public DonationPage<DonationListVm> getAllDonations(@RequestParam Map<String, String> allParams) {
        allParams.put("donor.id", authService.getCurrentUserAuthenticated().getId().toString());
        return donationService.findAll(allParams);
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.donor.username() == authentication.name")
    public DonationDetailVm getDonationById(@PathVariable Integer id) {
        return donationService.findByIdWithMapper(id);
    }

}