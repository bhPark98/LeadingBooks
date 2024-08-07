package com.springboot.leadingbooks.services.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class BorrowedBookInfoDto {
    private Long bId;
    private String bName;
    private int cDate;
}
