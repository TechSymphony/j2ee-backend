package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyPutVm;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;

    @GetMapping
    public DonationPage<DonationListVm> getAllDonations(@RequestParam Map<String, String> allParams) {
        return donationService.findAll(allParams);
    }


    @GetMapping("/{id}")
    public DonationDetailVm getDonationById(@PathVariable Integer id) {
        return donationService.findByIdWithMapper(id);
    }


    @GetMapping("/report")
    public DonationPage<DonationListVm> getReportDonations(@RequestParam Map<String, String> allParams) {
        DonationPage<DonationListVm> donationPage = donationService.findAll(allParams);
        return donationPage;
    }

    @GetMapping("/export")
    public FileSystemResource export() throws IOException {
        return donationService.export();
    }

    @PutMapping(value = "/{id}/payment")
    public DonationDetailVm verify(
            @PathVariable Integer id,
            @Valid DonationVerifyPutVm donationVerifyPutVm
    ) {
        Donation donation = donationService.findById(id);
        if (donation.getIsAbleToPreview()) {
            if (donationVerifyPutVm.donationStatus() == DonationStatus.COMPLETED) {
                return donationService.updateSuccessDonationAndAmountTotalCampaign(donation, donation.getCampaign().getId());
            } else
                return donationService.updateStatus(donation, donationVerifyPutVm.donationStatus());
        } else {
            throw new AccessDeniedException("Thao tác này không hợp lệ");
        }
    }
}
