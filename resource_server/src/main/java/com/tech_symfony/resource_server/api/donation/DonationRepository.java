package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.system.pagination.AggregatePaginationRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer>, JpaSpecificationExecutor<Donation>, AggregatePaginationRepository<Donation> {
    // Query 1: Lấy top 10 donations theo amountTotal và donationDate
    @Query("SELECT d FROM Donation d WHERE d.campaign.id = :campaignId AND d.donor is NOT NULL " +
            "AND d.isAnonymous = FALSE ORDER BY d.amountTotal DESC, d.donationDate DESC")
    List<Donation> getTopDonationsByCampaignId(Integer campaignId);

    // Query 2: Lấy tất cả donations, sắp xếp theo donationDate
//    @Query("SELECT new com.tech_symfony.resource_server.api.donation.viewmodel.DonationTopListVm(d.donor.fullName, d.amountTotal, d.donationDate) " +
//            "FROM Donation d " +
//            "WHERE d.campaign.id = :campaignId AND d.donor IS NOT NULL " +
//            "AND d.isAnonymous = FALSE " +
//            "GROUP BY d.donor.id, d.donor.fullName, d.amountTotal " +
//            "ORDER BY MAX(d.donationDate) DESC")
    List<Donation> getClientDonationsByCampaignId(Integer campaignId);

    @Query("SELECT d FROM Donation d WHERE d.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Donation> findByIdForUpdateStatus(Integer id);

}
