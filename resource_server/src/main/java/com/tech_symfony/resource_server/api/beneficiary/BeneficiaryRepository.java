package com.tech_symfony.resource_server.api.beneficiary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Integer>, JpaSpecificationExecutor<Beneficiary> {

    public Set<Beneficiary> findAllByUserId(Integer userId);
}
