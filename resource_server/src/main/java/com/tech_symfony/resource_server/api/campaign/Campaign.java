package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.categories.Category;
import com.tech_symfony.resource_server.system.model.NamedEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.mapping.List;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "campaigns")
@Setter
@Getter
public class Campaign extends NamedEntity {

	@Column(name = "code", unique = true)
	private String code= "";

	@Column(name = "description", length = Integer.MAX_VALUE)
	private String description = "";

	@DecimalMin("0.00")
	@Column(name = "target_amount", precision = 10, scale = 2)
	private BigDecimal targetAmount = BigDecimal.ZERO;

	@DecimalMin("0.00")
	@Column(name = "current_amount", precision = 10, scale = 2, columnDefinition = "DECIMAL(10,2) DEFAULT 0")
	private BigDecimal currentAmount = BigDecimal.ZERO;

	@Column(name = "start_date")
	private LocalDate startDate = LocalDate.now();

	@Column(name = "end_date")
	private LocalDate endDate = LocalDate.now();

//	@Column(name = "is_approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
//	private boolean isApproved = false;
	@Enumerated(EnumType.ORDINAL)
	@NotNull
	@Column(name = "status")
	private CampaignsStatusEnum status = CampaignsStatusEnum.WAITING;


	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;

	@ColumnDefault("CURRENT_TIMESTAMP")
	@CreationTimestamp
	@Column(name = "create_at")
	private Instant createAt;

	@ColumnDefault("0")
	@Column(name = "number_of_donations")
	private Integer numberOfDonations;

	public boolean isReachTarget() {
		return currentAmount.compareTo(targetAmount) >= 0;
	}

	public boolean isExpired() {
		return endDate.isBefore(LocalDate.now());
	}

	public boolean isCampaignStarted() {
		return startDate.isAfter(LocalDate.now()) || startDate.isEqual(LocalDate.now());
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id")
	private Category category;

}
