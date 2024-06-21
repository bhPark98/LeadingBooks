package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import com.springboot.leadingbooks.services.BookService;
import com.springboot.leadingbooks.services.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.FindBookResponseDto;
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
    public List<Book> getAllBooks(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam int size) {
        return bookService.getAllBooks(page, size);
    }

    // 도서 등록
    @PostMapping("/post/books")
    public void postAllBooks(@RequestBody BookCreateRequestDto bookCreateRequestDto) {
        bookService.RegisterBook(bookCreateRequestDto);
    }

    // 책 제목으로 검색
    @GetMapping("/search/title")
    public FindBookResponseDto findBooksByTitle(@RequestParam String bName) {
        return bookService.FindBookByTitle(bName);
    }

    // 책 작가로 검색
    @GetMapping("search/writer")
    public FindBookResponseDto findBooksByWriter(@RequestParam String bWriter) {
        return bookService.FindBookByWriter(bWriter);
    }

    // 책 카테고리로 검색
    @GetMapping("search/category")
    public List<Book> findBooksByCategory(@RequestParam int pageNumber, @RequestParam int pageSize, @RequestParam Category bCategory) {
        return bookService.FindBookByCategory(pageNumber, pageSize, bCategory);
    }
}