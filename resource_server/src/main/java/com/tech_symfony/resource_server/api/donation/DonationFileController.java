package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationExportVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyPutVm;
import com.tech_symfony.resource_server.commonlibrary.exception.AccessDeniedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationFileController {

    private final DonationService donationService;

    @PostMapping("/export")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FileSystemResource> export(@Valid @RequestBody DonationExportVm donationExportVm) throws IOException {
        FileSystemResource file = donationService.export(donationExportVm);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF) // Set MIME type
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"donation-list.pdf\"")
                .body(file);
    }

}
