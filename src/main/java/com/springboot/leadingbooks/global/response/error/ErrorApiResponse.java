package com.springboot.leadingbooks.global.response.error;

import com.springboot.leadingbooks.global.response.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorApiResponse<T> extends ApiResponse {

    private ErrorApiResponse(CustomException e) {
        super(LocalDateTime.now().toString(),
                e.getErrorCode().getHttpStatus().toString(),
                e.getErrorCode().getCode(),
                e.getMessage()
                );
    }

    public static ErrorApiResponse<?> of(CustomException e) {
        return new ErrorApiResponse<>(e);
    }
}
