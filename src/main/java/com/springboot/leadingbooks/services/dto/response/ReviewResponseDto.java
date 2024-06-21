package com.springboot.leadingbooks.services.dto.response;

import com.springboot.leadingbooks.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ReviewResponseDto {
    private String mNickname;
    private int rRating;
    private String rContent;

    public static ReviewResponseDto of(Review review) {
        return ReviewResponseDto.builder()
                .mNickname(review.getMember().getLoginData().getMNickname())
                .rRating(review.getRRating())
                .rContent(review.getRContent())
                .build();
    }
}
