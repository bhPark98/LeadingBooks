package com.springboot.leadingbooks.util.token;

import com.springboot.leadingbooks.domain.entity.RefreshToken;
import com.springboot.leadingbooks.domain.enum_.TokenValid;
import com.springboot.leadingbooks.services.CustomUserDetailsService;
import com.springboot.leadingbooks.services.dto.response.TokenInfoDto;
import com.springboot.leadingbooks.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final CookieUtil cookieUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;


    /**
     * JWT 토큰 검증 필터 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // 인증이 필요 없는 경로
        if ("/sign/in".equals(path) || "/sign/up".equals(path) || path.startsWith("/bootstrap/") || path.startsWith("/members/")
                || "/emails/verifications".equals(path) || "/emails/verification-requests".equals(path) || "/reset/pwd".equals(path)
                || "/find/pwd".equals(path)) {
            log.info("No authentication needed for path: {}", path);
            filterChain.doFilter(request, response); // 바로 다음 필터로 넘기기
            return;
        }

        String accessToken = jwtUtil.resolveAccessToken(request);
        // 액세스 토큰이 없다면 로그인 페이지로 리다이렉트
        if(accessToken == null) {
            response.sendRedirect("/sign/in");
            return;
        }

        TokenValid accessTokenValid = jwtUtil.validateToken(accessToken);

        // 유효한 액세스 토큰일 때
        if(accessTokenValid == TokenValid.VALID) {
            log.info("유효한 액세스 토큰");
            Long userId = jwtUtil.getUserId(accessToken);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

            if(userDetails != null) {
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        // 액세스 토큰 만료 시, 리프레시 토큰과 액세스 토큰 재발급
        }else if(accessTokenValid == TokenValid.TIMEOUT) {
            RefreshToken refreshToken = jwtUtil.getRefreshToken(accessToken);
            // 리프레시 토큰이 없다면
            if(refreshToken == null) {
                response.sendRedirect("/sign/in");
                return;
            }
            log.info("리프레시 토큰 생성 = {}", refreshToken);

            // 유효한 리프레시 토큰일 때
            if(jwtUtil.validateToken(refreshToken.getRefreshToken()) == TokenValid.VALID) {
                // 회원 탈퇴 요청일 때
                String httpMethodOverride = request.getHeader("X-HTTP-Method-Override");
                if(httpMethodOverride != null) {
                    log.info("httpMethodOverride = {}", httpMethodOverride);
                    if(httpMethodOverride.equals("DELETE")) {
                        log.info("회원 탈퇴 요청 중");
                        filterChain.doFilter(request, response);
                        return;
                    }
                }

                // 새로운 액세스 토큰과 리프레시 토큰 생성 후 저장
                TokenInfoDto newToken = jwtUtil.reCreateAccessToken(accessToken, refreshToken);

                // 리프레시 토큰에서 유저 정보를 빼서 저장
                Long userId = jwtUtil.getUserId(newToken.getRefreshToken());
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId.toString());

                if(userDetails != null) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

                ResponseCookie accessCookie = cookieUtil.createAccessCookie(newToken.getAccessToken());
                ResponseCookie refreshCookie = cookieUtil.createRefreshCookie(newToken.getRefreshToken());

                response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
                response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

            }

            // 리프레시 토큰 만료 시
            else {
                jwtUtil.removeRefreshToken(accessToken);
                ResponseCookie responseCookie = cookieUtil.deleteAccessCookie();
                response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
                response.sendRedirect("/sign/in");
            }
        }


        filterChain.doFilter(request, response);    // 다음 필터로 넘기기
    }



}

