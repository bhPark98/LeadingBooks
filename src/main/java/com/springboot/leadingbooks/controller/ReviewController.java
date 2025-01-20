package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.services.BookService;
import com.springboot.leadingbooks.services.ReviewService;
import com.springboot.leadingbooks.services.dto.request.ReviewCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ReviewController {
    private final ReviewService reviewService;
    private final BookService bookService;

    // 리뷰 작성
    @PostMapping("/write/reviews")
    @ResponseBody
    public ResponseEntity<?> writeReviews(@RequestBody ReviewCreateRequestDto reviewCreateRequestDto) {
        log.info("Required Messages : ");
        reviewService.WriteReview(reviewCreateRequestDto);

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", "/detail/reviews");
        response.put("bId", reviewCreateRequestDto.getBId());

        return ResponseEntity.ok(response);
    }

    // 리뷰 삭제
    @DeleteMapping("/delete/review")
    public ResponseEntity<?> deleteReview(@RequestParam("rId") Long rId) {
        log.info("Required Messages : ", rId);
        reviewService.removeReview(rId);

        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", "/detail/reviews");

        return ResponseEntity.ok(response);
    }


    // 도서 세부정보 조회
    @GetMapping("/detail/reviews")
    public String detailReviews(@RequestParam(value = "bId") Long bId, Model model) {
        log.info("Required Messages : ");
        BookReviewResponseDto bookDetail = reviewService.getBookDetail(bId);
        Long bookCount = bookService.getTotalBooks(bId);

        model.addAttribute("bookDetail", bookDetail);
        model.addAttribute("bookCount", bookCount);

        return "books/detail";
    }
}
