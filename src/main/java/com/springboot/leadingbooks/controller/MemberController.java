package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.services.MemberService;
import com.springboot.leadingbooks.services.dto.request.LoginRequestDto;
import jakarta.validation.Valid;
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

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        String token = memberService.login(loginRequestDto);
        return ResponseEntity.ok().body(token);
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
