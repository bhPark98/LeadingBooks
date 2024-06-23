package com.springboot.leadingbooks.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    protected String timestamp;
    protected String status;
    protected String code;
    protected String message;
}
