package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import com.springboot.leadingbooks.services.dto.request.CustomUserInfoDto;
import com.springboot.leadingbooks.services.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.services.dto.response.BorrowedBookInfoDto;
import com.springboot.leadingbooks.services.dto.response.myPageResponseDto;
import com.springboot.leadingbooks.util.token.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final CheckOutRepository checkOutRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    // 로그인
    public String login(LoginRequestDto dto) {
        String email = dto.getMEmail();
        String password = dto.getMPwd();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMAIL)
        );

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if(!encoder.matches(password, member.getLoginData().getMPwd())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        CustomUserInfoDto info = modelMapper.map(member, CustomUserInfoDto.class);

        String accessToken = jwtUtil.createAceesToken(info);
        return accessToken;
    }


    // 마이페이지 조회
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
