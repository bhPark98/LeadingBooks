package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.dto.request.ReviewCreateRequestDto;
import com.springboot.leadingbooks.dto.response.BookReviewResponseDto;

public interface ReviewService {
    // 도서 조회 로직
    BookReviewResponseDto getBookDetail(Long bId);

    // 도서 리뷰 작성 로직
    BookReviewResponseDto WriteReview(ReviewCreateRequestDto reviewCreateRequestDto);
}
