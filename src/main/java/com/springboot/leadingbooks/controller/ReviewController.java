package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.services.ReviewService;
import com.springboot.leadingbooks.services.dto.request.ReviewCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookReviewResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/v1")
public class ReviewController {
    private final ReviewService reviewService;

    // 리뷰 작성
    @PostMapping("/write/reviews")
    @ResponseBody
    public String writeReviews(@RequestBody ReviewCreateRequestDto reviewCreateRequestDto, Model model) {
        log.info("Required Messages : ");
        BookReviewResponseDto bookDetail = reviewService.WriteReview(reviewCreateRequestDto);
        model.addAttribute("bookDetail", bookDetail);
        return "redirect:/api/v1/books/detail";
    }
    // 도서 세부정보 조회
    @GetMapping("/detail/reviews")
    public String detailReviews(@RequestParam(value = "bId") Long bId, Model model) {
        BookReviewResponseDto bookDetail = reviewService.getBookDetail(bId);
        model.addAttribute("bookDetail", bookDetail);
        return "books/detail";
    }
}
