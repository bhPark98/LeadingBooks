package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.services.BookService;
import com.springboot.leadingbooks.services.BookServiceImpl;
import com.springboot.leadingbooks.services.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.BookCreateResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/v1")
public class BookController {
    private final BookService bookService;

    // 책 전체 조회
    @GetMapping("/all/books")
    public List<Book> getAllBooks(@RequestParam int page, @RequestParam int size) {
        return bookService.getAllBooks(page, size);
    }

    // 도서 등록
    @PostMapping("/post/books")
    public void postAllBooks(@RequestBody BookCreateRequestDto bookCreateRequestDto) {
        bookService.RegisterBook(bookCreateRequestDto);
    }
}
