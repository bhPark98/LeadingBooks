package com.springboot.leadingbooks.admin.service.bookservice;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookManagementService {
    private final BookRepository bookRepository;

    // 전체 책 조회
    public List<Book> findAllBooks() {
        return bookRepository.findAllBooks();
    }
    // 책 제목으로 찾기
    public Book findBookByName(String bName) {
        Book book = bookRepository.findBookByName(bName).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_NAME)
        );
        return book;
    }
    // 책 작가로 찾기
    public Book findBookByWriter(String bWriter) {
        Book book = bookRepository.findBookByWriter(bWriter).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_WRITER)
        );
        return book;
    }
    // 책 카테고리별로 조회
    public List<Book> findBookByCategory(Category category) {
        return bookRepository.findBookByCategory(category);
    }
    // 단일 책 삭제
    public void deleteBook(Long bId) {
        Book book = bookRepository.findBookById(bId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_BOOK)
        );
        bookRepository.delete(book);
    }
    // 도서 등록
    public void createBooks(BookCreateRequestDto bookCreateRequestDto) {
        Book book = Book.builder()
                .bName(bookCreateRequestDto.getBName())
                .bWriter(bookCreateRequestDto.getBWriter())
                .bPublish(bookCreateRequestDto.getBPublish())
                .bCategory(bookCreateRequestDto.getCategory())
                .build();

        bookRepository.save(book);
    }


}
