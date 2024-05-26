package com.springboot.leadingbooks.dto.response;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class BookCreateResponseDto {
    private String bName;
    private String bWriter;
    private String bPublish;
    private Category category;
    private Long bCount;

    public static BookCreateResponseDto of(Book book) {
        return BookCreateResponseDto.builder()
                .bName(book.getBName())
                .bWriter(book.getBWriter())
                .bPublish(book.getBPublish())
                .category(book.getBCategory())
                .bCount(book.getBCount())
                .build();
    }
}
