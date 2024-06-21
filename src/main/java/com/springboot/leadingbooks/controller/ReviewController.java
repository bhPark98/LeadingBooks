package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.services.ReviewService;
import com.springboot.leadingbooks.services.dto.request.ReviewCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/write/reviews")
    public BookReviewResponseDto writeReviews(@RequestBody ReviewCreateRequestDto reviewCreateRequestDto) {
        return reviewService.WriteReview(reviewCreateRequestDto);
    }
    // 도서 세부정보 조회
    @GetMapping("/detail/reviews")
    public BookReviewResponseDto detailReviews(@RequestParam(value = "bId") Long bId) {
        return reviewService.getBookDetail(bId);
    }
}
