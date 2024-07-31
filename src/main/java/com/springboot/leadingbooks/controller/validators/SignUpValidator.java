package com.springboot.leadingbooks.controller.validators;

import com.springboot.leadingbooks.controller.dto.request.MemberRequestDto;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class SignUpValidator implements Validator, PwdFormValidator, NameFormValidator {
    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberRequestDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberRequestDto memberRequestDto = (MemberRequestDto)target;

        String email = memberRequestDto.getEmail();
        String pwd = memberRequestDto.getPwd();
        String rePwd = memberRequestDto.getRePwd();
        String name = memberRequestDto.getName();
        String nickname = memberRequestDto.getNickname();

        // 이메일 중복 체크
        if(email != null && !email.isBlank() && memberRepository.findMemberByEmail(email).isPresent()) {
            errors.rejectValue("email", "", "이메일 중복");
        }

        // 닉네임 중복 체크
        if(nickname != null && !nickname.isBlank() && memberRepository.findMemberByNickname(nickname).isPresent()) {
            errors.rejectValue("nickname", "", "닉네임 중복");
        }

        // 비밀번호 일치 체크
        if(pwd != null && rePwd != null && !pwd.equals(rePwd)) {
            errors.rejectValue("pwd", "", "비밀번호가 일치하지 않습니다.");
        }

//        // 비밀번호 양식 확인
//        if(!check_number(pwd) || !check_alphabet(pwd) || !check_special(pwd)) {
//            errors.rejectValue("pwd", "", "비밀번호 양식이 잘못되었습니다.");
//        }


//        // 회원명 체크
//        if(name != null && !name.isBlank() && !checkName(name)) {
//            errors.rejectValue("name", "", "이름은 한글로 입력하세요.");
//        }


    }

    @Override
    public Errors validateObject(Object target) {
        return Validator.super.validateObject(target);
    }
}
