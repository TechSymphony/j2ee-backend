package com.tech_symfony.resource_server.api.campaign;

//import com.tech_symfony.resource_server.api.role.permission.PermissionRepository;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignListVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignPostVm;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

public interface CampaignService {
    Page<CampaignListVm> findAll(  Integer page,
                                   Integer limit,
                                   String sortBy);

    CampaignDetailVm findById(Integer id);

    CampaignDetailVm save(CampaignPostVm campaign);

    CampaignDetailVm update(Integer id, CampaignPostVm campaign);

    Boolean delete(Integer id);
}

@Service
@RequiredArgsConstructor
class DefaultCampaignService implements CampaignService {

    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    @Override
    public Page<CampaignListVm> findAll(Integer page,
                                        Integer limit,
                                        String sortBy) {
        Pageable paging = PageRequest.of(page, limit);
        return campaignRepository.findAll(paging).map(campaignMapper::entityToCampaignListVm);
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
}
