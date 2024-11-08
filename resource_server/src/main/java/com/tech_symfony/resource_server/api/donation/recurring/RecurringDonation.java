package com.tech_symfony.resource_server.api.donation.recurring;

import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.system.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "recurring_donations")
public class RecurringDonation extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "donation_id", nullable = false)
    private Donation donation;

    @NotNull
    @Column(name = "next_donation_date", nullable = false)
    private LocalDate nextDonationDate;

}