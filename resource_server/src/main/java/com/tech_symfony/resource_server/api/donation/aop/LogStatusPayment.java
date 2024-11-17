package com.tech_symfony.resource_server.api.donation.aop;

import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationDetailVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationVerifyEventVm;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Configuration
public class LogStatusPayment {


    @AfterReturning("execution(* com.tech_symfony.resource_server.api.donation.DonationService.create(..)) && args(donationDetailVm)")
    public void logAfterReturningCreate(DonationDetailVm donationDetailVm) {
        log.info("Successfully created donation {} for campaign {} at {}",
                donationDetailVm.id(), donationDetailVm.campaign().id(), LocalDateTime.now());
    }

    @Before("execution(* com.tech_symfony.resource_server.api.donation.DonationService.verify(..)) && args(donationVerifyEventVm)")
    public void logBefore(JoinPoint joinPoint, DonationVerifyEventVm donationVerifyEventVm) {
        if (donationVerifyEventVm != null) {
            log.info("Donation {} is verifying at {}", donationVerifyEventVm.donationId(), LocalDateTime.now());
        }
    }

    @AfterReturning("execution(* com.tech_symfony.resource_server.api.donation.DonationService.verify(..)) && args(donationVerifyEventVm)")
    public void logAfterReturning(DonationVerifyEventVm donationVerifyEventVm) {
        if (donationVerifyEventVm != null) {
            log.info("Donation {} verified successfully at {}", donationVerifyEventVm.donationId(), LocalDateTime.now());
        }

    }

    @AfterThrowing(pointcut = "execution(* com.tech_symfony.resource_server.api.donation.DonationService.verify(..)) && args(donationVerifyEventVm)", throwing = "ex")
    public void logAfterThrowing(DonationVerifyEventVm donationVerifyEventVm, Exception ex) {
        if (donationVerifyEventVm != null) {
            log.debug("Sending verify for donation {} failed due to {}", donationVerifyEventVm.donationId(), ex.getMessage());

        }
    }
}
