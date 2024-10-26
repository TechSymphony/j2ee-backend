package com.tech_symfony.resource_server.api.donation;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.TimerTask;

@RequiredArgsConstructor
public class HandleUnusedBillDonationTask extends TimerTask {

    private final DonationRepository donationRepository;

    private final Integer donationId;

    @Override
    public void run() {
        Optional<Donation> donationEntity = donationRepository.findById(donationId);
        if (donationEntity.isPresent() && donationEntity.get().getStatus() == DonationStatus.IN_PROGRESS) {
            Donation donation = donationEntity.get();
            donation.setStatus(DonationStatus.HOLDING);
            donationRepository.save(donation);
        }
        cancel();
    }
}