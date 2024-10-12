package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.api.user.User;
import com.tech_symfony.resource_server.system.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "beneficiaries")
public class Beneficiary extends BaseEntity {
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "situation_detail", nullable = false, length = Integer.MAX_VALUE)
    private String situationDetail;

    @NotNull
    @Column(name = "support_received", nullable = false, precision = 10, scale = 2)
    private BigDecimal supportReceived;

    @ColumnDefault("false")
    @Column(name = "verification_status")
    private Boolean verificationStatus;

}