package com.tech_symfony.resource_server.api.donation;

import lombok.RequiredArgsConstructor;
import java.util.TimerTask;

@RequiredArgsConstructor
public class HandleUnusedBillDonationTask extends TimerTask {

    private final DonationService donationService;

    private final Integer donationId;

    @Override
    public void run() {
        donationService.sendEventVerify(donationId);

        cancel();
    }
}