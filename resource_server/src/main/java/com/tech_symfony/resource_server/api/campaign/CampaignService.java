package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.*;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.image.ImageService;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface CampaignService {
    List<CampaignListVm> findAllActiveCampaign();

    Page<CampaignListVm> findAll(Map<String, String> params);

    CampaignDetailVm findById(Integer id);

    CampaignDetailVm save(CampaignPostVm campaign, MultipartFile image) throws Exception;

    CampaignDetailVm update(Integer id, CampaignPostVm campaign, String old_image, MultipartFile image) throws Exception;

    Boolean delete(Integer id);

    @Transactional
    void updateTotalByDonation(Integer id, BigDecimal amountTotal);

    boolean isAbleToDonate(Integer id);

    void updateDisabledStatus(Integer id, CampaignDisabledStatusUpdateVm campaignDisabledStatusUpdateVm);

    void updateStatus(Integer id, CampaignStatusUpdateVm campaignStatusUpdateVm);
}

@Service
@RequiredArgsConstructor
class DefaultCampaignService implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final SpecificationBuilderPagination<Campaign> specificationBuilder;
    private final PaginationCommand<Campaign, CampaignListVm> paginationCommand;
    private final ImageService imageService;

    @Override
    public List<CampaignListVm> findAllActiveCampaign() {
        return campaignRepository.findAllByStatus(CampaignsStatusEnum.APPROVED)
                .stream().map(campaignMapper::entityToCampaignListVm).collect(Collectors.toList());
    }

    @Override
    public Page<CampaignListVm> findAll(Map<String, String> params) {
        return paginationCommand.execute(params, campaignRepository, campaignMapper, specificationBuilder);
    }

    @Override
    public CampaignDetailVm findById(Integer id) {
        return campaignRepository.findById(id)
                .map(campaignMapper::entityToCampaignDetailVm)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
    }

    @Override
    public CampaignDetailVm save(CampaignPostVm campaign, MultipartFile image) throws Exception {
        Campaign newCampaign = campaignMapper.campaignPostVmToCampaign(campaign);
        if (image != null && !image.isEmpty()) {
            String imageUrl = imageService.sendImage(image);
            newCampaign.setImage(imageUrl);
        }
        Campaign savedCampaign = campaignRepository.save(newCampaign);
        return campaignMapper.entityToCampaignDetailVm(savedCampaign);
    }


    @Override
    public CampaignDetailVm update(Integer id, CampaignPostVm campaign, String old_image, MultipartFile image) throws Exception {
        Campaign existingCampaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        Campaign updatedCampaign = campaignMapper.updateCampaignFromDto(campaign, existingCampaign);
        if (old_image != null && !old_image.isEmpty()) {
            updatedCampaign.setImage(old_image);
        }
        if (image != null && !image.isEmpty()) {
            String imageUrl = imageService.sendImage(image);
            updatedCampaign.setImage(imageUrl);
        }
        Campaign savedCampaign = campaignRepository.save(updatedCampaign);
        return campaignMapper.entityToCampaignDetailVm(savedCampaign);
    }

    @Override
    @Transactional
    public void updateDisabledStatus(Integer id, CampaignDisabledStatusUpdateVm campaignDisabledStatusUpdateVm) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
        campaign.setDisabledAt(campaignDisabledStatusUpdateVm.disabledAt());
        campaignRepository.save(campaign);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id,  CampaignStatusUpdateVm campaignStatusUpdateVm) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));
        campaign.setStatus(campaignStatusUpdateVm.status());
        campaignRepository.save(campaign);
    }

    @Override
    public Boolean delete(Integer id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

//        if (campaignRepository.existsByUsersRole(id)) {
//            throw new BadRequestException(MessageCode.RESOURCE_NOT_CONTAIN_CHILD_FOR_DELETION);
//        }
        campaignRepository.delete(campaign);
        return !campaignRepository.existsById(id);
    }

    @Override
    @Transactional
    public void updateTotalByDonation(Integer id, BigDecimal amountTotal) {

        // Perform the atomic update directly in the database
        campaignRepository.updateCampaignAmount(id, amountTotal);
    }

    @Override
    public boolean isAbleToDonate(Integer id) {
        CampaignDetailVm campaign = this.findById(id);
        boolean isReachTarget = campaign.isReachTarget();  // Campaign has not reached the target
        boolean isExpired = campaign.isExpired();      // Campaign has not expired
        boolean isCampaignStarted = campaign.isCampaignStarted(); // Campaign has started

        return !isReachTarget && !isExpired && isCampaignStarted;
    }


}
