package com.tech_symfony.resource_server.api.campaign.beneficiary;

import com.tech_symfony.resource_server.api.campaign.Campaign;
import com.tech_symfony.resource_server.system.model.BaseEntity;
import com.tech_symfony.resource_server.system.model.NamedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Entity
@Table(name = "beneficiaries")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Beneficiary extends BaseEntity {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

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