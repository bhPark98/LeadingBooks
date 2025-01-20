package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.services.dto.request.ReviewCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookReviewResponseDto;

public interface ReviewService {
    // 도서 조회 로직
    BookReviewResponseDto getBookDetail(Long bId);

    // 도서 리뷰 작성 로직
    void WriteReview(ReviewCreateRequestDto reviewCreateRequestDto);

    // 도서 리뷰 삭제 로직
    public void removeReview(Long rId);
}
