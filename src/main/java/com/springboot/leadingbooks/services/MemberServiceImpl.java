package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import com.springboot.leadingbooks.services.dto.request.JwtTokenRequestDto;
import com.springboot.leadingbooks.services.dto.response.BorrowedBookInfoDto;
import com.springboot.leadingbooks.services.dto.response.myPageResponseDto;
import com.springboot.leadingbooks.util.token.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final CheckOutRepository checkOutRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    @Override
    public JwtTokenRequestDto signin(String username, String password) {
        // 1. username + password 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        Authentication authentication =
                authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

        return jwtTokenProvider.generateToken(authentication);
    }

    // 마이페이지 조회
    @Transactional
    public myPageResponseDto getBorrowedBooks(Long mId) {
        List<CheckOut> checkOuts = checkOutRepository.findByMemberId(mId);

        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        String mName = member.getLoginData().getMName();
        String mNickname = member.getLoginData().getMNickname();
        String mEmail = member.getLoginData().getMEmail();

        List<BorrowedBookInfoDto> borrowedBooks = checkOuts.stream()
                .map(checkOut -> new BorrowedBookInfoDto(
                        checkOut.getBook().getBName(),
                        checkOut.getCDate()
                ))
                .toList();

        return myPageResponseDto.builder()
                .mName(mName)
                .mNickname(mNickname)
                .mEmail(mEmail)
                .booksInfo(borrowedBooks)
                .build();
    }

    // 닉네임 변경
    public void changeNickname(Long mId, String mNickname) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        boolean isNicknameNotExist = memberRepository.isNotExistsNickname(mNickname);
        if(isNicknameNotExist) {
            member.getLoginData().changeNickname(mNickname);
            memberRepository.save(member);
        }
        else
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);


    }
}
