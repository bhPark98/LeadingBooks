package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.admin.service.memberservice.MemberManagementService;
import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.entity.Stopped;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl {
    private final BookRepository bookRepository;
    private final CheckOutRepository checkOutRepository;
    private final MemberRepository memberRepository;
    private final MemberManagementService memberManagementService;

    // 도서 대여 로직
    @Transactional
    public void CheckOutBooks(Long bId, Long mId) {
        Book book = bookRepository.findBookById(bId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_BOOK)
        );

        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        if(book.getBCount() >= 1) {
            book.decreaseBookCount();
            CheckOut checkOut = CheckOut.builder()
                    .book(book)
                    .member(member)
                    .build();
            checkOutRepository.save(checkOut);
        }
        else
            throw new CustomException(ErrorCode.NOT_COUNT_BOOK);
    }
    // 대출 기한 로직
    @Scheduled(cron = "0 0 0 * * ?")
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

    // 도서 대여 기간 연장
    public void extendDates(Long mId, List<Long> bIds) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        List<CheckOut> checkOutList = member.getCheckOutList();

        for(Long bId : bIds) {
            Optional<CheckOut> checkOutOptional = checkOutList.stream()
                    .filter(checkOut -> checkOut.getBook().getId().equals(bId))
                    .findFirst();

            if(checkOutOptional.isPresent()) {
                CheckOut checkOut = checkOutOptional.get();
                checkOut.setcDate(checkOut.getCDate() + 7);
                checkOutRepository.save(checkOut);
            } else {
                throw new CustomException(ErrorCode.NOT_FOUND_BOOK);
            }
        }
    }

}
