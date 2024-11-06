package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignListVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignPostVm;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns")
public class CampaignController {

    private final CampaignService campaignService;

    @GetMapping
    public Page<CampaignListVm> getAllCampaigns(@RequestParam Map<String, String> allParams) {
        return campaignService.findAll(allParams);
    }

    @GetMapping("/{id}")
    public CampaignDetailVm getCampaignById(@PathVariable Integer id) {
        return campaignService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignDetailVm createCampaign(@Valid @RequestBody CampaignPostVm campaign) {
        return campaignService.save(campaign);
    }

    @PutMapping("/{id}")
    public CampaignDetailVm updateCampaign(@PathVariable Integer id, @Valid @RequestBody CampaignPostVm campaign) {
        return campaignService.update(id, campaign);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampaign(@PathVariable Integer id) {
        campaignService.delete(id);
    }
}
