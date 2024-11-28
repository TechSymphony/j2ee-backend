package com.tech_symfony.resource_server.api.donation;


import com.tech_symfony.resource_server.api.campaign.CampaignService;
import com.tech_symfony.resource_server.api.categories.CampaignNotAbleToDonateException;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationExportVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyEventVm;
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
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.stream.Collectors;

public interface DonationService {
    DonationPage<DonationListVm> findAll(Map<String, String> params);

    List<DonationListVm> getTopDonationsByCampaignId(int campaignId);

    List<DonationListVm> getDonationsByCampaignId(int campaignId);

    Donation create(DonationPostVm donationPostVm) throws ValidationException;

    Donation findById(Integer donationId);

    DonationDetailVm findByIdWithMapper(Integer donationId);

    @Transactional
    void verify(DonationVerifyEventVm donationVerifyEventVm);

    boolean sendEventVerify(DonationVerifyEventVm donationVerifyEventVm);

    @Transactional
    DonationDetailVm updateStatus(Donation donation, DonationStatus donationStatus);

    @Transactional
    DonationDetailVm updateSuccessDonationAndAmountTotalCampaign(Donation donation, Integer idCampaign);

    void sendEventVerifyClient(Integer id);
    FileSystemResource export(DonationExportVm donationExportVm) throws IOException;
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
    private final CacheManager cacheManager;

    private final RabbitTemplate rabbitTemplate;
    @Value("${rabbitmq.exchange.payment.name}")
    private String paymentExchange;

    @Value("${rabbitmq.binding.payment.name}")
    private String paymentRoutingKey;
    private final ExportPdfService<Donation> exportPdfService;

    private String keyCacheEvent = "event-donation";

    @Override
    public DonationPage<DonationListVm> findAll(Map<String, String> params) {
        BigDecimal amountTotal = donationRepository.sum(specificationBuilder.buildSpecificationFromParams(params), Donation.class, BigDecimal.class, "amountTotal");

        return new DonationPage<>(paginationCommand.execute(params, donationRepository, donationMapper, specificationBuilder), amountTotal);

    }

    @Override
    public List<DonationListVm> getTopDonationsByCampaignId(int campaignId) {
        List<Donation> topDonations = donationRepository.getTopDonationsByCampaignId(campaignId);
        return topDonations.stream()
                .map(donationMapper::entityDonationListVm)
                .collect(Collectors.toList());
    }

    @Override
    public List<DonationListVm> getDonationsByCampaignId(int campaignId) {
        List<Donation> donations = donationRepository.getClientDonationsByCampaignId(campaignId);
        return donations.stream()
                .sorted(Comparator.comparing(Donation::getDonationDate))
                .limit(10)
                .map(donationMapper::entityDonationListVm)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Donation create(DonationPostVm donationPostVm) throws ValidationException {
        if (!campaignService.isAbleToDonate(donationPostVm.campaign().getId())) {
            throw new CampaignNotAbleToDonateException();
        }

        Donation donation = new Donation();

        donation.setAmountTotal(donationPostVm.amountTotal());
        donation.setAmountBase(donationPostVm.amountTotal());

        donation.setMessage(donationPostVm.message());
        donation.setCampaign(donationPostVm.campaign());
        donation.setAnonymous(donationPostVm.isAnonymous());

        // public user are able to make create payment
        authService.getCurrentUserAuthenticatedWithoutHandlingException().ifPresent(donation::setDonor);

        Donation savedDonation = donationRepository.save(donation);

        // need bill id to create payment url, update it to database
        savedDonation.setPaymentUrl(paymentService.createBill(donation));
        donationRepository.save(savedDonation);

        // wait until for time expiration to verify payment
        Timer timer = new Timer();
        timer.schedule(new HandleUnusedBillDonationTask(this, new DonationVerifyEventVm(savedDonation.getId(), donationPostVm.campaign().getId())), vnpayConfig.exprationTime);

        return savedDonation;
    }

    @Override
    public Donation findById(Integer donationId) {
        if (donationId != null) {
            return donationRepository.findById(donationId).orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, donationId));
        }
        return null;
    }

    @Override
    public DonationDetailVm findByIdWithMapper(Integer donationId) {
        return donationRepository.findById(donationId)
                .map(donationMapper::entityToDonationDetailVm)
                .orElseThrow(() -> new NotFoundException(MessageCode.RESOURCE_NOT_FOUND, donationId));
    }


    @Override
    @RabbitListener(queues = "payment_queue_dlx")
    @Transactional
    public void verify(DonationVerifyEventVm donationVerifyEventVm) {
        Donation donation = donationRepository.findByIdForUpdateStatus(donationVerifyEventVm.donationId()).orElse(null);
        if (donation == null) return;

        try {

            if (donation.getStatus() == DonationStatus.IN_PROGRESS) {
                JSONObject jsonObject = paymentService.verifyPay(donation);

                donation.setTransactionId(jsonObject.getString("vnp_TransactionNo"));
                donation.setStatus(DonationStatus.COMPLETED);
                donation.setDonationDate(Instant.now());

                this.updateSuccessDonationAndAmountTotalCampaign(donation, donationVerifyEventVm.campaignId());
            }

        } catch (TransactionException e) {

            Instant maxTimePayment = donation.getDonationDate().plus(Duration.ofHours(24));

            // max time 24h
            // remove event so that prevent infinite loop
            if (maxTimePayment.compareTo(Instant.now()) <= 0) {
                if (donation.getStatus() == DonationStatus.IN_PROGRESS) {
                    this.updateStatus(donation, DonationStatus.HOLDING);

                    log.info("Donation {} is now in HOLDING status", donation.getId());
                }

            } else throw e;
        }

        Cache cache = getEventDonationCache();
        if (cache != null) {
            cache.evict(keyCacheEvent + "-" + donationVerifyEventVm.donationId());
        }


    }

    @Override
    public boolean sendEventVerify(DonationVerifyEventVm donationVerifyEventVm) {

        rabbitTemplate.convertAndSend(paymentExchange, paymentRoutingKey, donationVerifyEventVm);

        log.info("Event for verification of donation {} was fired at {}", donationVerifyEventVm.donationId(), LocalDateTime.now());

        return true;

    }

    @Override
    @Transactional
    public DonationDetailVm updateStatus(Donation donation, DonationStatus donationStatus) {
        donation.setStatus(donationStatus);
        donationRepository.save(donation);

        return donationMapper.entityToDonationDetailVm(donationRepository.save(donation));
    }

    @Transactional
    public DonationDetailVm updateSuccessDonationAndAmountTotalCampaign(Donation donation, Integer idCampaign) {
        campaignService.updateTotalByDonation(idCampaign, donation.getAmountTotal());

        return this.updateStatus(donation, DonationStatus.COMPLETED);
    }

    private Cache getEventDonationCache() {

        return cacheManager.getCache(keyCacheEvent);
    }

    /**
     * Send event to verify donation only once time
     *
     * @param id
     */
    @Override
    public void sendEventVerifyClient(Integer id) {
        // prevent send event verify multiple times

        Cache cache = getEventDonationCache();
        if (cache != null) {
            Cache.ValueWrapper cachedValue = cache.get(keyCacheEvent + "-" + id);
            if (cachedValue != null) {
                return;
            } else cache.put(keyCacheEvent + "-" + id, true);
        }

        Donation donation = this.findById(id);
        if (donation.getStatus() == DonationStatus.IN_PROGRESS) {
            this.sendEventVerify(new DonationVerifyEventVm(donation.getId(), donation.getCampaign().getId()));
        }
    }


    @Override
    public FileSystemResource export(DonationExportVm donationExportVm) throws IOException {
        List<Donation> donations = donationRepository.findAll();
        if (donationExportVm.campaign() != 0) {
            donations = donations.stream()
                    .filter(donation -> donation.getCampaign().getId() == donationExportVm.campaign())
                    .collect(Collectors.toList());
        }
        donations = donations.stream()
                .filter(donation ->
                        donation.getDonationDate().equals(donationExportVm.from())
                                || donation.getDonationDate().equals(donationExportVm.to())
                                || (donation.getDonationDate().isAfter(
                                        donationExportVm.from().atStartOfDay().toInstant(ZoneOffset.of("+7")))
                                && donation.getDonationDate().isBefore(
                                        donationExportVm.to().atStartOfDay().toInstant(ZoneOffset.of("+7"))
                                ))
                ).collect(Collectors.toList());
        if (donationExportVm.studentOnly() == true) {
            donations = donations.stream()
                    .filter(donation -> donation.getDonor().getIsStudent())
                    .collect(Collectors.toList());
        }
        return new FileSystemResource(exportPdfService.from(donations, "donations"));
    }
}