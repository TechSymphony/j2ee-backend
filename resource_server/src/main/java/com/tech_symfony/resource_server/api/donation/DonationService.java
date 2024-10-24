package com.tech_symfony.resource_server.api.donation;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.api.campaign.CampaignRepository;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.api.donation.vnpay.VnpayService;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.api.user.UserRepository;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public interface DonationService {
    List<DonationListVm> findAll();

    Donation create(DonationPostVm donationPostVm, String username);

    Donation pay(int donationID, String username);


}

@Service
@RequiredArgsConstructor
class DefaultDonationService implements DonationService {

    private  final  DonationRepository donationRepository;
    private  final  DonationMapper donationMapper;
    private  final CampaignRepository campaignRepository;
    private  final UserRepository userRepository;
    private  final VnpayService vnpayService;

    @Override
    public List<DonationListVm> findAll() {
        return donationRepository.findAll().stream()
                .map(donationMapper::entityDonationListVm)
                .collect(Collectors.toList());
    }

    @Override
    public Donation create(DonationPostVm donationPostVm,String username) {

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, username));


        Donation donation = new Donation();
        donation.setAmountTotal(donationPostVm.amountTotal());
        donation.setMessage(donationPostVm.message());
        donation.setCampaign(donationPostVm.campaign());

        Donation savedDonation = donationRepository.save(donation);
        savedDonation.setVnpayUrl(vnpayService.doPost(savedDonation));
        return savedDonation;
    }


    @Override
    public Donation pay(int donationID, String username) {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, username));
        Donation donation = donationRepository.findById(donationID)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, donationID));
        JSONObject jsonObject = vnpayService.verifyPay(donation);


        donation.setStatus(DonationStatus.HOLDING);
        donation.setDonationDate(Instant.from(LocalDateTime.now()));
        donation. setTransactionId(jsonObject.getString("vnp_TransactionNo"));
        return donationRepository.save(donation);

    }

}