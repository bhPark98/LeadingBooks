package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.controller.dto.request.SignInRequestDto;
import com.springboot.leadingbooks.services.MemberService;
import com.springboot.leadingbooks.services.dto.request.JwtTokenRequestDto;
import com.springboot.leadingbooks.services.dto.response.myPageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/v1")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("sign/in")
    public JwtTokenRequestDto signIn(@RequestBody SignInRequestDto signInRequestDto) {
        String username = signInRequestDto.getUsername();
        String password = signInRequestDto.getPassword();
        JwtTokenRequestDto jwtTokenRequestDto = memberService.signin(username, password);
        log.info("request username = {}, password = {}", username, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtTokenRequestDto.getAccessToken(),
                jwtTokenRequestDto.getRefreshToken());

        return jwtTokenRequestDto;
    }

    // 내정보 확인
    @GetMapping("/myPage/{mId}")
    public ResponseEntity<?> getMyPage(@PathVariable Long mId) {
        return ResponseEntity.ok(memberService.getBorrowedBooks(mId));
    }
    // 닉네임 변경
    @PutMapping("/changeNickname/{mId}")
    public void changeNickname(@PathVariable Long mId, @RequestBody String mNickname) {
        memberService.changeNickname(mId, mNickname);
    }


}
