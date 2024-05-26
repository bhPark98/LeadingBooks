package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.dto.request.JwtTokenRequestDto;

public interface MemberService {
    public JwtTokenRequestDto signin(String username, String password);


}
