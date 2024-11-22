package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationExportVm;
import com.tech_symfony.resource_server.api.categories.viewmodel.CategoryDetailVm;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import jakarta.validation.Valid;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyPutVm;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
@PreAuthorize("hasPermission('Donation', 'MANGE_DONATIONS')")
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
    public Set<DonationStatis> getReportDonations(@RequestParam Map<String, String> allParams) {
//        DonationStatis donationStatis1 = new DonationStatis("01-01-2024", BigDecimal.valueOf(1000));
//        DonationStatis donationStatis2 = new DonationStatis("02-01-2024", BigDecimal.valueOf(2000));
//        DonationStatis donationStatis3 = new DonationStatis("03-01-2024", BigDecimal.valueOf(3000));
//        DonationStatis donationStatis4 = new DonationStatis("04-01-2024", BigDecimal.valueOf(4000));
//        DonationStatis donationStatis5 = new DonationStatis("05-01-2024", BigDecimal.valueOf(5000));
//        DonationStatis donationStatis6 = new DonationStatis("06-01-2024", BigDecimal.valueOf(6000));
//        DonationStatis donationStatis7 = new DonationStatis("07-01-2024", BigDecimal.valueOf(7000));
//        DonationStatis donationStatis8 = new DonationStatis("08-01-2024", BigDecimal.valueOf(8000));
//        DonationStatis donationStatis9 = new DonationStatis("09-01-2024", BigDecimal.valueOf(9000));
//        DonationStatis donationStatis10 = new DonationStatis("10-01-2024", BigDecimal.valueOf(10000));

//        monthly
//        DonationStatis donationStatis1 = new DonationStatis("01-2024", BigDecimal.valueOf(1000));
//        DonationStatis donationStatis2 = new DonationStatis("02-2024", BigDecimal.valueOf(2000));
//        DonationStatis donationStatis3 = new DonationStatis("03-2024", BigDecimal.valueOf(3000));
//        DonationStatis donationStatis4 = new DonationStatis("04-2024", BigDecimal.valueOf(4000));
//        DonationStatis donationStatis5 = new DonationStatis("05-2024", BigDecimal.valueOf(5000));
//        DonationStatis donationStatis6 = new DonationStatis("06-2024", BigDecimal.valueOf(6000));
//        DonationStatis donationStatis7 = new DonationStatis("07-2024", BigDecimal.valueOf(7000));
//        DonationStatis donationStatis8 = new DonationStatis("08-2024", BigDecimal.valueOf(8000));
//        DonationStatis donationStatis9 = new DonationStatis("09-2024", BigDecimal.valueOf(9000));
//        DonationStatis donationStatis10 = new DonationStatis("10-2024", BigDecimal.valueOf(10000));


        // yearly
        DonationStatis donationStatis1 = new DonationStatis("2024", BigDecimal.valueOf(1000));
        DonationStatis donationStatis2 = new DonationStatis("2023", BigDecimal.valueOf(2000));
        DonationStatis donationStatis3 = new DonationStatis("2022", BigDecimal.valueOf(3000));
        DonationStatis donationStatis4 = new DonationStatis("2021", BigDecimal.valueOf(4000));
        DonationStatis donationStatis5 = new DonationStatis("2020", BigDecimal.valueOf(5000));
        DonationStatis donationStatis6 = new DonationStatis("2019", BigDecimal.valueOf(6000));
        DonationStatis donationStatis7 = new DonationStatis("2018", BigDecimal.valueOf(7000));
        DonationStatis donationStatis8 = new DonationStatis("2017", BigDecimal.valueOf(8000));
        DonationStatis donationStatis9 = new DonationStatis("2016", BigDecimal.valueOf(9000));
        DonationStatis donationStatis10 = new DonationStatis("2015", BigDecimal.valueOf(10000));

        Set<DonationStatis> result = new HashSet<>();
        result.add(donationStatis1);
        result.add(donationStatis2);
        result.add(donationStatis3);
        result.add(donationStatis4);
        result.add(donationStatis5);
        result.add(donationStatis6);
        result.add(donationStatis7);
        result.add(donationStatis8);
        result.add(donationStatis9);
        result.add(donationStatis10);
        return result;
//        DonationPage<DonationListVm> donationPage = donationService.findAll(allParams);
//        return donationPage;
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
