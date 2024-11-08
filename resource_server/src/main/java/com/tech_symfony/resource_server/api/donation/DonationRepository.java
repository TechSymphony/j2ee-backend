package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.system.pagination.AggregatePaginationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer>, JpaSpecificationExecutor<Donation>, AggregatePaginationRepository<Donation> {

}
