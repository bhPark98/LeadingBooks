package com.springboot.leadingbooks.global.response.error;

import com.springboot.leadingbooks.global.response.ApiResponse;
import com.springboot.leadingbooks.services.dto.request.CustomUserInfoDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDto extends ApiResponse {
    public ErrorResponseDto(CustomException e) {
        super(LocalDateTime.now().toString(),
                e.getErrorCode().getHttpStatus().toString(),
                e.getErrorCode().getCode().toString(),
                e.getMessage());
    }
}
