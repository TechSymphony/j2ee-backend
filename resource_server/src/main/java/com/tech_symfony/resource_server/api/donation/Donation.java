package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.api.donation.constant.DonationStatus;
import com.tech_symfony.resource_server.api.donation.constant.DonationsFrequencyEnum;
import com.tech_symfony.resource_server.api.donation.recurring.RecurringDonation;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.system.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donor_id")
    private User donor;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "campaign_id", nullable = false)
    private Campaign campaign;

    @Column(name = "transaction_id")
    private String transactionId;

    @NotNull
    @Column(name = "amount_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountBase;

    @NotNull
    @Column(name = "amount_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal amountTotal;

    @NotNull
    @Column(name = "message", nullable = false, length = Integer.MAX_VALUE)
    private String message;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "donation_date")
    private Instant donationDate = Instant.now();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Column(name = "create_time")
    private Instant createTime;

    @OneToMany(mappedBy = "donation")
    private Set<RecurringDonation> recurringDonations = new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "frequency")
    private DonationsFrequencyEnum frequency = DonationsFrequencyEnum.ONCE;

    @NotNull(message = "Status must not be null")
    @Enumerated(EnumType.ORDINAL)
    private DonationStatus status = DonationStatus.IN_PROGRESS;

    @Transient
    String vnpayUrl;

}