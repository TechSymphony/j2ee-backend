package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignDetailVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignListVm;
import com.tech_symfony.resource_server.api.campaign.viewmodel.CampaignPostVm;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CampaignMapper {

	CampaignDetailVm entityToCampaignDetailVm (Campaign campaign);
	CampaignListVm entityToCampaignListVm(Campaign campaign);

	Campaign updateCampaignFromDto(CampaignPostVm campaignPostVm, @MappingTarget Campaign campaign);
	Campaign campaignPostVmToCampaign(CampaignPostVm campaignPostVm);

}