package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.dto.request.JwtTokenRequestDto;
import com.springboot.leadingbooks.util.token.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public JwtTokenRequestDto signin(String username, String password) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }
}
