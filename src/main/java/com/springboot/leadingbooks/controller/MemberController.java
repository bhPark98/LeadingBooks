package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.controller.dto.response.JwtTokenResponseDto;
import com.springboot.leadingbooks.controller.validators.SignUpValidator;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.services.MemberService;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpResponse;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/v1")
public class MemberController {
    private final MemberService memberService;
    private final SignUpValidator signUpValidator;

    // 첫 회원가입 페이지
    @GetMapping("/sign/up")
    public String createForm(Model model) {
        model.addAttribute("memberRequestDto", new MemberRequestDto());
        return "members/createMemberForm";
    }

    // 회원가입
    @PostMapping("/sign/up")
    public String addMember(Model model, @Validated @ModelAttribute("memberRequestDto") MemberRequestDto memberRequestDto, BindingResult bindingResult, Errors errors) {

        signUpValidator.validate(memberRequestDto, errors);

        if(bindingResult.hasErrors())
            return "members/createMemberForm";

        if(errors.hasErrors())
            return "members/createMemberForm";

        memberService.join(memberRequestDto);

        model.addAttribute("memberRequestDto", memberRequestDto);

        return "members/success";

    }

    // 로그인 페이지
    @GetMapping("sign/in")
    public String signInForm(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "members/login";
    }

    // 로그인
    @PostMapping("sign/in")
    public String login(Model model, @Validated @ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, BindingResult bindingResult, HttpServletResponse response) {
        log.info("Received login request: {}", loginRequestDto);

        if(bindingResult.hasErrors())
            return "members/login";

        String token = memberService.login(loginRequestDto);
        log.info("token = {}", token);

        Cookie accessTokenCookie = new Cookie("access_token", token);
        accessTokenCookie.setMaxAge(60 * 60 * 24);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        return "redirect:/api/v1/all/books?page=0&size=10";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("access_token", null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/api/v1/sign/in";
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

    // 이메일 전송
    @PostMapping("emails/verification-requests")
    public ResponseEntity<?> sendMessage(@RequestParam("email") @Valid String email) {
        memberService.sendCodeToEmail(email);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    // 인증번호 검증
    @GetMapping("emails/verifications")
    public ResponseEntity<?> verificationEmail(@RequestParam("email") @Valid String email,
                                               @RequestParam("code") String authCode) {
        memberService.verifiedCode(email, authCode);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 회원 정보 가져오기
    @GetMapping("/userInfo")
    @ResponseBody
    public Long getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails 객체를 사용해서 현재 인증된 사용자 정보를 가져옴
        String username = userDetails.getUsername();
        // UserService를 통해 사용자 정보를 가져옴
        Member member = memberService.getMemberByUsername(username);

        log.info("Retrieved member = {}", member);
        return member.getId();
    }
}
