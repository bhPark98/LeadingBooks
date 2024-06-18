package com.springboot.leadingbooks.services.dto.request;

import com.springboot.leadingbooks.domain.enum_.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class BookCreateRequestDto {
    private String bName;
    private String bWriter;
    private String bPublish;
    private Category category;
}
