package com.springboot.leadingbooks.services.dto.request;

import com.springboot.leadingbooks.domain.enum_.Role;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserInfoDto {
    private Long id;
    private String mEmail;
    private String mName;
    private String mPwd;
    private Role role;
}
