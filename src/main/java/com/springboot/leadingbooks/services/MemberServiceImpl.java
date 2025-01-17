package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.controller.dto.request.DeleteUserRequestDto;
import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.entity.RefreshToken;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.domain.repository.RefreshTokenRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import com.springboot.leadingbooks.services.dto.request.CustomUserInfoDto;
import com.springboot.leadingbooks.controller.dto.request.LoginRequestDto;
import com.springboot.leadingbooks.services.dto.request.FindPwdRequestDto;
import com.springboot.leadingbooks.services.dto.response.BorrowedBookInfoDto;
import com.springboot.leadingbooks.services.dto.response.TokenInfoDto;
import com.springboot.leadingbooks.services.dto.response.MyPageResponseDto;
import com.springboot.leadingbooks.util.token.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final CheckOutRepository checkOutRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordEncoder encoder;
    private static String authCode = "";
    private final RefreshTokenRepository refreshTokenRepository;


    @Value("${spring.mail.auth-code-expiration-millis}")
    private Long authCodeExpirationMillis;

    // 회원가입
    @Transactional
    public void join(MemberRequestDto dto) {
        log.info("회원가입 시도 - 이메일: {}", dto.getEmail());
        Optional<Member> valiMember = memberRepository.findMemberByEmail(dto.getEmail());
        if(valiMember.isPresent()) {
            log.warn("중복된 이메일 - 이메일: {}", dto.getEmail());
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
        String encodedPassword = "";
        String pwd = dto.getPwd();
        String rePwd = dto.getRePwd();
        if(pwd.equals(rePwd)) {
            // 비밀번호 해시처리
            encodedPassword = passwordEncoder.encode(pwd);
            log.info("비밀번호 해시 처리 완료 - 이메일: {}", dto.getEmail());
        } else {
            throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
        }

        Member member = Member.builder()
                .mName(dto.getName())
                .mEmail(dto.getEmail())
                .mNickname(dto.getNickname())
                .password(encodedPassword)
                .build();


        memberRepository.save(member);
        log.info("회원가입 성공 - 이메일: {}", dto.getEmail());

    }

    // 로그인
    @Transactional
    public TokenInfoDto login(LoginRequestDto dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMAIL)
        );

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if (!encoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.NOT_MATCH_PASSWORD);
        }

        CustomUserInfoDto info = modelMapper.map(member, CustomUserInfoDto.class);

        // 액세스 토큰 생성
        String accessToken = jwtUtil.createAccessToken(info);
        // 리프레시 토큰 저장
        String refreshToken = jwtUtil.createRefreshToken(info, accessToken);

        return TokenInfoDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 로그아웃
    @Transactional
    public void logout(Long mId) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );
        RefreshToken token = refreshTokenRepository.findById(member.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TOKEN)
        );

        log.info("token = {}", token);
        refreshTokenRepository.delete(token);
    }

    // 회원 탈퇴
    @Transactional
    public void deleteMember(DeleteUserRequestDto dto, HttpServletRequest request) {
        String email = dto.getEmail();
        String pwd = dto.getPwd();
        String rePwd = dto.getRePwd();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        RefreshToken token = refreshTokenRepository.findById(member.getId()).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_TOKEN)
        );

        log.info("token = {}", token);
        refreshTokenRepository.delete(token);


        if(pwd.equals(rePwd) && passwordEncoder.matches(pwd, member.getPassword())) {
            memberRepository.delete(member);
        }
        else {
            throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
        }


    }

    // 마이페이지 조회
    public MyPageResponseDto getBorrowedBooks(Long mId) {
        List<CheckOut> checkOuts = checkOutRepository.findByMemberId(mId);

        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        String mName = member.getMName();
        String mNickname = member.getMNickname();
        String mEmail = member.getMEmail();

        List<BorrowedBookInfoDto> borrowedBooks = checkOuts.stream()
                .map(checkOut -> new BorrowedBookInfoDto(
                        checkOut.getBook().getId(),
                        checkOut.getBook().getBName(),
                        checkOut.getCDate()
                ))
                .toList();

        return MyPageResponseDto.builder()
                .name(mName)
                .nickname(mNickname)
                .email(mEmail)
                .booksInfo(borrowedBooks)
                .build();
    }

    // 닉네임 변경
    @Transactional
    public void changeNickname(Long mId, String mNickname) {
        Member member = memberRepository.findById(mId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        boolean isNicknameNotExist = memberRepository.isNotExistsNickname(mNickname);
        log.info("isNicknameNotExist = {}", isNicknameNotExist);
        if(isNicknameNotExist) {
            member.changeNickname(mNickname);
            memberRepository.save(member);
        }
        else {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    // 이메일 인증번호 요청
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "leadingbooks 이메일 인증 번호";
        authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
    }

    // 비밀번호 찾기 링크 요청
    public void sendPwdLinkToEmail(String toEmail) {
        String title = "leadingbooks 비밀번호 찾기 링크";
        String link = "localhost:8080/find/pwd";
        mailService.sendEmail(toEmail, title, link);
    }

    // 인증번호 생성
    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for(int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new CustomException(ErrorCode.NO_SUCH_ALGORITHM);
        }
    }
    // 이메일 중복 확인
    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findMemberByEmail(email);
        if(member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new CustomException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    // 인증번호 검증
    public void verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        if(!this.authCode.equals(authCode)) {
            throw new CustomException(ErrorCode.NOT_MATCHES_AUTHCODE);
        }
    }

    // 토큰으로 유저정보 반환
    public Member getMemberByUsername(String username) {
        Long mId = Long.parseLong(username);
        return memberRepository.findById(mId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));
    }

    // 비밀번호 찾기
    @Transactional
    public void findMemberPwd(FindPwdRequestDto findPwdRequestDto) {
        String email = findPwdRequestDto.getEmail();
        Member member = memberRepository.findMemberByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_EMAIL)
        );
        String encodedPassword = "";
        String pwd = findPwdRequestDto.getPwd();
        String rePwd = findPwdRequestDto.getRePwd();
        if(pwd.equals(rePwd)) {
            encodedPassword = passwordEncoder.encode(pwd);
        }
        else {
            throw new CustomException(ErrorCode.NOT_MATCHES_PASSWORD);
        }

        member.changePassword(encodedPassword);
        memberRepository.update(member);
    }
}
