package com.springboot.leadingbooks.services;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface CheckOutService {
    // 도서 대여 로직
    @Transactional
    void CheckOutBooks(Long bId, Long mId);

    // 대출 기한 로직
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    void updateCheckOutDates();

    // 도서 대여 기간 연장
    void extendDates(Long mId, List<Long> bIds);
}
