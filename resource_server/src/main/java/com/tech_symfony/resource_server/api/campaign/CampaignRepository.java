package com.tech_symfony.resource_server.api.campaign;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer>, JpaSpecificationExecutor<Campaign> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("UPDATE Campaign c SET c.currentAmount = c.currentAmount + :amountTotal WHERE c.id = :id")
    @Modifying
    void updateCampaignAmount(@Param("id") Integer id, @Param("amountTotal") BigDecimal amountTotal);
}
