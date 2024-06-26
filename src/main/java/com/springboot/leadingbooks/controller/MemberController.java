package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.services.MemberService;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/v1")
@Validated
public class MemberController {
    private final MemberService memberService;

    // 회원가입
    @PostMapping("sign/up")
    public ResponseEntity<?> addMember(@RequestBody @Valid MemberRequestDto dto) {
        Long id = memberService.join(dto);
        return ResponseEntity.status(HttpStatus.OK).body(id);
    }

    // 로그인
    @PostMapping("sign/in")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        log.info("Received login request: {}", loginRequestDto);
        String token = memberService.login(loginRequestDto);
        return ResponseEntity.ok().body(token);
    }
    // 회원탈퇴
    @DeleteMapping("delete/user")
    public ResponseEntity<?> deleteUser(@RequestBody DeleteUserRequestDto dto) {
        log.info("Received deleteInfo request: {}", dto);
        memberService.deleteMember(dto);
        return ResponseEntity.ok(HttpStatus.OK);
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
