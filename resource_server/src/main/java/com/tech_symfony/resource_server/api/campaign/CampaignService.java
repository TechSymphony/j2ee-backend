package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignListVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignPostVm;
import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.pagination.GenericPaginationCommand;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

public interface CampaignService {
    Page<CampaignListVm> findAll(Map<String, String> params);

    CampaignDetailVm findById(Integer id);

    CampaignDetailVm save(CampaignPostVm campaign);

    CampaignDetailVm update(Integer id, CampaignPostVm campaign);

    Boolean delete(Integer id);

    void updateTotalByDonation(Donation donation);

    boolean isReachTarget(Integer id);

}

@Service
@RequiredArgsConstructor
class DefaultCampaignService implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final SpecificationBuilderPagination<Campaign> specificationBuilder;
    private final PaginationCommand<Campaign, CampaignListVm> paginationCommand;

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
    public CampaignDetailVm save(CampaignPostVm campaign) {
        Campaign newCampain = campaignMapper.campaignPostVmToCampaign(campaign);
        Campaign savedCampaign = campaignRepository.save(newCampain);
        return campaignMapper.entityToCampaignDetailVm(savedCampaign);
    }

    @Override
    @Transactional
    public CampaignDetailVm update(Integer id, CampaignPostVm campaign) {
        Campaign existingCampaign = campaignRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, id));

        Campaign campaign1 = campaignMapper.updateCampaignFromDto(campaign, existingCampaign);
        Campaign updatedCampaign = campaignRepository.save(
                campaign1
        );
        return campaignMapper.entityToCampaignDetailVm(updatedCampaign);
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
    public void updateTotalByDonation(Donation donation) {

        Campaign campaign = donation.getCampaign();
        campaign.setCurrentAmount(campaign.getCurrentAmount().add(donation.getAmountTotal()));
        campaignRepository.save(campaign);
    }

    @Override
    public boolean isReachTarget(Integer id) {
        return this.findById(id).isReachTarget();
    }


}
