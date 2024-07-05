package com.springboot.leadingbooks.global.response.error;

import com.springboot.leadingbooks.global.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cglib.core.Local;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorApiResponse<T> extends ApiResponse {

    private T content;

    private ErrorApiResponse(CustomException e) {
        super(LocalDateTime.now().toString(),
                e.getErrorCode().getHttpStatus().toString(),
                e.getErrorCode().getCode(),
                e.getMessage()
                );
    }

    private ErrorApiResponse(MethodArgumentNotValidException e, T content) {
        super(
                LocalDateTime.now().toString(),
                ErrorCode.VALIDATION_FAIL.getHttpStatus().toString(),
                ErrorCode.VALIDATION_FAIL.getCode(),
                ErrorCode.VALIDATION_FAIL.getMessage()
        );
        this.content = content;
    }

    private ErrorApiResponse(ConstraintViolationException e, T content) {
        super(
                LocalDateTime.now().toString(),
                ErrorCode.VALIDATION_FAIL.getHttpStatus().toString(),
                ErrorCode.VALIDATION_FAIL.getCode(),
                ErrorCode.VALIDATION_FAIL.getMessage()
        );
        this.content = content;
    }

    public static ErrorApiResponse<?> of(CustomException e) {
        return new ErrorApiResponse<>(e);
    }

    public static <T> ErrorApiResponse<T> of(MethodArgumentNotValidException e, T content) {
        return new ErrorApiResponse<>(e, content);
    }

    public static <T> ErrorApiResponse<T> of(ConstraintViolationException e, T content) {
        return new ErrorApiResponse<>(e, content);
    }
}
