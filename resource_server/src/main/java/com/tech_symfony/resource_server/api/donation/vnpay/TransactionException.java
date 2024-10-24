package com.tech_symfony.resource_server.api.donation.vnpay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionException extends RuntimeException {
    private String error;
    private Integer Status;
    private List<String> messages;
}

