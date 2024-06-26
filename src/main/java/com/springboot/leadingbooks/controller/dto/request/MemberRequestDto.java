package com.springboot.leadingbooks.controller.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8자 ~ 20자의 비밀번호여야 합니다.")
    @JsonProperty("pwd")
    private String pwd;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @JsonProperty("name")
    private String name;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(01[016789])(\\d{3,4})(\\d{4})",
            message = "올바른 휴대폰 번호를 입력해주세요.")
    @JsonProperty("phone")
    private String phone;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$",
            message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    @JsonProperty("nickname")
    private String nickname;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @JsonProperty("email")
    private String email;


}
