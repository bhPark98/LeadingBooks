package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.controller.dto.request.ChangeNicknameRequestDto;
import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.controller.dto.response.JwtTokenResponseDto;
import com.springboot.leadingbooks.controller.validators.SignUpValidator;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.services.MemberService;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.services.dto.request.FindPwdRequestDto;
import com.springboot.leadingbooks.services.dto.response.MyPageResponseDto;
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
import java.util.HashMap;
import java.util.Map;

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
        return "members/register";
    }

    // 회원가입
    @PostMapping("/sign/up")
    public String addMember(Model model, @Validated @ModelAttribute("memberRequestDto") MemberRequestDto memberRequestDto, BindingResult bindingResult, Errors errors) {

        signUpValidator.validate(memberRequestDto, errors);

        if(bindingResult.hasErrors())
            return "members/register";

        if(errors.hasErrors())
            return "members/register";

        memberService.join(memberRequestDto);

        model.addAttribute("memberRequestDto", memberRequestDto);

        return "members/success";

    }

    // 로그인 페이지
    @GetMapping("/sign/in")
    public String signInForm(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "members/login";
    }

    // 로그인
    @PostMapping("/sign/in")
    @ResponseBody
    public ResponseEntity<?> login(Model model, @Validated @ModelAttribute("loginRequestDto") LoginRequestDto loginRequestDto, BindingResult result, HttpServletResponse response) {
        log.info("Received login request: {}", loginRequestDto);

        if(result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errorMap.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.badRequest().body(errorMap); // 400 Bad Request와 함께 오류 메시지 반환
        }

        String token = memberService.login(loginRequestDto);
        log.info("token = {}", token);

        Cookie accessTokenCookie = new Cookie("access_token", token);
        accessTokenCookie.setMaxAge(60 * 60 * 24);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);

        return ResponseEntity.ok(HttpStatus.OK);
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

    // 회원탈퇴 페이지
    @GetMapping("/delete/user")
    public String deleteUserForm(Model model) {
        model.addAttribute("dto", new DeleteUserRequestDto());
        log.info(model);
        return "members/deleteMemberForm";
    }

    // 회원탈퇴
    @PostMapping("/delete/user")
    public ResponseEntity<?> deleteUser(@RequestHeader("X-HTTP-Method-Override") String httpMethodOverride, @RequestBody DeleteUserRequestDto dto, HttpServletResponse response) {
        log.info("Received deleteInfo request: {}", dto);
        if("DELETE".equals(httpMethodOverride)) {
            memberService.deleteMember(dto);
            Cookie cookie = new Cookie("access_token", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return ResponseEntity.ok(HttpStatus.OK);
        } else {
            return ResponseEntity.badRequest().build();
        }

    }

    // 내정보 확인
    @GetMapping("/myPage/{mId}")
    public String getMyPage(@PathVariable String mId, Model model) {
         MyPageResponseDto myPageResponseDto = memberService.getBorrowedBooks(Long.parseLong(mId));
         log.info("myPageResponseDto = {}", myPageResponseDto);
         model.addAttribute("myPageResponseDto", myPageResponseDto);
         model.addAttribute("mId", mId);

         return "members/myPage";
    }

    // 닉네임 변경
    @PutMapping("/changeNickname/{mId}")
    public ResponseEntity<?> changeNickname(@PathVariable Long mId, @RequestBody ChangeNicknameRequestDto dto) {
        log.info("mNickname = {}", dto.getNewNickname());
        memberService.changeNickname(mId, dto.getNewNickname());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 이메일 전송
    @PostMapping("emails/verification-requests")
    public ResponseEntity<?> sendMessage(@RequestParam("email") @Valid String email) {
        log.info("email = {}", email);
        memberService.sendCodeToEmail(email);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 비밀번호 재설정 페이지
    @GetMapping("/reset/pwd")
    public String resetPwdForm() {
        return "members/forgot-password";
    }

    // 비밀번호 재설정
    @PostMapping("/reset/pwd")
    public ResponseEntity<?> resetPwd(@RequestParam("email")String email) {
        memberService.sendPwdLinkToEmail(email);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 비밀번호 찾기 페이지
    @GetMapping("/find/pwd")
    public String findPwdForm(Model model) {
        model.addAttribute("dto", new FindPwdRequestDto());
        return "members/findPwdForm";
    }

    // 비밀번호 찾기
    @PostMapping("/find/pwd")
    public ResponseEntity<?> findPwd(@ModelAttribute FindPwdRequestDto dto) {
        log.info("dto = {}", dto);
        memberService.findMemberPwd(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 인증번호 검증
    @GetMapping("/emails/verifications")
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