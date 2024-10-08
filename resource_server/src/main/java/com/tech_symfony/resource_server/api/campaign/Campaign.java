package com.tech_symfony.resource_server.api.campaign;

import com.tech_symfony.resource_server.system.model.NamedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "campaigns")
@Setter
@Getter
public class Campaign extends NamedEntity {

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

}
