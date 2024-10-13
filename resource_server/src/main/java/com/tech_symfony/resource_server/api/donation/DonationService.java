package com.tech_symfony.resource_server.api.donation;


import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

public interface DonationService {
    List<DonationListVm> findAll();
}

@Service
@RequiredArgsConstructor
class DefaultDonationService implements DonationService {

    private  final  DonationRepository donationRepository;
    private  final  DonationMapper donationMapper;

    @Override
    public List<DonationListVm> findAll() {
        return donationRepository.findAll().stream()
                .map(donationMapper::entityDonationListVm)
                .collect(Collectors.toList());
    }
}