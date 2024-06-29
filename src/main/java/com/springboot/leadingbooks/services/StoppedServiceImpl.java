package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.admin.service.memberservice.MemberManagementService;
import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.domain.repository.StoppedRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoppedServiceImpl {
    private final BookRepository bookRepository;
    private final CheckOutRepository checkOutRepository;
    private final MemberRepository memberRepository;
    private final MemberManagementService memberManagementService;
    private final StoppedRepository stoppedRepository;

    // 대출 기한 로직
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "0/5 * * * * ?")
    @Transactional
    public void updateCheckOutDates() {
        List<CheckOut> checkOutList = checkOutRepository.findAll();
        for(CheckOut checkOut : checkOutList) {
            int currentTime = checkOut.getCDate();
            if(currentTime > 0) {
                checkOut.setcDate(currentTime-1);
            } else {
                Member member = checkOut.getMember();
                memberManagementService.punishMember(member.getId(), "대출 기한 초과");
            }
        }
    }

    // 정지 회원 -> 회원 로직
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void backMember() {

    }
}
