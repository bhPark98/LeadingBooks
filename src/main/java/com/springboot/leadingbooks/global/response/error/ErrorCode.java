package com.springboot.leadingbooks.global.response.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    NOT_FOUND_NAME(HttpStatus.NOT_FOUND, "NFN", "해당하는 제목의 책을 찾을 수 없습니다."),
    NOT_FOUND_WRITER(HttpStatus.NOT_FOUND, "NFW", "해당하는 작가의 책을 찾을 수 없습니다"),
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "NFC", "해당하는 카테고리의 책을 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "NFM", "회원정보를 찾을 수 없습니다."),
    NOT_FOUND_BOOK(HttpStatus.NOT_FOUND, "NFB", "해당 책을 찾을 수 없습니다."),
    NOT_FOUND_STOPPED(HttpStatus.NOT_FOUND, "NFS", "정지회원이 존재하지 않습니다."),
    NOT_COUNT_BOOK(HttpStatus.NOT_FOUND, "NCB", "대여 가능한 도서가 존재하지 않습니다."),
    DUPLICATED_NICKNAME(HttpStatus.IM_USED, "DN", "중복된 닉네임입니다. 다른 닉네임을 입력하여주세요."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "NFE", "이메일이 존재하지 않습니다."),
    NOT_MATCH_PASSWORD(HttpStatus.CONFLICT, "NMP", "비밀번호가 일치하지 않습니다."),
    NOT_AUTHORIZED(HttpStatus.UNAUTHORIZED, "NA", "Not Authenticated Request"),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "DE", "존재하는 이메일이 있습니다."),
    NOT_MATCHES_PASSWORD(HttpStatus.CONFLICT, "NMP", "비밀번호가 일치하지 않습니다."),
    UNABLE_TO_SEND_EMAIL(HttpStatus.BAD_GATEWAY, "UTSE", "이메일을 전송할 수 없습니다."),
    NO_SUCH_ALGORITHM(HttpStatus.NOT_FOUND, "NSA", "인증번호 생성 오류"),
    NOT_MATCHES_AUTHCODE(HttpStatus.NOT_FOUND, "NMA", "인증번호가 일치하지 않습니다."),
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "VF", "유효성 검증 실패하였습니다."),
    DUPLICATED_BOOKS(HttpStatus.CONFLICT, "DB", "이미 같은 책을 대여한 기록이 존재합니다."),
    EXTENDED_BOOK(HttpStatus.NOT_EXTENDED, "EB", "이미 한 번 연장한 도서입니다. 연장할 수 없습니다."),
    NOT_BORROWED_BOOK(HttpStatus.NOT_FOUND, "NBB", "대여하지 않은 도서입니다. 도서를 먼저 대여 후 연장을 해주세요."),
    CANNOT_BORROW_BOOKS(HttpStatus.UNAUTHORIZED, "CBB", "연체로 인한 정지입니다. 관리자에게 문의하세요."),
    RETURNING_BOOK(HttpStatus.NOT_EXTENDED, "RB", "연체된 도서를 반납하지 않으면 연장이 불가능합니다."),
    NOT_FOUND_BORROWED_INFO(HttpStatus.NOT_FOUND, "NFBI", "도서 대여 정보가 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    private ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
