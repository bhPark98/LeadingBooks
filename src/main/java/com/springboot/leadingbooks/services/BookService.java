package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import com.springboot.leadingbooks.services.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.FindBookResponseDto;
import java.util.List;

public interface BookService {
    public void RegisterBook(BookCreateRequestDto bookCreateRequestDto);

    public FindBookResponseDto FindBookByTitle(String bName);

    public FindBookResponseDto FindBookByWriter(String bWriter);

    public List<Book> FindBookByCategory(int pageNumber, int pageSize, Category bCategory);

    public List<Book> getAllBooks(int pageNumber, int pageSize);

    public Long getTotalBooks();
}
