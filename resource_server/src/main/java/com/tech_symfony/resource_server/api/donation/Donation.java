package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.system.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donor_id", nullable = false)
    private User donor;

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

    @OneToMany(mappedBy = "donation")
    private Set<RecurringDonation> recurringDonations = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "frequency")
    private DonationsFrequencyEnum frequency = DonationsFrequencyEnum.ONCE;

}