package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationStatisticVm;
import com.tech_symfony.resource_server.system.pagination.AggregatePaginationRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer>, JpaSpecificationExecutor<Donation>, AggregatePaginationRepository<Donation> {
    // Query 1: Lấy top 10 donations theo amountTotal và donationDate
    @Query("SELECT d FROM Donation d WHERE d.campaign.id = :campaignId AND d.donor is NOT NULL " +
            "AND d.isAnonymous = FALSE AND d.status = com.tech_symfony.resource_server.api.donation.constant.DonationStatus.COMPLETED " +
            "ORDER BY d.amountTotal, d.donationDate DESC")
    List<Donation> getTopDonationsByCampaignId(Integer campaignId);

    // Query 2: Lấy tất cả donations, sắp xếp theo donationDate
//    @Query("SELECT new com.tech_symfony.resource_server.api.donation.viewmodel.DonationTopListVm(d.donor.fullName, d.amountTotal, d.donationDate) " +
//            "FROM Donation d " +
//            "WHERE d.campaign.id = :campaignId AND d.donor IS NOT NULL " +
//            "AND d.isAnonymous = FALSE " +
//            "GROUP BY d.donor.id, d.donor.fullName, d.amountTotal " +
//            "ORDER BY MAX(d.donationDate) DESC")
    @Query("SELECT d FROM Donation d WHERE d.campaign.id = :campaignId AND d.donor is NOT NULL " +
            "AND d.isAnonymous = FALSE AND d.status = com.tech_symfony.resource_server.api.donation.constant.DonationStatus.COMPLETED " +
            "ORDER BY d.donationDate, d.amountTotal DESC")
    List<Donation> getClientDonationsByCampaignId(Integer campaignId);

    @Query("SELECT d FROM Donation d WHERE d.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Donation> findByIdForUpdateStatus(Integer id);

    @Query("SELECT new com.tech_symfony.resource_server.api.donation.viewmodel.DonationStatisticVm(subquery.period, SUM(subquery.amountTotal) )" +
            "FROM (SELECT " +
            "        (CASE " +
            "          WHEN :groupBy = 'MONTH' THEN TO_CHAR(d.donationDate, 'MM-YYYY') " +
            "          WHEN :groupBy = 'YEAR' THEN TO_CHAR(d.donationDate, 'YYYY') " +
            "          ELSE TO_CHAR(d.donationDate, 'DD-MM-YYYY') " +
            "        END) AS period, " +
            "        d.amountTotal as amountTotal " +
            "      FROM Donation d " +
            "      WHERE d.donationDate BETWEEN :fromDate AND :toDate " +
            "      AND d.status= com.tech_symfony.resource_server.api.donation.constant.DonationStatus.COMPLETED " +
            "      AND (:campaignId IS NULL OR d.campaign.id = :campaignId)) subquery " +
            "GROUP BY subquery.period ORDER BY period ")

    List<DonationStatisticVm> getDonationsByPeriodWithFilters(
            @Param("fromDate") Instant fromDate,
            @Param("toDate") Instant toDate,
            @Param("campaignId") Long campaignId,
            @Param("groupBy") String groupBy);

}
