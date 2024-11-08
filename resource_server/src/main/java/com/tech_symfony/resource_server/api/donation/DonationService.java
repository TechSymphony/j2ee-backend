package com.tech_symfony.resource_server.api.donation;


import com.tech_symfony.resource_server.api.campaign.CampaignService;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import com.tech_symfony.resource_server.commonlibrary.constants.MessageCode;
import com.tech_symfony.resource_server.commonlibrary.exception.NotFoundException;
import com.tech_symfony.resource_server.system.export.ExportPdfService;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import com.tech_symfony.resource_server.system.payment.vnpay.PaymentService;
import com.tech_symfony.resource_server.system.payment.vnpay.TransactionException;
import com.tech_symfony.resource_server.system.payment.vnpay.VnpayConfig;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Timer;

public interface DonationService {
    DonationPage<DonationListVm> findAll(Map<String, String> params);

    Donation create(DonationPostVm donationPostVm) throws ValidationException;

    Donation findById(Integer donationId);

    @Transactional
    void verify(Donation donationId);

    boolean sendEventVerify(Donation donation);

    FileSystemResource export() throws IOException;
}

@Service
@RequiredArgsConstructor
@Slf4j
class DefaultDonationService implements DonationService {

    private final VnpayConfig vnpayConfig;
    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final PaymentService<Donation, JSONObject> paymentService;
    private final AuthService authService;
    private final SpecificationBuilderPagination<Donation> specificationBuilder;
    private final PaginationCommand<Donation, DonationListVm> paginationCommand;
    private final CampaignService campaignService;

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.payment.name}")
    private String paymentExchange;

    @Value("${rabbitmq.binding.payment.name}")
    private String paymentRoutingKey;
    private final ExportPdfService<Donation> exportPdfService;


    @Override
    public DonationPage<DonationListVm> findAll(Map<String, String> params) {
        BigDecimal amountTotal = donationRepository.sum(specificationBuilder.buildSpecificationFromParams(params), Donation.class, BigDecimal.class, "amountTotal");

        return new DonationPage<>(paginationCommand.execute(params, donationRepository, donationMapper, specificationBuilder), amountTotal);

    }

    @Override
    @Transactional
    public Donation create(DonationPostVm donationPostVm) throws ValidationException {
        if (campaignService.isReachTarget(donationPostVm.campaign().getId())) {
            throw new ValidationException("Campaign is reached target");
        }
        Donation donation = new Donation();

        donation.setAmountTotal(donationPostVm.amountTotal());
        donation.setAmountBase(donationPostVm.amountTotal());

        donation.setMessage(donationPostVm.message());
        donation.setCampaign(donationPostVm.campaign());

        // public user are able to make create payment
        authService.getCurrentUserAuthenticatedWithoutHandlingException().ifPresent(donation::setDonor);

        Donation savedDonation = donationRepository.save(donation);
        savedDonation.setVnpayUrl(paymentService.createBill(savedDonation));
        Timer timer = new Timer();


        timer.schedule(new HandleUnusedBillDonationTask(this, savedDonation), vnpayConfig.exprationTime);

        log.info("Created bill {} at {}", savedDonation.getId(), LocalDateTime.now());

        return savedDonation;
    }

    @Override
    public Donation findById(Integer donationId) {
        if (donationId != null) {
            return donationRepository.findById(donationId)
                    .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, donationId));
        }
        return null;
    }


    @Override
    @RabbitListener(queues = "payment_queue")
    @Transactional
    public void verify(Donation donation) {

        if (donation == null) return;

        try {
            if (donation.getStatus() == DonationStatus.IN_PROGRESS || donation.getStatus() == DonationStatus.HOLDING) {
                JSONObject jsonObject = paymentService.verifyPay(donation);

                donation.setTransactionId(jsonObject.getString("vnp_TransactionNo"));
                donation.setStatus(DonationStatus.COMPLETED);
                donation.setDonationDate(Instant.now());

                donationRepository.save(donation);

                campaignService.updateTotalByDonation(donation.getCampaign().getId(), donation.getAmountTotal());
            }

        } catch (TransactionException e) {

            if (donation.getStatus() == DonationStatus.IN_PROGRESS) {
                donation.setStatus(DonationStatus.HOLDING);
                donationRepository.save(donation);
            }

            // retry until success
            Timer timer = new Timer();
            timer.schedule(new HandleUnusedBillDonationTask(this, donation), vnpayConfig.exprationTime);

        }
    }

    @Override
    @Transactional
    public boolean sendEventVerify(Donation donation) {

        rabbitTemplate.convertAndSend(paymentExchange,
                paymentRoutingKey,
                donation
        );

        log.info("Event for verification of donation {} was fired at {}", donation.getId(), LocalDateTime.now());

        return true;

    }

    public FileSystemResource export() throws IOException {
        return new FileSystemResource(exportPdfService.from(donationRepository.findAll(), "donations"));
    }
}