package com.springboot.leadingbooks.controller;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import com.springboot.leadingbooks.services.BookService;
import com.springboot.leadingbooks.services.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.FindBookResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class BookController {
    private final BookService bookService;

    // 책 전체 조회
    @GetMapping("/all/books")
    public String getAllBooks(Model model) {
        List<Book> books = bookService.getAllBooks();
        model.addAttribute("books", books);

        return "/books/home";
    }

//    // 책 전체 조회
//    @GetMapping("/all/books")
//    @ResponseBody
//    public ResponseEntity<?> getAllBooks(@RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "10") int size, Model model) {
//        log.info("Received request : page = {}, size = {}", page, size);
//        return ResponseEntity.ok(bookService.getAllBooks(page, size));
//
//    }


    // 도서 등록
    @PostMapping("/post/books")
    public ResponseEntity<?> postAllBooks(@RequestBody BookCreateRequestDto bookCreateRequestDto) {
        bookService.RegisterBook(bookCreateRequestDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    // 책 제목으로 검색
    @GetMapping("/search/title")
    @ResponseBody
    public ResponseEntity<?> findBooksByTitle(@RequestParam String bName) {
        log.info("bName = {}", bName);
        FindBookResponseDto book = bookService.FindBookByTitle(bName);
        return ResponseEntity.ok().body(Collections.singletonList(book));
    }


    // 책 작가로 검색
    @GetMapping("/search/writer")
    @ResponseBody
    public ResponseEntity<?> findBooksByWriter(@RequestParam String bWriter) {
        log.info("bWriter = {}", bWriter);
        FindBookResponseDto book = bookService.FindBookByWriter(bWriter);
        return ResponseEntity.ok().body(Collections.singletonList(book));
    }

    // 책 카테고리로 검색
    @GetMapping("/search/category")
    @ResponseBody
    public ResponseEntity<?> findBooksByCategory(@RequestParam Category bCategory) {
        List<Book> book = bookService.FindBookByCategory(bCategory);
        return ResponseEntity.ok().body(book);
    }
}