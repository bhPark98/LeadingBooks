package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.enum_.Category;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.domain.repository.CheckOutRepository;
import com.springboot.leadingbooks.domain.repository.MemberRepository;
import com.springboot.leadingbooks.services.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.services.dto.response.FindBookResponseDto;
import com.springboot.leadingbooks.global.response.error.CustomException;
import com.springboot.leadingbooks.global.response.error.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final CheckOutRepository checkOutRepository;

    // 도서 등록 메서드
    public void RegisterBook(BookCreateRequestDto bookCreateRequestDto) {
        String bName = bookCreateRequestDto.getBName();

        if(bookRepository.isExistsBookName(bName)) {
            Book book = bookRepository.findBookByName(bName).orElseThrow(
                    () -> new CustomException(ErrorCode.NOT_FOUND_BOOK)
            );

            book.increaseBookCount();
            bookRepository.save(book);
        }
        else {
            Book book = Book.builder()
                    .bName(bookCreateRequestDto.getBName())
                    .bWriter(bookCreateRequestDto.getBWriter())
                    .bPublish(bookCreateRequestDto.getBPublish())
                    .bCategory(bookCreateRequestDto.getCategory())
                    .build();


            bookRepository.save(book);
        }

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
    // 책 카테고리로 책 리스트 조회하기
    public List<Book> FindBookByCategory(Category bCategory) {

        return bookRepository.findBookByCategory(bCategory);
    }

    // 책 전체 조회
    public List<Book> getAllBooks() {
        return bookRepository.findAllBooks();
    }

    // 책 전체 개수 로직
    public Long getTotalBooks(Long bId) {
        Optional<Long> optionalLong = bookRepository.countBooks(bId);

        return optionalLong.orElse(0L);
    }
}