package com.springboot.leadingbooks.services;

import com.springboot.leadingbooks.domain.entity.Book;
import com.springboot.leadingbooks.domain.repository.BookRepository;
import com.springboot.leadingbooks.dto.request.BookCreateRequestDto;
import com.springboot.leadingbooks.dto.response.BookCreateResponseDto;
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
}
