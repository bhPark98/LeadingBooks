package com.springboot.leadingbooks.global.response.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleBindException(MethodArgumentNotValidException e) {
        log.error("[Error] 에러 발생 ! errorCode : {}, errorMessage : {}",e.getMessage());
        List<String> errorList = e.getFieldErrors().stream().map(
            b -> b.getField() + " : " +b.getDefaultMessage()
        ).toList();
        return ResponseEntity.status(ErrorCode.VALIDATION_FAIL.getHttpStatus())
                .body(ErrorApiResponse.of(e, errorList));
    }

    @ExceptionHandler(value = CustomException.class)
    protected ResponseEntity<?> customExceptionHandler(CustomException e) {
        log.error("Error occurred in controller advice! errorCode: {}, errorMessage: {}", e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus()).body(ErrorApiResponse.of(e));
    }
}
