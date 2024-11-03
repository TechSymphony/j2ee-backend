package com.tech_symfony.resource_server.api.donation;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class DonationPage<T> {
    List<T> content;
    CustomPageable pageable;

    public DonationPage(Page<T> page, BigDecimal amountTotal) {
        this.content = page.getContent();
        Pageable pageable = page.getPageable();

        this.pageable = new CustomPageable(pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), amountTotal);
    }

    @Getter
    class CustomPageable {

        public int number;
        public int size;
        public long totalElements;

        BigDecimal amountTotal;

        public CustomPageable(int pageNumber, int pageSize, long totalElements, BigDecimal amountTotal) {
            this.number = pageNumber;
            this.size = pageSize;
            this.totalElements = totalElements;
            this.amountTotal = amountTotal;
        }
    }

}