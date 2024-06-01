package com.springboot.leadingbooks.global.response.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND_NAME(HttpStatus.NOT_FOUND, "NFN", "해당하는 제목의 책을 찾을 수 없습니다."),
    NOT_FOUND_WRITER(HttpStatus.NOT_FOUND, "NFW", "해당하는 작가의 책을 찾을 수 없습니다"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "NFC", "해당하는 카테고리의 책을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    private ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
