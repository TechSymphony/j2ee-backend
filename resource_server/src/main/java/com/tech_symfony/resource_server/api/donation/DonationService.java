package com.tech_symfony.resource_server.api.donation;


import com.tech_symfony.resource_server.api.campaign.CampaignRepository;
import com.tech_symfony.resource_server.api.campaign.CampaignService;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.api.user.UserRepository;
import com.tech_symfony.resource_server.system.export.ExportPdfService;
import com.tech_symfony.resource_server.system.pagination.PaginationCommand;
import com.tech_symfony.resource_server.system.pagination.SpecificationBuilderPagination;
import com.tech_symfony.resource_server.system.payment.vnpay.PaymentService;
import com.tech_symfony.resource_server.system.payment.vnpay.TransactionException;
import com.tech_symfony.resource_server.system.payment.vnpay.VnpayConfig;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;

public interface DonationService {
    DonationPage<DonationListVm> findAll(Map<String, String> params);

    Donation create(DonationPostVm donationPostVm) throws ValidationException;

    Donation verify(int donationId);


    boolean sendEventVerify(int donationId);

    boolean updateDonationHolding(int donationId);

    boolean updateDonationSuccess(Donation donation);

    FileSystemResource export() throws IOException;
}

@Service
@RequiredArgsConstructor
@Slf4j
class DefaultDonationService implements DonationService {

    private final VnpayConfig vnpayConfig;
    private final DonationRepository donationRepository;
    private final DonationMapper donationMapper;
    private final UserRepository userRepository;
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
    public Donation create(DonationPostVm donationPostVm) throws ValidationException {
        if(campaignService.isReachTarget(donationPostVm.campaign().getId())){
            throw new ValidationException("Campaign is reached target");
        }
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
    @Transactional
    public Donation verify(int donationId) {
        log.info("Donation {} is verifying at {}", donationId, LocalDateTime.now());

        Donation donation = donationRepository.findById(donationId)
                .orElse(null);

        if (donation == null) return null;

        try {
            if (donation.getStatus() == DonationStatus.IN_PROGRESS || donation.getStatus() == DonationStatus.HOLDING) {
                JSONObject jsonObject = paymentService.verifyPay(donation);

                donation.setTransactionId(jsonObject.getString("vnp_TransactionNo"));
                donation.setStatus(DonationStatus.COMPLETED);
                donation.setDonationDate(Instant.now());
                updateDonationSuccess(donation);
            }
            log.info("Donation {} verified successfully at {}", donationId, LocalDateTime.now());

        } catch (TransactionException e) {

            log.debug("Sending verify for donation {} failed due to {}", donationId, e.getMessages());

            if (donation.getStatus() == DonationStatus.IN_PROGRESS) {
                donation.setStatus(DonationStatus.HOLDING);
                donationRepository.save(donation);
                log.info("Donation {} is now in HOLDING status", donationId);
            }

            // retry until success
            Timer timer = new Timer();
            timer.schedule(new HandleUnusedBillDonationTask(this, donation.getId()), vnpayConfig.exprationTime);

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

    @Override
    @Transactional
    public boolean updateDonationSuccess(Donation donation) {
        donationRepository.save(donation);
        campaignService.updateTotalByDonation(donation);

        return true;
    }

    public FileSystemResource export() throws IOException {
        return new FileSystemResource(exportPdfService.from(donationRepository.findAll(), "donations"));
    }
}