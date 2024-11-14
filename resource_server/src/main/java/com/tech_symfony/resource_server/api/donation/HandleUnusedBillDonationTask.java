package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyEventVm;
import lombok.RequiredArgsConstructor;
import java.util.TimerTask;

@RequiredArgsConstructor
public class HandleUnusedBillDonationTask extends TimerTask {

    private final DonationService donationService;

    private final DonationVerifyEventVm donationVerifyEventVm;

    @Override
    public void run() {
        donationService.sendEventVerify(donationVerifyEventVm);

        cancel();
    }
}