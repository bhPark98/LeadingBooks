package com.springboot.leadingbooks.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequestDto {
    private String email;
    private String pwd;
    private String rePwd;
}
