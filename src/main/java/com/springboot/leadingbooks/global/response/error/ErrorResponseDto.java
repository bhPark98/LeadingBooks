package com.springboot.leadingbooks.global.response.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponseDto {
    private HttpStatus status;
    private String code;
    private String message;
}
