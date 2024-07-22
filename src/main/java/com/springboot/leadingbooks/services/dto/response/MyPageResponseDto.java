package com.springboot.leadingbooks.services.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
public class MyPageResponseDto {
    private String name;
    private String nickname;
    private String email;
    private List<BorrowedBookInfoDto> booksInfo;

}
