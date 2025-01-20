package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Member;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import com.springboot.leadingbooks.services.dto.request.CustomUserInfoDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MEMBER));

        CustomUserInfoDto dto = modelMapper.map(member, CustomUserInfoDto.class);

        return new CustomUserDetails(dto);
    }
}
