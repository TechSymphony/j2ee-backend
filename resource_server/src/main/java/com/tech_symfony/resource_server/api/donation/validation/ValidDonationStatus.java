package com.tech_symfony.resource_server.api.donation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DonationStatusValidator.class)
public @interface ValidDonationStatus {
    String message() default "Chỉ có thể chấp thuận hoặc từ chối hóa đơn quyên góp";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

