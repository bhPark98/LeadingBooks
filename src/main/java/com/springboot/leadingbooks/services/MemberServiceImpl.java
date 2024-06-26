package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Login;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.enum_.Role;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import com.springboot.leadingbooks.services.dto.request.CustomUserInfoDto;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.services.dto.response.BorrowedBookInfoDto;
import com.springboot.leadingbooks.services.dto.response.myPageResponseDto;
import com.springboot.leadingbooks.util.token.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final CheckOutRepository checkOutRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    // 회원가입
    public Long join(MemberRequestDto dto) {
        log.info("회원가입 시도 - 이메일: {}", dto.getEmail());
        Optional<Member> valiMember = memberRepository.findMemberByEmail(dto.getEmail());
        if(valiMember.isPresent()) {
            log.warn("중복된 이메일 - 이메일: {}", dto.getEmail());
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
        // 비밀번호 해시처리
        String encodedPassword = passwordEncoder.encode(dto.getPwd());
        log.info("비밀번호 해시 처리 완료 - 이메일: {}", dto.getEmail());

        Login updatedLogin = Login.builder()
                .mName(dto.getName())
                .mEmail(dto.getEmail())
                .mNickname(dto.getNickname())
                .mPwd(encodedPassword)
                .mPhone(dto.getPhone())
                .build();

        Member member = Member.builder()
                .loginData(updatedLogin)
                .mRole(Role.USER)
                .build();

        memberRepository.save(member);
        log.info("회원가입 성공 - 이메일: {}", dto.getEmail());

        return member.getId();
    }

    // 로그인
    public String login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPwd();
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

    // 회원 탈퇴
    public void deleteMember(DeleteUserRequestDto dto) {
        String email = dto.getEmail();
        String pwd = dto.getPwd();
        String rePwd = dto.getRePwd();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        if(pwd.equals(rePwd) && passwordEncoder.matches(pwd, member.getLoginData().getMPwd())) {
            memberRepository.delete(member);
        }
        else {
            throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
        }
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
