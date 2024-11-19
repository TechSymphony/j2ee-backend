package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.system.pagination.AggregatePaginationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Integer>, JpaSpecificationExecutor<Donation>, AggregatePaginationRepository<Donation> {
    // Query 1: Lấy top 10 donations theo amountTotal và donationDate
    @Query("SELECT d FROM Donation d WHERE d.campaign.id = :campaignId AND d.donor is NOT NULL " +
            "AND d.isAnonymous = FALSE ORDER BY d.amountTotal DESC, d.donationDate DESC")
    List<Donation> getTopDonationsByCampaignId(Integer campaignId);

    // Query 2: Lấy tất cả donations, sắp xếp theo donationDate
    List<Donation> getDonationsByCampaignId(Integer campaignId);
}
