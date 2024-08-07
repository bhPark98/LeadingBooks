package com.springboot.leadingbooks.services.dto.response;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class FindBookResponseDto {
    private Long bId;
    private String bName;
    private String bWriter;
    private String bPublish;
    private Category bCategory;
    private Long bCount;

    public static FindBookResponseDto of(Book book) {
        return FindBookResponseDto.builder()
                .bId(book.getId())
                .bName(book.getBName())
                .bWriter(book.getBWriter())
                .bPublish(book.getBPublish())
                .bCategory(book.getBCategory())
                .bCount(book.getBCount())
                .build();
    }
}
