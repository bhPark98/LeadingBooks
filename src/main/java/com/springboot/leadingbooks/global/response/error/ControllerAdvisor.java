package com.springboot.leadingbooks.global.response.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<?> customExceptionHandler(CustomException e) {
        log.error("Error occurred in controller advice! errorCode: {}, errorMessage: {}", e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorApiResponse.of(e));
    }
}
