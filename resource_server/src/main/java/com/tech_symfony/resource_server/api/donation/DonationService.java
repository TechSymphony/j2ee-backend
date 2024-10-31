package com.tech_symfony.resource_server.api.donation;


import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.beneficiary.viewmodel.BeneficiaryListVm;
import com.tech_symfony.resource_server.api.campaign.CampaignRepository;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.api.user.UserRepository;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import com.tech_symfony.resource_server.system.payment.vnpay.PaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

public interface DonationService {
    Page<DonationListVm> findAll(Map<String, String> params);

    Donation create(DonationPostVm donationPostVm);

    Donation pay(int donationID);


}

@Service
@RequiredArgsConstructor
class DefaultDonationService implements DonationService {

    //15minute
    private Integer payExpiration = 30 * 60 * 1000; // minute * second * millisecond

    private  final  DonationRepository donationRepository;
    private  final  DonationMapper donationMapper;
    private  final CampaignRepository campaignRepository;
    private  final UserRepository userRepository;
    private  final PaymentService<Donation, JSONObject> paymentService;
    private final AuthService authService;
    private final SpecificationBuilderPagination<Donation> specificationBuilder;
    private final PaginationCommand<Donation, DonationListVm> paginationCommand;

    @Override
    public Page<DonationListVm> findAll(Map<String, String> params) {

        return paginationCommand.execute(params, donationRepository, donationMapper, specificationBuilder);

    }

    @Override
    public Donation create(DonationPostVm donationPostVm) {

        Donation donation = new Donation();

        donation.setAmountTotal(donationPostVm.amountTotal());
        donation.setAmountBase(donationPostVm.amountTotal());

        donation.setMessage(donationPostVm.message());
        donation.setCampaign(donationPostVm.campaign());

        // public user are able to make create payment
        User user = authService.getCurrentUserAuthenticatedWithoutHandlingException().orElse(null);
        if (user != null)
            donation.setDonor(user);

        Donation savedDonation = donationRepository.save(donation);
        savedDonation.setVnpayUrl(paymentService.createBill(savedDonation));
        Timer timer = new Timer();


        timer.schedule(new HandleUnusedBillDonationTask(donationRepository, savedDonation.getId()), payExpiration);
        return savedDonation;
    }


    @Override
    public Donation pay(int donationID) {

        Donation donation = donationRepository.findById(donationID)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, donationID));
            JSONObject jsonObject = paymentService.verifyPay(donation);
        if(donation.getStatus() == DonationStatus.IN_PROGRESS || donation.getStatus() == DonationStatus.HOLDING){
            donation.setStatus(DonationStatus.COMPLETED);
            donation.setDonationDate(Instant.now());
            donation. setTransactionId(jsonObject.getString("vnp_TransactionNo"));
            donation = donationRepository.save(donation);
        }

        return donation;

    }

}