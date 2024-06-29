package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckOutServiceImpl implements CheckOutService {
    private final BookRepository bookRepository;
    private final CheckOutRepository checkOutRepository;
    private final MemberRepository memberRepository;

    // 도서 대여 로직
    @Override
    @Transactional
    public void CheckOutBooks(Long bId, Long mId) {
        Book book = bookRepository.findBookById(bId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_BOOK)
        );

        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        Optional<CheckOut> checkOutOptional = member.getCheckOutList().stream()
                .filter(b -> b.getBook().getId().equals(bId)).findFirst();

        if(checkOutOptional.isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATED_BOOKS);
        }

        if(!member.isMBanned()) {
            if(book.getBCount() >= 1) {
                book.decreaseBookCount();
                bookRepository.save(book);

                CheckOut checkOut = CheckOut.builder()
                        .cDate(7)
                        .book(book)
                        .member(member)
                        .build();
                checkOutRepository.save(checkOut);
            }
            else
                throw new CustomException(ErrorCode.NOT_COUNT_BOOK);
        }
    }

    // 도서 대여 기간 연장
    @Override
    @Transactional
    public void extendDates(Long mId, Long bId) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        List<CheckOut> checkOutList = member.getCheckOutList();

        Optional<CheckOut> checkOutOptional = checkOutList.stream()
                .filter(c -> c.getBook().getId().equals(bId)).findFirst();

        if(checkOutOptional.isPresent()) {
            if(!checkOutOptional.get().isCExtend()) {
                CheckOut checkOut = checkOutOptional.get();
                checkOut.extend();

                checkOutRepository.save(checkOut);
            }
            else {
                throw new CustomException(ErrorCode.EXTENDED_BOOK);
            }

        }else {
            throw new CustomException(ErrorCode.NOT_BORROWED_BOOK);
        }


    }













}
