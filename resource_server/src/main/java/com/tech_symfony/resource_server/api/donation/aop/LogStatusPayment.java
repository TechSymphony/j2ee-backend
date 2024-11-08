package com.tech_symfony.resource_server.api.donation.aop;

import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
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

    @Before("execution(* com.tech_symfony.resource_server.api.donation.DonationService.verify(..)) && args(donation)")
    public void logBefore(JoinPoint joinPoint, Donation donation) {
        if (donation != null) {
            log.info("Donation {} is verifying at {}", donation.getId(), LocalDateTime.now());
        }
    }

    @AfterReturning("execution(* com.tech_symfony.resource_server.api.donation.DonationService.verify(..)) && args(donation)")
    public void logAfterReturning(Donation donation) {
        if (donation != null && donation.getStatus() == DonationStatus.COMPLETED){
            log.info("Donation {} verified successfully at {}", donation.getId(), LocalDateTime.now());
        }
    }

    @AfterThrowing(pointcut = "execution(* com.tech_symfony.resource_server.api.donation.DonationService.verify(..)) && args(donation)", throwing = "ex")
    public void logAfterThrowing(Donation donation, Exception ex) {
        if (donation != null) {
            log.debug("Sending verify for donation {} failed due to {}", donation.getId(), ex.getMessage());

            // Log if status changed to HOLDING
            if (donation.getStatus() == DonationStatus.HOLDING) {
                log.info("Donation {} is now in HOLDING status", donation.getId());
            }
        }
    }
}
