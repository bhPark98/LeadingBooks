package com.springboot.leadingbooks.util.token;

import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.entity.RefreshToken;
import com.springboot.leadingbooks.domain.enum_.TokenValid;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.domain.repository.RefreshTokenRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import com.springboot.leadingbooks.services.dto.request.CustomUserInfoDto;
import com.springboot.leadingbooks.services.dto.response.TokenInfoDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

// JWT 관련 메서드를 제공하는 클래스
@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    public TokenValid tokenValid;

    public JwtUtil(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access_expiration_time}") long accessTokenExpTime,
            @Value("${jwt.refresh_expiration_time}") long refreshTokenExpTime,
            RefreshTokenRepository refreshTokenRepository, MemberRepository memberRepository, ModelMapper modelMapper) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
        this.refreshTokenRepository = refreshTokenRepository;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }


    /**
     * Access Token 생성
     * @param member
     * @return Access Token String
     */
    public String createAccessToken(CustomUserInfoDto member) {
        return createAccessToken(member, accessTokenExpTime);
    }
    /**
     * Refresh Token 생성
     * @param member, accessToken, expireTime
     * @return Refresh Token String
     */
    public String createRefreshToken(CustomUserInfoDto member, String accessToken) {
         return createRefreshToken(member, accessToken, refreshTokenExpTime);
    }

    private String createRefreshToken(CustomUserInfoDto member, String accessToken, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getId());
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        log.info("refreshToken = {}", refreshToken);

        refreshTokenRepository.save(new RefreshToken(member.getId(), accessToken, refreshToken));

        return refreshToken;
    }

    private String createAccessToken(CustomUserInfoDto member, long expireTime) {
        Claims claims = Jwts.claims();
        claims.put("memberId", member.getId());
        claims.put("email", member.getMEmail());
        claims.put("role", member.getRole());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(now.toInstant()))
                .setExpiration(Date.from(tokenValidity.toInstant()))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Token에서 User ID 추출
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }

    // JWT 검증
    public TokenValid validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            log.info("Token is Valid");
            tokenValid = TokenValid.VALID;
        } catch (ExpiredJwtException e) {
            log.info("Token is TimeOut");
            tokenValid = TokenValid.TIMEOUT;
        } catch (UnsupportedJwtException e) {
            log.info("Token is Unsupported");
            tokenValid = TokenValid.UNSUPPORTED;
        } catch (Exception e) {
            log.info("Token Exception");
            tokenValid = TokenValid.EX;
        }
        return tokenValid;
    }

    // JWT Claims 추출
    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


    // 쿠키에서 엑세스 토큰 추출
    public String resolveAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 리프레시 토큰 추출
    @Transactional(readOnly = true)
    public RefreshToken getRefreshToken(String accessToken) {
        return refreshTokenRepository.findByAccessToken(accessToken).orElse(null);
    }

    // 리프레시 토큰 삭제 - 액세스 토큰 이용
    @Transactional
    public void removeRefreshToken(String accessToken) {
        refreshTokenRepository.findByAccessToken(accessToken).ifPresent(refreshTokenRepository::delete);
    }

    // 액세스 토큰, 리프레시 토큰 재발급
    @Transactional
    public TokenInfoDto reCreateAccessToken(String originAccessToken, RefreshToken refreshToken) {
        Long userId = getUserId(refreshToken.getRefreshToken());
        Member member = memberRepository.findById(userId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_MEMBER)
        );

        CustomUserInfoDto info = modelMapper.map(member, CustomUserInfoDto.class);
        String newAccessToken = createAccessToken(info);

        removeRefreshToken(originAccessToken);

        String newRefreshToken = createRefreshToken(info, newAccessToken);

        return TokenInfoDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }




}
