package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.entity.Review;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.domain.repository.ReviewRepository;
import com.springboot.leadingbooks.services.dto.request.ReviewCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookReviewResponseDto;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;

    // 도서 조회 로직
    @Override
    @Transactional
    public BookReviewResponseDto getBookDetail(Long bId) {
        Book book = bookRepository.findBookById(bId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_BOOK)
        );

        return BookReviewResponseDto.of(book);
    }
    // 도서 리뷰 작성 로직
    @Override
    @Transactional
    public BookReviewResponseDto WriteReview(ReviewCreateRequestDto reviewCreateRequestDto) {
        Book book = bookRepository.findBookById(reviewCreateRequestDto.getBId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_BOOK)
        );
        Member member = memberRepository.findById(reviewCreateRequestDto.getMId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        Review review = Review.builder()
                .rRating(reviewCreateRequestDto.getRRating())
                .rContent(reviewCreateRequestDto.getRContent())
                .member(member)
                .book(book)
                .build();

        reviewRepository.save(review);
        entityManager.flush();

        return BookReviewResponseDto.of(book);
    }
}
