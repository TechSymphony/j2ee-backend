package com.tech_symfony.resource_server.api.donation.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@RequiredArgsConstructor
public class DonationStatisticVm {

  private String period;
  private BigDecimal amountTotal;

  public DonationStatisticVm(String period, BigDecimal amountTotal) {
    this.period = period;
    this.amountTotal = amountTotal;
  }
}