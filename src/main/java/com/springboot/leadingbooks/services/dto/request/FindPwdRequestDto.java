package com.springboot.leadingbooks.services.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FindPwdRequestDto {
    private String email;
    private String pwd;
    private String rePwd;
}
