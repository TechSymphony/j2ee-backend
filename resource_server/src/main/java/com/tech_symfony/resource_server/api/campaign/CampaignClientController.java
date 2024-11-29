package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignListVm;
import com.tech_symfony.resource_server.system.config.JacksonConfig;
import com.tech_symfony.resource_server.api.donation.DonationService;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CampaignClientController {

    private final CampaignService campaignService;
    private final DonationService donationService;

    @GetMapping("/public/campaigns")
    public Page<CampaignListVm> getAllCampaigns(@RequestParam Map<String, String> allParams) {
        allParams.put("status", String.valueOf(CampaignsStatusEnum.APPROVED));

        // only show campaign that started
        allParams.put("startDate_lte", LocalDateTime.now().format(JacksonConfig.DATE_FORMATTER));
        allParams.put("sortBy", "startDate");
        allParams.put("limit", "6");

        return campaignService.findAll(allParams);
    }

    @GetMapping("/public/campaigns/options")
    public List<CampaignListVm> getAllCampaignOptions() {

        return campaignService.findAllActiveCampaign();
    }

    @GetMapping("/public/campaigns/{id}")
    public CampaignDetailVm getCampaignById(@PathVariable Integer id) {
        CampaignDetailVm campaign = campaignService.findById(id);
        if (campaign.status() != CampaignsStatusEnum.APPROVED) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Campaign is not approved");
        }
        return campaign;
    }

    @GetMapping("/public/campaigns/{id}/top-donations")
    public List<DonationListVm> getTopDonationsByCampaignId(@PathVariable Integer id) {
        return donationService.getTopDonationsByCampaignId(id);
    }

    @GetMapping("public/campaigns/{id}/donations")
    public List<DonationListVm> getDonationsByCampaignId(@PathVariable Integer id) {
        return donationService.getDonationsByCampaignId(id);
    }

}
