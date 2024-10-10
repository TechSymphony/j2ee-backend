package com.tech_symfony.resource_server;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.system.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation extends NamedEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @NotNull
    @Column(name = "amount_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountBase;

    @NotNull
    @Column(name = "amount_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountTotal;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "donation_date")
    private Instant donationDate;

/*
 TODO [Reverse Engineering] create field to map the 'frequency' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "frequency", columnDefinition = "donations_frequency_enum not null")
    private Object frequency;
*/
}