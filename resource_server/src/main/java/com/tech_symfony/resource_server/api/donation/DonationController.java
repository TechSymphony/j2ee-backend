package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public List<DonationListVm> getAllDonations() {
        return donationService.findAll();
    }

}
