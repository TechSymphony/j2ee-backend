package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DonationMapper {

	DonationListVm entityDonationListVm(Donation donation);
}
