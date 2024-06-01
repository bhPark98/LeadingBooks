package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.dto.response.BookCreateResponseDto;
import com.springboot.leadingbooks.dto.response.FindBookResponseDto;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    // 도서 등록 메서드
    public BookCreateResponseDto RegisterBook(BookCreateRequestDto bookCreateRequestDto) {
        Book book = Book.builder()
                .bName(bookCreateRequestDto.getBName())
                .bWriter(bookCreateRequestDto.getBWriter())
                .bPublish(bookCreateRequestDto.getBPublish())
                .bCategory(bookCreateRequestDto.getCategory())
                .build();

        bookRepository.save(book);

        return BookCreateResponseDto.of(book);

    }
    // 책 제목으로 검색하기
    public FindBookResponseDto FindBookByTitle(String bName) {
        Book book = bookRepository.findBookByName(bName).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_NAME)
        );

        return FindBookResponseDto.of(book);
    }
    // 책 작가로 검색하기
    public FindBookResponseDto FindBookByWriter(String bWriter) {
        Book book = bookRepository.findBookByWriter(bWriter).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_WRITER)
        );

        return FindBookResponseDto.of(book);
    }
    // 책 카테고리로 검색하기
    public FindBookResponseDto FindBookByCategory(Category category) {
        Book book = bookRepository.findBookByCategory(category).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_CATEGORY)
        );

        return FindBookResponseDto.of(book);
    }
}
