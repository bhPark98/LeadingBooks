package com.springboot.leadingbooks.services.dto.response;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.entity.CheckOut;
import com.springboot.leadingbooks.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class myPageResponseDto {
    private String mName;
    private String mNickname;
    private String mEmail;
    private List<BorrowedBookInfoDto> booksInfo;

}
