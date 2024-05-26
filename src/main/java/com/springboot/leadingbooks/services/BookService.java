package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.dto.response.BookCreateResponseDto;

public interface BookService {
    public BookCreateResponseDto RegisterBook(BookCreateRequestDto bookCreateRequestDto);
}
