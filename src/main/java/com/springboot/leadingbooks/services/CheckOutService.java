package com.springboot.leadingbooks.services;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

public interface CheckOutService {
    // 도서 대여 로직
    @Transactional
    void CheckOutBooks(Long bId, Long mId);

    // 도서 대여 기간 연장
    void extendDates(Long mId, Long bId);

    // 도서 반납 로직
    public void returnBooks(Long mId, Long bId);
}
