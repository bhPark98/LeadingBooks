package com.springboot.leadingbooks.services.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ReviewCreateRequestDto {
    private Long bId;
    private Long mId;
    private int rRating;
    private String rContent;
}
