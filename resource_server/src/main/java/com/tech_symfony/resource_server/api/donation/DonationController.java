package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.*;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.system.config.JacksonConfig;
import jakarta.validation.Valid;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
@PreAuthorize("hasPermission('Donation', 'MANAGE_DONATIONS')")
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
    public List<DonationStatisticVm> getReportDonations(@RequestParam Map<String, String> allParams) {
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
//        DonationStatis donationStatis1 = new DonationStatis("2024", BigDecimal.valueOf(1000));
//        DonationStatis donationStatis2 = new DonationStatis("2023", BigDecimal.valueOf(2000));
//        DonationStatis donationStatis3 = new DonationStatis("2022", BigDecimal.valueOf(3000));
//        DonationStatis donationStatis4 = new DonationStatis("2021", BigDecimal.valueOf(4000));
//        DonationStatis donationStatis5 = new DonationStatis("2020", BigDecimal.valueOf(5000));
//        DonationStatis donationStatis6 = new DonationStatis("2019", BigDecimal.valueOf(6000));
//        DonationStatis donationStatis7 = new DonationStatis("2018", BigDecimal.valueOf(7000));
//        DonationStatis donationStatis8 = new DonationStatis("2017", BigDecimal.valueOf(8000));
//        DonationStatis donationStatis9 = new DonationStatis("2016", BigDecimal.valueOf(9000));
//        DonationStatis donationStatis10 = new DonationStatis("2015", BigDecimal.valueOf(10000));
//
//        Set<DonationStatis> result = new HashSet<>();
//        result.add(donationStatis1);
//        result.add(donationStatis2);
//        result.add(donationStatis3);
//        result.add(donationStatis4);
//        result.add(donationStatis5);
//        result.add(donationStatis6);
//        result.add(donationStatis7);
//        result.add(donationStatis8);
//        result.add(donationStatis9);
//        result.add(donationStatis10);

        // Extract parameters
        String toDateParam = allParams.get("period_lt");
        String campaignIdParam = allParams.get("campaignId");
        String groupByParam = allParams.getOrDefault("groupBy", "MONTH"); // Default to "MONTH"

        // Validate and parse parameters
        Instant fromDate = allParams.get("period_gt") != null
                ? LocalDateTime.parse(allParams.get("period_gt") + " 00:00:00", JacksonConfig.DATE_FORMATTER).atZone(ZoneId.of(JacksonConfig.MY_TIME_ZONE)).toInstant()
                : LocalDateTime.now()
                .withMonth(1)  // Set the month to January
                .withDayOfMonth(1)  // Set the day to 1
                .withHour(0)  // Set the hour to 00:00
                .withMinute(0)  // Set the minute to 00
                .withSecond(0)  // Set the second to 00
                .atZone(ZoneId.of(JacksonConfig.MY_TIME_ZONE))
                .toInstant();

        Instant toDate = toDateParam != null ?
                LocalDateTime.parse(allParams.get("period_lt") + " 23:59:59", JacksonConfig.DATE_FORMATTER).atZone(ZoneId.of(JacksonConfig.MY_TIME_ZONE)).toInstant()
                : LocalDateTime.now()
                .withMonth(12)  // Set the month to December
                .withDayOfMonth(31)  // Set the day to 31
                .withHour(23)  // Set the hour to 23:59
                .withMinute(59)  // Set the minute to 59
                .withSecond(59)  // Set the second to 59
                .atZone(ZoneId.of(JacksonConfig.MY_TIME_ZONE))
                .toInstant();

        Long campaignId = campaignIdParam != null ? Long.parseLong(campaignIdParam) : null;


        return donationService.getReportByPeriod(fromDate, toDate, campaignId, groupByParam);
//        DonationPage<DonationListVm> donationPage = donationService.findAll(allParams);
//        return donationPage;
    }

    @PutMapping(value = "/{id}/payment")
    public DonationDetailVm verify(
            @PathVariable Integer id,
            @Valid @RequestBody DonationVerifyPutVm donationVerifyPutVm
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
