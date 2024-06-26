package com.springboot.leadingbooks.global.response.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND_NAME(HttpStatus.NOT_FOUND, "NFN", "해당하는 제목의 책을 찾을 수 없습니다."),
    NOT_FOUND_WRITER(HttpStatus.NOT_FOUND, "NFW", "해당하는 작가의 책을 찾을 수 없습니다"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "NFC", "해당하는 카테고리의 책을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NFM", "멤버정보를 찾을 수 없습니다."),
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "NFB", "해당 책을 찾을 수 없습니다."),
    NOT_FOUND_STOPPED(HttpStatus.NOT_FOUND, "NFS", "정지회원이 존재하지 않습니다."),
    NOT_COUNT_BOOK(HttpStatus.NOT_FOUND, "NCB", "대여 가능한 도서가 존재하지 않습니다."),
    DUPLICATED_NICKNAME(HttpStatus.IM_USED, "DN", "중복된 닉네임입니다. 다른 닉네임을 입력하여주세요."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "NFE", "이메일이 존재하지 않습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.CONFLICT, "NMP", "비밀번호가 일치하지 않습니다."),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "NA", "Not Authenticated Request"),
    DUPLICATED_EMAIL(HttpStatus.IM_USED, "DE", "존재하는 이메일이 있습니다."),
    NOT_MATCHES_PASSWORD(HttpStatus.CONFLICT, "NMP", "비밀번호가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    private ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
