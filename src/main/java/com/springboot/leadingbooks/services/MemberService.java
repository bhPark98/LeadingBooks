package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.services.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.services.dto.response.myPageResponseDto;

public interface MemberService {

    public String login(LoginRequestDto dto);

    public myPageResponseDto getBorrowedBooks(Long mId);

    public void changeNickname(Long mId, String mNickname);
}
