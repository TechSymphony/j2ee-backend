package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.system.pagination.GenericMapperPagination;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DonationMapper extends GenericMapperPagination<Donation, DonationListVm> {

	DonationListVm entityDonationListVm(Donation donation);

    DonationDetailVm entityToDonationDetailVm(Donation donation);
}
