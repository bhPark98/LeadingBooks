package com.springboot.leadingbooks.services.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class MyPageResponseDto {
    private String mName;
    private String mNickname;
    private String mEmail;
    private List<BorrowedBookInfoDto> booksInfo;

}
