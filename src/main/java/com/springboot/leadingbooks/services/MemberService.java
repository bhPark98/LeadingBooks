package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.services.dto.response.MyPageResponseDto;

public interface MemberService {

    public void join(MemberRequestDto dto);

    public String login(LoginRequestDto dto);

    public void deleteMember(DeleteUserRequestDto dto);

    public MyPageResponseDto getBorrowedBooks(Long mId);

    public void changeNickname(Long mId, String mNickname);

    public void sendCodeToEmail(String toEmail);

    public void verifiedCode(String email, String authCode);

    public Member getMemberByUsername(String username);
}