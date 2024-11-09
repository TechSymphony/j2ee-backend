package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignListVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignPostVm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class CampaignClientController {

    private final CampaignService campaignService;

    @GetMapping("/public/campaigns")
    public Page<CampaignListVm> getAllCampaigns(@RequestParam Map<String, String> allParams) {
        allParams.put("status", String.valueOf(CampaignsStatusEnum.APPROVED));
        allParams.put("limit", "6");

        return campaignService.findAll(allParams);
    }

}
