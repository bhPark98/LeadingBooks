package com.springboot.leadingbooks.util;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
    // 액세스 쿠키 생성
    public ResponseCookie createAccessCookie(String token) {

        return ResponseCookie.from("access_token", token)
                .path("/")
                .httpOnly(true)
                .build();
    }
    // 리프레시 쿠키 생성
    public ResponseCookie createRefreshCookie(String token) {

        return ResponseCookie.from("refresh_token", token)
                .path("/")
                .httpOnly(true)
                .build();
    }
    // 액세스 쿠키 삭제
    public ResponseCookie deleteAccessCookie() {

        return ResponseCookie.from("access_token")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }
    // 리프레시 쿠키 삭제
    public ResponseCookie deleteRefreshCookie() {

        return ResponseCookie.from("refresh_token")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();
    }

}
