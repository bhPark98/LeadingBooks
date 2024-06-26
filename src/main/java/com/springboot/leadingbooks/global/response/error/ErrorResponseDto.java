package com.springboot.leadingbooks.global.response.error;

import com.springboot.leadingbooks.global.response.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponseDto extends ApiResponse {
    public ErrorResponseDto(CustomException e) {
        super(LocalDateTime.now().toString(),
                e.getErrorCode().getHttpStatus().toString(),
                e.getErrorCode().getCode(),
                e.getMessage());
    }
}
