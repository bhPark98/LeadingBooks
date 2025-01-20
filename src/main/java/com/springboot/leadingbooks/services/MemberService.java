package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.services.dto.request.FindPwdRequestDto;
import com.springboot.leadingbooks.services.dto.response.TokenInfoDto;
import com.springboot.leadingbooks.services.dto.response.MyPageResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberService {

    public void join(MemberRequestDto dto);

    public TokenInfoDto login(LoginRequestDto dto);

    public void deleteMember(DeleteUserRequestDto dto, HttpServletRequest request);

    public MyPageResponseDto getBorrowedBooks(Long mId);

    public void changeNickname(Long mId, String mNickname);

    public void sendCodeToEmail(String toEmail);

    public void verifiedCode(String email, String authCode);

    public Member getMemberByUsername(String username);

    public void sendPwdLinkToEmail(String toEmail);

    public void findMemberPwd(FindPwdRequestDto findPwdRequestDto);

    public void logout(Long mId);
}