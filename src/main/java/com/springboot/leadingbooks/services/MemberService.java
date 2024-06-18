package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.services.dto.request.JwtTokenRequestDto;
import com.springboot.leadingbooks.services.dto.response.myPageResponseDto;

public interface MemberService {
    public JwtTokenRequestDto signin(String username, String password);

    public myPageResponseDto getBorrowedBooks(Long mId);

    public void changeNickname(Long mId, String mNickname);
}
