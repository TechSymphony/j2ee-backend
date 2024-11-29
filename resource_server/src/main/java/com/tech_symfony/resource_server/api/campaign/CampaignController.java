package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.*;
import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/campaigns")
@PreAuthorize("hasPermission('Campaign', 'MANAGE_CAMPAIGNS')")
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
    public CampaignDetailVm createCampaign(@Valid @RequestPart("campaign") CampaignPostVm campaign, @RequestPart("image") MultipartFile image) throws Exception {
        return campaignService.save(campaign, image);
    }

    @PutMapping("/{id}")
    public CampaignDetailVm updateCampaign(@PathVariable Integer id, @Valid @RequestPart("campaign") CampaignPostVm campaign,@RequestPart("old_image") String old_image, @RequestPart("image") MultipartFile image) throws Exception {
        return campaignService.update(id, campaign, old_image, image);
    }

    @PatchMapping("/{id}/disabled")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateDisabledStatus(@PathVariable Integer id, @Valid @RequestBody CampaignDisabledStatusUpdateVm campaignDisabledStatusUpdateVm) {
        campaignService.updateDisabledStatus(id, campaignDisabledStatusUpdateVm);
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Integer id, @Valid @RequestBody CampaignStatusUpdateVm campaignStatusUpdateVm) {
        campaignService.updateStatus(id, campaignStatusUpdateVm);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCampaign(@PathVariable Integer id) {campaignService.delete(id);
    }
}
