package com.tech_symfony.resource_server.api.donation;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DonationStatis {

  private String period;
  private BigDecimal amountTotal;

  public DonationStatis(String period, BigDecimal amountTotal) {
      this.period = period;
      this.amountTotal = amountTotal;
  }

}