package com.tech_symfony.resource_server.api.beneficiary;

import com.tech_symfony.resource_server.api.donation.DonationPage;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class BeneficiaryPage<T> {
    List<T> content;
    CustomPageable page;

    public BeneficiaryPage(Page<T> page) {
        this.content = page.getContent();
        Pageable pageable = page.getPageable();

        this.page = new CustomPageable(pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Getter
    class CustomPageable {

        public int number;
        public int size;
        public long totalElements;
        public long totalPages;

        public CustomPageable(int pageNumber, int pageSize, long totalElements, long totalPages) {
            this.number = pageNumber;
            this.size = pageSize;
            this.totalElements = totalElements;
            this.totalPages = totalPages;
        }
    }

}