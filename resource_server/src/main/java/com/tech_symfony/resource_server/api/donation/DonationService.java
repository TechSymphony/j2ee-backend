package com.tech_symfony.resource_server.api.donation;


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
import com.tech_symfony.resource_server.system.payment.vnpay.TransactionException;
import com.tech_symfony.resource_server.system.payment.vnpay.VnpayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;

public interface DonationService {
    Page<DonationListVm> findAll(Map<String, String> params);

    Donation create(DonationPostVm donationPostVm);

    Donation verify(int donationId);


    boolean sendEventVerify(int donationId);

    boolean updateDonationHolding(int donationId);
}

@Service
@RequiredArgsConstructor
@Slf4j
class DefaultDonationService implements DonationService {

    private final VnpayConfig vnpayConfig;
    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final CampaignRepository campaignRepository;
    private final UserRepository userRepository;
    private final PaymentService<Donation, JSONObject> paymentService;
    private final AuthService authService;
    private final SpecificationBuilderPagination<Donation> specificationBuilder;
    private final PaginationCommand<Donation, DonationListVm> paginationCommand;


    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.payment.name}")
    private String paymentExchange;

    @Value("${rabbitmq.binding.payment.name}")
    private String paymentRoutingKey;

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


        timer.schedule(new HandleUnusedBillDonationTask(this, savedDonation.getId()), vnpayConfig.exprationTime);

        log.info("Created bill {} at {}", savedDonation.getId(), LocalDateTime.now());

        return savedDonation;
    }


    @Override
    @RabbitListener(queues = "payment_queue")
    public Donation verify(int donationId) {
        log.info("Donation {} is verifying at {}", donationId, LocalDateTime.now());

        Donation donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, donationId));
        try {
            JSONObject jsonObject = paymentService.verifyPay(donation);
            if (donation.getStatus() == DonationStatus.IN_PROGRESS || donation.getStatus() == DonationStatus.HOLDING) {
                donation.setStatus(DonationStatus.COMPLETED);
                donation.setDonationDate(Instant.now());
                donation.setTransactionId(jsonObject.getString("vnp_TransactionNo"));
                donationRepository.save(donation);
            }
            log.info("Donation verified successfully at {}", donationId, LocalDateTime.now());

        }catch (TransactionException e ){
            log.debug("Sending verify for donation {} failed due to {}", donationId, e.getMessages());
            throw e;
        }


        return donation;

    }

    @Override
    public boolean sendEventVerify(int donationId) {

        rabbitTemplate.convertAndSend(paymentExchange,
                paymentRoutingKey,
                donationId
        );

        log.info("Event for verification of donation {} was fired at {}", donationId, LocalDateTime.now());

        return true;

    }

    @Override
    public boolean updateDonationHolding(int donationId) {
        Optional<Donation> donationEntity = donationRepository.findById(donationId);
        if (donationEntity.isPresent() && donationEntity.get().getStatus() == DonationStatus.IN_PROGRESS) {
            Donation donation = donationEntity.get();
            donation.setStatus(DonationStatus.HOLDING);
            donationRepository.save(donation);
            log.info("Donation {} is now in HOLDING status", donationId);
        }
        return true;
    }

}