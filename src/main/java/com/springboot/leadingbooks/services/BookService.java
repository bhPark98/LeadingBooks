package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.services.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookCreateResponseDto;

public interface BookService {
    public BookCreateResponseDto RegisterBook(BookCreateRequestDto bookCreateRequestDto);
}
