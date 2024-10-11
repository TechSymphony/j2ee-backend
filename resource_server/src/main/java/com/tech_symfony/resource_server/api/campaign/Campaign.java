package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.api.campaign.beneficiary.Beneficiary;
import com.tech_symfony.resource_server.api.role.Role;
import com.tech_symfony.resource_server.system.model.NamedEntity;
import com.tech_symfony.resource_server.api.donation.Donation;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.List;

import java.math.BigDecimal;
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

	@Column(name = "is_approved", columnDefinition = "BOOLEAN DEFAULT FALSE")
	private boolean isApproved = false;

	@OneToMany(mappedBy = "campaign")
	private Set<Donation> donations = new LinkedHashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "beneficiary_id")
	private Beneficiary beneficiary;

}
